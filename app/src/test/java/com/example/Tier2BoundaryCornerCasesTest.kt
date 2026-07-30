package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppDatabase
import com.example.data.CryptoRepository
import com.example.data.HoldingEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class Tier2BoundaryCornerCasesTest {

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
    fun testInsufficientBalanceTradeValidation() = runBlocking {
        database.holdingDao().insertHolding(HoldingEntity("USD", "US Dollar", 100.0, 1.0, isFiat = true))
        val marketPrices = mapOf("BTC" to 50000.0)

        // Attempting to spend $500 when balance is $100
        val result = repository.executeTrade("USD", "BTC", 500.0, marketPrices)

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is IllegalStateException)
        assertTrue(exception?.message?.contains("Insufficient balance") == true)

        // USD Balance remains untouched
        val usdHolding = database.holdingDao().getHoldingBySymbol("USD")
        assertEquals(100.0, usdHolding?.amount ?: 0.0, 0.001)
    }

    @Test
    fun testZeroAndNegativeTradeAmounts() = runBlocking {
        database.holdingDao().insertHolding(HoldingEntity("USD", "US Dollar", 1000.0, 1.0, isFiat = true))
        val marketPrices = mapOf("BTC" to 50000.0)

        // Zero trade amount
        val resultZero = repository.executeTrade("USD", "BTC", 0.0, marketPrices)
        assertTrue(resultZero.isFailure)
        assertTrue(resultZero.exceptionOrNull() is IllegalArgumentException)

        // Negative trade amount
        val resultNegative = repository.executeTrade("USD", "BTC", -50.0, marketPrices)
        assertTrue(resultNegative.isFailure)
        assertTrue(resultNegative.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun testMaxBalanceTrades() = runBlocking {
        database.holdingDao().insertHolding(HoldingEntity("USD", "US Dollar", 5000.0, 1.0, isFiat = true))
        val marketPrices = mapOf("BTC" to 50000.0)

        // Trade 100% of USD balance ($5000)
        val result = repository.executeTrade("USD", "BTC", 5000.0, marketPrices)

        assertTrue(result.isSuccess)

        val usdHolding = database.holdingDao().getHoldingBySymbol("USD")
        val btcHolding = database.holdingDao().getHoldingBySymbol("BTC")

        assertEquals(0.0, usdHolding?.amount ?: -1.0, 0.001)
        assertEquals(0.1, btcHolding?.amount ?: 0.0, 0.0001) // $5000 / $50000 = 0.1 BTC
    }

    @Test
    fun testUnknownSymbolHandling() = runBlocking {
        database.holdingDao().insertHolding(HoldingEntity("USD", "US Dollar", 1000.0, 1.0, isFiat = true))
        val marketPrices = mapOf("BTC" to 50000.0)

        // Attempting to trade from an unseeded/unknown source symbol
        val resultUnknownSource = repository.executeTrade("UNKNOWN_COIN", "USD", 10.0, marketPrices)
        assertTrue(resultUnknownSource.isFailure)
        assertTrue(resultUnknownSource.exceptionOrNull()?.message?.contains("Unknown source asset") == true)

        // Attempting to trade with missing market price for destination asset
        val resultMissingPrice = repository.executeTrade("USD", "NO_PRICE_COIN", 100.0, marketPrices)
        assertTrue(resultMissingPrice.isFailure)
        assertTrue(resultMissingPrice.exceptionOrNull()?.message?.contains("Invalid or missing market price") == true)
    }
}
