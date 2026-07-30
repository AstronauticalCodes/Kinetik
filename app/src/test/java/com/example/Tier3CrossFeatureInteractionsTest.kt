package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppDatabase
import com.example.data.CryptoRepository
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
        // Step 1: Seed database ($50,000 USD, 0.5 BTC, 4 ETH, 25 SOL)
        repository.seedInitialDataIfEmpty()
        val initialHoldings = repository.getAllHoldings()
        val marketPrices = mapOf("BTC" to 60000.0, "ETH" to 3000.0, "SOL" to 150.0)

        val initialNetWorth = repository.calculateNetWorth(initialHoldings, marketPrices)
        // 50,000 + (0.5*60000) + (4*3000) + (25*150) = 50000 + 30000 + 12000 + 3750 = 95,750
        assertEquals(95750.0, initialNetWorth, 0.001)

        // Step 2: Execute trade: Buy $12,000 USD worth of BTC at $60,000/BTC
        val result = repository.executeTrade("USD", "BTC", 12000.0, marketPrices)
        assertTrue(result.isSuccess)

        // Step 3: Room holdings update verification
        val updatedUsdHolding = database.holdingDao().getHoldingBySymbol("USD")
        val updatedBtcHolding = database.holdingDao().getHoldingBySymbol("BTC")

        assertEquals(38000.0, updatedUsdHolding?.amount ?: 0.0, 0.001) // 50,000 - 12,000
        assertEquals(0.7, updatedBtcHolding?.amount ?: 0.0, 0.0001)   // 0.5 + (12,000 / 60,000) = 0.7 BTC

        // Step 4: Dynamic portfolio balance recalculation
        val updatedHoldings = repository.getAllHoldings()
        val updatedNetWorth = repository.calculateNetWorth(updatedHoldings, marketPrices)
        // Net worth after buying at market price remains $95,750
        assertEquals(95750.0, updatedNetWorth, 0.001)

        // Step 5: Transaction log record creation verification
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
