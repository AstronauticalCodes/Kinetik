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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class Tier3CrossFeatureInteractionsTest {

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
    fun testTradeExecutionCrossFeatureCascade() = runBlocking {
        
        repository.seedInitialDataIfEmpty()
        val initialHoldings = repository.getAllHoldings()
        val marketPrices = mapOf("BTC" to 60000.0, "ETH" to 3000.0, "SOL" to 150.0)

        val initialNetWorth = repository.calculateNetWorth(initialHoldings, marketPrices)
        
        assertEquals(95750.0, initialNetWorth, 0.001)

        
        val result = repository.executeTrade("USD", "BTC", 12000.0, marketPrices)
        assertTrue(result.isSuccess)

        
        val updatedUsdHolding = database.holdingDao().getHoldingBySymbol("USD")
        val updatedBtcHolding = database.holdingDao().getHoldingBySymbol("BTC")

        assertEquals(38000.0, updatedUsdHolding?.amount ?: 0.0, 0.001) 
        assertEquals(0.7, updatedBtcHolding?.amount ?: 0.0, 0.0001)   

        
        val updatedHoldings = repository.getAllHoldings()
        val updatedNetWorth = repository.calculateNetWorth(updatedHoldings, marketPrices)
        
        assertEquals(95750.0, updatedNetWorth, 0.001)

        
        val transactions = repository.getAllTransactions()
        assertEquals(1, transactions.size)

        val tx = transactions.first()
        assertEquals("BUY", tx.type)
        assertEquals("USD", tx.fromAsset)
        assertEquals("BTC", tx.toAsset)
        assertEquals(12000.0, tx.fromAmount, 0.001)
        assertEquals(0.2, tx.toAmount, 0.0001)
        assertEquals(60000.0, tx.priceUsd, 0.001)
        assertEquals("COMPLETED", tx.status)
        assertTrue(tx.timestamp > 0)
    }
}
