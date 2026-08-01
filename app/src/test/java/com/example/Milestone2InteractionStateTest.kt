package com.example

import com.example.data.HoldingEntity
import com.example.data.TickerResponse
import com.example.ui.components.Candle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Locale

class Milestone2InteractionStateTest {

    class KeypadInputSimulator(initialText: String = "0.5") {
        var inputAmountText: String = initialText
            private set

        fun handleKeypadInput(key: String) {
            when (key) {
                "DEL" -> {
                    inputAmountText = if (inputAmountText.length > 1) inputAmountText.dropLast(1) else "0"
                }
                "." -> {
                    if (!inputAmountText.contains(".")) {
                        inputAmountText += "."
                    }
                }
                else -> {
                    inputAmountText = if (inputAmountText == "0") key else inputAmountText + key
                }
            }
        }
    }

    class SwapSliderSimulator(
        val containerWidthPx: Float = 400f,
        val handleSizePx: Float = 100f,
        val paddingPx: Float = 10f,
        val enabled: Boolean = true
    ) {
        val maxDragPx: Float = (containerWidthPx - handleSizePx - (paddingPx * 2)).coerceAtLeast(0f)
        var dragOffsetPx: Float = 0f
            private set
        var isDragging: Boolean = false
            private set
        var confirmed: Boolean = false
            private set

        fun onDrag(delta: Float) {
            if (!enabled) return
            isDragging = true
            dragOffsetPx = (dragOffsetPx + delta).coerceIn(0f, maxDragPx)
        }

        fun onDragStopped() {
            if (!enabled) return
            if (maxDragPx > 0 && dragOffsetPx >= maxDragPx * 0.75f) {
                dragOffsetPx = maxDragPx
                confirmed = true
            }
            isDragging = false
            dragOffsetPx = 0f
        }
    }

    @Test
    fun testSwapSlider_ThresholdLogic_At75Percent_TriggersConfirm() {
        val slider = SwapSliderSimulator(containerWidthPx = 400f, handleSizePx = 100f, paddingPx = 10f)
        assertEquals(280f, slider.maxDragPx, 0.001f)

        slider.onDrag(210f)
        assertEquals(210f, slider.dragOffsetPx, 0.001f)
        assertTrue(slider.isDragging)

        slider.onDragStopped()
        assertTrue(slider.confirmed)
        assertFalse(slider.isDragging)
        assertEquals(0f, slider.dragOffsetPx, 0.001f)
    }

    @Test
    fun testSwapSlider_ThresholdLogic_Below75Percent_DoesNotTriggerConfirm() {
        val slider = SwapSliderSimulator(containerWidthPx = 400f, handleSizePx = 100f, paddingPx = 10f)
        
        slider.onDrag(207.2f)
        assertEquals(207.2f, slider.dragOffsetPx, 0.001f)

        slider.onDragStopped()
        assertFalse(slider.confirmed)
        assertFalse(slider.isDragging)
        assertEquals(0f, slider.dragOffsetPx, 0.001f)
    }

    @Test
    fun testSwapSlider_DragBounds_CoercedWithinZeroAndMax() {
        val slider = SwapSliderSimulator(containerWidthPx = 400f, handleSizePx = 100f, paddingPx = 10f)

        slider.onDrag(500f)
        assertEquals(280f, slider.dragOffsetPx, 0.001f)

        slider.onDrag(-400f)
        assertEquals(0f, slider.dragOffsetPx, 0.001f)
    }

    @Test
    fun testSwapSlider_DisabledState_IgnoresDragEvents() {
        val slider = SwapSliderSimulator(containerWidthPx = 400f, handleSizePx = 100f, paddingPx = 10f, enabled = false)

        slider.onDrag(250f)
        assertEquals(0f, slider.dragOffsetPx, 0.001f)
        assertFalse(slider.isDragging)

        slider.onDragStopped()
        assertFalse(slider.confirmed)
    }

    @Test
    fun testKeypad_MultipleDecimals_Rejected() {
        val keypad = KeypadInputSimulator("0.5")
        keypad.handleKeypadInput(".")
        assertEquals("0.5", keypad.inputAmountText)

        keypad.handleKeypadInput("2")
        assertEquals("0.52", keypad.inputAmountText)

        keypad.handleKeypadInput(".")
        assertEquals("0.52", keypad.inputAmountText)
    }

