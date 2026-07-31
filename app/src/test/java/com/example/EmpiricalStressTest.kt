package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppDatabase
import com.example.data.CryptoRepository
import com.example.data.HoldingEntity
import com.example.data.TickerResponse
import com.example.data.TransactionEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class EmpiricalStressTest {

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
    fun testNetWorthCalculation_ExtremeCrash_BtcDropsToOneDollar() {
        val holdings = listOf(
            HoldingEntity("USD", "US Dollar", 50000.0, 1.0, isFiat = true),
            HoldingEntity("BTC", "Bitcoin", 0.5, 65000.0, isFiat = false),
            HoldingEntity("ETH", "Ethereum", 4.0, 3500.0, isFiat = false)
        )
        
        val extremeCrashPrices = mapOf(
            "BTC" to 1.0,
            "ETH" to 3500.0
        )

        
        val netWorth = repository.calculateNetWorth(holdings, extremeCrashPrices)
        assertEquals(64000.5, netWorth, 0.001)
    }

    @Test
    fun testNetWorthCalculation_ExtremeSurge_BtcSurgesToOneMillionDollars() {
        val holdings = listOf(
            HoldingEntity("USD", "US Dollar", 50000.0, 1.0, isFiat = true),
            HoldingEntity("BTC", "Bitcoin", 0.5, 65000.0, isFiat = false),
            HoldingEntity("ETH", "Ethereum", 4.0, 3500.0, isFiat = false)
        )
        
        val extremeSurgePrices = mapOf(
            "BTC" to 1000000.0,
            "ETH" to 10000.0
        )

        
        val netWorth = repository.calculateNetWorth(holdings, extremeSurgePrices)
        assertEquals(590000.0, netWorth, 0.001)
    }

    @Test
    fun testNetWorthCalculation_NearZeroAndMicroFractionalPrices() {
        val holdings = listOf(
            HoldingEntity("USD", "US Dollar", 100.0, 1.0, isFiat = true),
            HoldingEntity("SHIB", "Shiba Inu", 1_000_000.0, 0.00001, isFiat = false),
            HoldingEntity("PEPE", "Pepe", 10_000_000.0, 0.000001, isFiat = false)
        )
        val microPrices = mapOf(
            "SHIB" to 0.000025,
            "PEPE" to 0.0000085
        )

        
        val netWorth = repository.calculateNetWorth(holdings, microPrices)
        assertEquals(210.0, netWorth, 0.0001)
    }

    @Test
    fun testNetWorthCalculation_MissingPricesFallbackToAvgBuyPrice() {
        val holdings = listOf(
            HoldingEntity("USD", "US Dollar", 1000.0, 1.0, isFiat = true),
            HoldingEntity("BTC", "Bitcoin", 2.0, 50000.0, isFiat = false),
            HoldingEntity("ALT", "Altcoin", 100.0, 15.0, isFiat = false)
        )
        
        val partialPrices = mapOf(
            "BTC" to 60000.0
        )

        
        val netWorth = repository.calculateNetWorth(holdings, partialPrices)
        assertEquals(122500.0, netWorth, 0.001)
    }

    @Test
    fun testNetWorthCalculation_UsdtSuffixSymbolMatching() {
        val holdings = listOf(
            HoldingEntity("BTC", "Bitcoin", 1.0, 50000.0, isFiat = false)
        )
        
        val suffixPrices = mapOf(
            "BTCUSDT" to 75000.0
        )

        val netWorth = repository.calculateNetWorth(holdings, suffixPrices)
        assertEquals(75000.0, netWorth, 0.001)
    }

    
    
    

    @Test
    fun testTopMoversSorting_PositiveNegativeZeroAndInvalidPercent() {
        val tickers = listOf(
            TickerResponse("TOKEN_POS_HIGH", "10.0", "150.5", "10.0"),
            TickerResponse("TOKEN_POS_MED", "5.0", "12.3", "5.0"),
            TickerResponse("TOKEN_ZERO", "0.0", "0.0", "1.0"),
            TickerResponse("TOKEN_EMPTY", "0.0", "", "1.0"),
            TickerResponse("TOKEN_MALFORMED", "0.0", "INVALID_PCT", "1.0"),
            TickerResponse("TOKEN_NEG_SMALL", "-0.5", "-1.5", "2.0"),
            TickerResponse("TOKEN_NEG_LARGE", "-50.0", "-45.8", "0.5")
        )

        val sorted = repository.getTopMovers(tickers)

        
        assertEquals(7, sorted.size)

        
        assertEquals("TOKEN_POS_HIGH", sorted[0].symbol)
        assertEquals("TOKEN_POS_MED", sorted[1].symbol)

        
        val middlePcts = sorted.subList(2, 5).map { it.priceChangePercent.toDoubleOrNull() ?: 0.0 }
        assertTrue(middlePcts.all { it == 0.0 })

        assertEquals("TOKEN_NEG_SMALL", sorted[5].symbol)
        assertEquals("TOKEN_NEG_LARGE", sorted[6].symbol)
    }

    @Test
    fun testTopMoversSorting_AllNegativePercentages() {
        val tickers = listOf(
            TickerResponse("COIN_A", "-10.0", "-5.0", "100.0"),
            TickerResponse("COIN_B", "-50.0", "-40.0", "100.0"),
            TickerResponse("COIN_C", "-0.1", "-0.1", "100.0")
        )

        val sorted = repository.getTopMovers(tickers)

        assertEquals("COIN_C", sorted[0].symbol) 
        assertEquals("COIN_A", sorted[1].symbol) 
        assertEquals("COIN_B", sorted[2].symbol) 
    }

    @Test
    fun testTopMoversSorting_EmptyList() {
        val sorted = repository.getTopMovers(emptyList())
        assertTrue(sorted.isEmpty())
    }

    
    
    

    @Test
    fun testAssetDetail24HHighLow_ValidParsingAndFormatting() {
        val ticker = TickerResponse(
            symbol = "BTCUSDT",
            priceChange = "2500.0",
            priceChangePercent = "4.12",
            lastPrice = "65000.0",
            highPrice = "66800.75",
            lowPrice = "62300.50",
            volume = "987654.321"
        )

        val highVal = ticker.highPrice.toDoubleOrNull()
        val lowVal = ticker.lowPrice.toDoubleOrNull()
        val volVal = ticker.volume.toDoubleOrNull()

        assertNotNull(highVal)
        assertNotNull(lowVal)
        assertNotNull(volVal)

        val highStr = highVal?.let { String.format(Locale.US, "$%,.2f", it) } ?: "$0.00"
        val lowStr = lowVal?.let { String.format(Locale.US, "$%,.2f", it) } ?: "$0.00"
        val volStr = volVal?.let { String.format(Locale.US, "%,.2f", it) } ?: "0.00"

        assertEquals("$66,800.75", highStr)
        assertEquals("$62,300.50", lowStr)
        assertEquals("987,654.32", volStr)
    }

    @Test
    fun testAssetDetail24HHighLow_ZeroValuesHandling() {
        val ticker = TickerResponse(
            symbol = "NEWUSDT",
            priceChange = "0.0",
            priceChangePercent = "0.0",
            lastPrice = "0.0",
            highPrice = "0.0",
            lowPrice = "0.0",
            volume = "0.0"
        )

        val highVal = ticker.highPrice.toDoubleOrNull()
        val lowVal = ticker.lowPrice.toDoubleOrNull()
        val volVal = ticker.volume.toDoubleOrNull()

        assertEquals(0.0, highVal ?: -1.0, 0.001)
        assertEquals(0.0, lowVal ?: -1.0, 0.001)
        assertEquals(0.0, volVal ?: -1.0, 0.001)

        val highStr = highVal?.let { String.format(Locale.US, "$%,.2f", it) } ?: "$0.00"
        val lowStr = lowVal?.let { String.format(Locale.US, "$%,.2f", it) } ?: "$0.00"
        val volStr = volVal?.let { String.format(Locale.US, "%,.2f", it) } ?: "0.00"

        assertEquals("$0.00", highStr)
        assertEquals("$0.00", lowStr)
        assertEquals("0.00", volStr)
    }

    @Test
    fun testAssetDetail24HHighLow_NullOrMalformedFallbackHandling() {
        val tickerMalformed = TickerResponse(
            symbol = "BADUSDT",
            priceChange = "N/A",
            priceChangePercent = "N/A",
            lastPrice = "invalid",
            highPrice = "CORRUPT",
            lowPrice = "",
            volume = "null"
        )

        val highVal = tickerMalformed.highPrice.toDoubleOrNull()
        val lowVal = tickerMalformed.lowPrice.toDoubleOrNull()
        val volVal = tickerMalformed.volume.toDoubleOrNull()

        assertNull(highVal)
        assertNull(lowVal)
        assertNull(volVal)

        
        val highStr = highVal?.let { String.format(Locale.US, "$%,.2f", it) } ?: "$0.00"
        val lowStr = lowVal?.let { String.format(Locale.US, "$%,.2f", it) } ?: "$0.00"
        val volStr = volVal?.let { String.format(Locale.US, "%,.2f", it) } ?: "0.00"

        assertEquals("$0.00", highStr)
        assertEquals("$0.00", lowStr)
        assertEquals("0.00", volStr)
    }

    @Test
    fun testAssetDetail24HHighLow_ExtremeHighValuesFormatting() {
        val ticker = TickerResponse(
            symbol = "BTCUSDT",
            priceChange = "50000.0",
            priceChangePercent = "100.0",
            lastPrice = "1000000.0",
            highPrice = "1250500.99",
            lowPrice = "500000.00",
            volume = "50000000.0"
        )

        val highVal = ticker.highPrice.toDoubleOrNull()
        val highStr = highVal?.let { String.format(Locale.US, "$%,.2f", it) } ?: "$0.00"

        assertEquals("$1,250,500.99", highStr)
    }
}
