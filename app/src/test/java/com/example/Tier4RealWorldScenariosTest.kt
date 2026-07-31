package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppDatabase
import com.example.data.CryptoRepository
import com.example.data.HoldingEntity
import com.example.data.TransactionEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class Tier4RealWorldScenariosTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: CryptoRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = CryptoRepository(database.holdingDao(), database.transactionDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testRealWorldMultiTradeLifecycleScenario() = runBlocking {
        
        repository.seedInitialDataIfEmpty()
        val marketPrices = mapOf(
            "BTC" to 50000.0,
            "ETH" to 2500.0,
            "SOL" to 150.0
        )

        

        
        val trade1Result = repository.executeTrade("USD", "BTC", 10000.0, marketPrices)
        assertTrue(trade1Result.isSuccess)

        var usdHolding = database.holdingDao().getHoldingBySymbol("USD")
        var btcHolding = database.holdingDao().getHoldingBySymbol("BTC")
        assertEquals(40000.0, usdHolding?.amount ?: 0.0, 0.001)
        assertEquals(0.7, btcHolding?.amount ?: 0.0, 0.0001) 

        
        
        val trade2Result = repository.executeTrade("BTC", "ETH", 0.2, marketPrices)
        assertTrue(trade2Result.isSuccess)

        btcHolding = database.holdingDao().getHoldingBySymbol("BTC")
        var ethHolding = database.holdingDao().getHoldingBySymbol("ETH")
        assertEquals(0.5, btcHolding?.amount ?: 0.0, 0.0001) 
        assertEquals(8.0, ethHolding?.amount ?: 0.0, 0.0001) 

        
        val updatedPrices = mapOf(
            "BTC" to 50000.0,
            "ETH" to 3000.0,
            "SOL" to 150.0
        )

        
        val trade3Result = repository.executeTrade("ETH", "USD", 4.0, updatedPrices)
        assertTrue(trade3Result.isSuccess)

        usdHolding = database.holdingDao().getHoldingBySymbol("USD")
        ethHolding = database.holdingDao().getHoldingBySymbol("ETH")
        assertEquals(52000.0, usdHolding?.amount ?: 0.0, 0.001) 
        assertEquals(4.0, ethHolding?.amount ?: 0.0, 0.0001)   

        
        val finalHoldings = repository.getAllHoldings().associateBy { it.assetSymbol }
        assertEquals(52000.0, finalHoldings["USD"]?.amount ?: 0.0, 0.001)
        assertEquals(0.5, finalHoldings["BTC"]?.amount ?: 0.0, 0.0001)
        assertEquals(4.0, finalHoldings["ETH"]?.amount ?: 0.0, 0.0001)
        assertEquals(25.0, finalHoldings["SOL"]?.amount ?: 0.0, 0.0001)

        
        val history = repository.getAllTransactions()
        assertEquals(3, history.size)

        
        val lastTx = history[0]
        assertEquals("SELL", lastTx.type)
        assertEquals("ETH", lastTx.fromAsset)
        assertEquals("USD", lastTx.toAsset)
        assertEquals(4.0, lastTx.fromAmount, 0.0001)
        assertEquals(12000.0, lastTx.toAmount, 0.001)

        val swapTx = history[1]
        assertEquals("SWAP", swapTx.type)
        assertEquals("BTC", swapTx.fromAsset)
        assertEquals("ETH", swapTx.toAsset)
        assertEquals(0.2, swapTx.fromAmount, 0.0001)
        assertEquals(4.0, swapTx.toAmount, 0.0001)

        val firstTx = history[2]
        assertEquals("BUY", firstTx.type)
        assertEquals("USD", firstTx.fromAsset)
        assertEquals("BTC", firstTx.toAsset)
        assertEquals(10000.0, firstTx.fromAmount, 0.001)
        assertEquals(0.2, firstTx.toAmount, 0.0001)
    }
}