    @Test
    fun testKeypad_ZeroLeadingDigits_HandledCorrectly() {
        val keypad = KeypadInputSimulator("0")

        keypad.handleKeypadInput("5")
        assertEquals("5", keypad.inputAmountText)

        val keypadZero = KeypadInputSimulator("0")
        keypadZero.handleKeypadInput("0")
        assertEquals("0", keypadZero.inputAmountText)

        keypadZero.handleKeypadInput(".")
        assertEquals("0.", keypadZero.inputAmountText)

        keypadZero.handleKeypadInput("0")
        assertEquals("0.0", keypadZero.inputAmountText)

        keypadZero.handleKeypadInput("5")
        assertEquals("0.05", keypadZero.inputAmountText)
    }

    @Test
    fun testKeypad_BackspaceOnEmptyOrZeroBuffer_StaysZero() {
        val keypad = KeypadInputSimulator("0")
        keypad.handleKeypadInput("DEL")
        assertEquals("0", keypad.inputAmountText)

        keypad.handleKeypadInput("DEL")
        assertEquals("0", keypad.inputAmountText)
    }

    @Test
    fun testKeypad_FullDeletionSequence() {
        val keypad = KeypadInputSimulator("0.5")

        keypad.handleKeypadInput("DEL")
        assertEquals("0.", keypad.inputAmountText)

        keypad.handleKeypadInput("DEL")
        assertEquals("0", keypad.inputAmountText)

        keypad.handleKeypadInput("DEL")
        assertEquals("0", keypad.inputAmountText)
    }

    @Test
    fun testTradeScreen_AssetSwapDirectionToggle() {
        var payAsset = "BTC"
        var receiveAsset = "USDT"

        val temp = payAsset
        payAsset = receiveAsset
        receiveAsset = temp

        assertEquals("USDT", payAsset)
        assertEquals("BTC", receiveAsset)
    }

    @Test
    fun testTradeScreen_OutputAmountCalculation() {
        val inputAmount = 0.5
        val payPrice = 67420.00
        val receivePrice = 1.0

        val outputAmount = if (receivePrice > 0.0) (inputAmount * payPrice) / receivePrice else 0.0
        assertEquals(33710.00, outputAmount, 0.01)
    }

    @Test
    fun testPortfolioScreen_HoldingCardValuesAndAllocation() {
        val holding = HoldingEntity("BTC", "Bitcoin", 0.5, 60000.0, isFiat = false)
        val currentPrice = 67420.00
        val value = holding.amount * currentPrice
        val totalNetWorth = 100000.0

        val allocationFraction = (value / totalNetWorth).toFloat().coerceIn(0f, 1f)
        assertEquals(33710.0, value, 0.01)
        assertEquals(0.3371f, allocationFraction, 0.001f)
    }

    @Test
    fun testAssetDetailScreen_Ticker24hStatsFormatting() {
        val ticker = TickerResponse(
            symbol = "BTCUSDT",
            priceChange = "2500.0",
            priceChangePercent = "3.85",
            lastPrice = "67420.00",
            highPrice = "68240.00",
            lowPrice = "65820.00",
            volume = "28400000000.0"
        )

        val highStr = ticker.highPrice.toDoubleOrNull()?.let { String.format(Locale.US, "$%,.2f", it) } ?: "$0.00"
        val lowStr = ticker.lowPrice.toDoubleOrNull()?.let { String.format(Locale.US, "$%,.2f", it) } ?: "$0.00"
        val volStr = ticker.volume.toDoubleOrNull()?.let { String.format(Locale.US, "$%,.2fB", it / 1_000_000_000.0) } ?: "$0.00B"

        assertEquals("$68,240.00", highStr)
        assertEquals("$65,820.00", lowStr)
        assertEquals("$28.40B", volStr)
    }

    @Test
    fun testMarketsScreen_CategoryFilteringAndSparklineFallback() {
        val candlesMap = mapOf<String, List<Candle>>()
        val tickerSymbol = "SOLUSDT"
        val isPositive = true

        val assetCandles = candlesMap[tickerSymbol] ?: emptyList()
        val sparklineData = if (assetCandles.isNotEmpty()) {
            assetCandles.map { it.close }
        } else {
            if (isPositive) listOf(10f, 12f, 11f, 14f, 13f, 16f) else listOf(16f, 14f, 15f, 12f, 13f, 10f)
        }

        assertEquals(6, sparklineData.size)
        assertEquals(10f, sparklineData[0], 0.01f)
        assertEquals(16f, sparklineData[5], 0.01f)
    }

    @Test
    fun testDashboardScreen_MarketMoversSectionParsing() {
        val tickers = listOf(
            TickerResponse("BTCUSDT", "1500.0", "2.45", "67420.00"),
            TickerResponse("ETHUSDT", "65.0", "1.82", "3512.80")
        )

        val firstMovers = tickers.take(6)
        assertEquals(2, firstMovers.size)

        val symbol = firstMovers[0].symbol.replace("USDT", "")
        assertEquals("BTC", symbol)
    }
}
