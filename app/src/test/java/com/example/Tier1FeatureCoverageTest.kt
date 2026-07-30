package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppDatabase
import com.example.data.CryptoRepository
import com.example.data.HoldingEntity
import com.example.data.TickerResponse
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
class Tier1FeatureCoverageTest {

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
    fun testRoomDatabaseSeedValidation() = runBlocking {
        repository.seedInitialDataIfEmpty()
        val holdings = repository.getAllHoldings()

        assertEquals(4, holdings.size)
        val symbols = holdings.map { it.assetSymbol }
        assertTrue(symbols.contains("USD"))
        assertTrue(symbols.contains("BTC"))
        assertTrue(symbols.contains("ETH"))
        assertTrue(symbols.contains("SOL"))
    }

    @Test
    fun testFiatBalanceInitialization() = runBlocking {
        repository.seedInitialDataIfEmpty()
        val usdHolding = database.holdingDao().getHoldingBySymbol("USD")

        assertNotNull(usdHolding)
        assertEquals("USD", usdHolding?.assetSymbol)
        assertEquals(50000.0, usdHolding?.amount ?: 0.0, 0.001)
        assertTrue(usdHolding?.isFiat == true)
    }

    @Test
    fun testDynamicPortfolioCalculationLogic() {
        val holdings = listOf(
            HoldingEntity("USD", "US Dollar", 50000.0, 1.0, isFiat = true),
            HoldingEntity("BTC", "Bitcoin", 0.5, 60000.0, isFiat = false),
            HoldingEntity("ETH", "Ethereum", 4.0, 3000.0, isFiat = false)
        )
        val currentPrices = mapOf(
            "BTC" to 60000.0,
            "ETH" to 3000.0
        )

        // Expected Net Worth: 50,000 + (0.5 * 60,000) + (4.0 * 3,000) = 50,000 + 30,000 + 12,000 = 92,000 USD
        val netWorth = repository.calculateNetWorth(holdings, currentPrices)
        assertEquals(92000.0, netWorth, 0.001)
    }

    @Test
    fun testTopMoversPriceChangePercentSorting() {
        val tickers = listOf(
            TickerResponse("BTCUSDT", "100.0", "2.5", "65000.0"),
            TickerResponse("ETHUSDT", "50.0", "12.8", "3500.0"),
            TickerResponse("SOLUSDT", "-5.0", "-3.2", "145.0"),
            TickerResponse("DOGEUSDT", "0.01", "8.1", "0.15")
        )

        val sorted = repository.getTopMovers(tickers)

        assertEquals("ETHUSDT", sorted[0].symbol)  // 12.8%
        assertEquals("DOGEUSDT", sorted[1].symbol) // 8.1%
        assertEquals("BTCUSDT", sorted[2].symbol)  // 2.5%
        assertEquals("SOLUSDT", sorted[3].symbol)  // -3.2%
    }

    @Test
    fun test24HHighLowParsing() {
        val ticker = TickerResponse(
            symbol = "BTCUSDT",
            priceChange = "1500.0",
            priceChangePercent = "2.35",
            lastPrice = "65200.0",
            highPrice = "66500.50",
            lowPrice = "63400.25",
            volume = "12345.67"
        )

        val high = ticker.highPrice.toDoubleOrNull()
        val low = ticker.lowPrice.toDoubleOrNull()
        val vol = ticker.volume.toDoubleOrNull()

        assertNotNull(high)
        assertNotNull(low)
        assertNotNull(vol)
        assertEquals(66500.50, high ?: 0.0, 0.001)
        assertEquals(63400.25, low ?: 0.0, 0.001)
        assertEquals(12345.67, vol ?: 0.0, 0.001)
    }
}
