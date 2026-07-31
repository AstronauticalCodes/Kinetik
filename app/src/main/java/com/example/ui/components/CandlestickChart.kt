package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import kotlin.math.max
import kotlin.math.min

public data class Candle(
    val open: Float,
    val close: Float,
    val high: Float,
    val low: Float
)

@Composable
public fun CandlestickChart(
    data: List<Candle>,
    modifier: Modifier = Modifier,
    bullishColor: Color = Color(0xFF22C55E),
    bearishColor: Color = Color(0xFFEF4444),
    gridColor: Color = Color(0xFFE2E8F0)
): Unit {
    Canvas(modifier = modifier.fillMaxSize(fraction = 1.0f)) {
        if (data.isEmpty()) return@Canvas

        // Draw horizontal grid lines
        val gridLines = 4
        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        for (i in 0..gridLines) {
            val y = size.height * (i / gridLines.toFloat())
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1f,
                pathEffect = dashEffect
            )
        }

        val maxPrice = data.maxOf { it.high }
        val minPrice = data.minOf { it.low }
        val range = (maxPrice - minPrice).let { if (it == 0f) 1f else it }
        val candleWidth = size.width / data.size
        val spacing = candleWidth * 0.25f
        val bodyWidth = (candleWidth - spacing).coerceAtLeast(2f)

        data.forEachIndexed { index, candle ->
            val x = index * candleWidth + candleWidth / 2
            val isBullish = candle.close >= candle.open
            val color = if (isBullish) bullishColor else bearishColor

            val highY = size.height - ((candle.high - minPrice) / range * size.height)
            val lowY = size.height - ((candle.low - minPrice) / range * size.height)
            val openY = size.height - ((candle.open - minPrice) / range * size.height)
            val closeY = size.height - ((candle.close - minPrice) / range * size.height)

            // Draw wick
            drawLine(
                color = color,
                start = Offset(x, highY),
                end = Offset(x, lowY),
                strokeWidth = 2f,
                cap = StrokeCap.Round
            )

            // Draw candle body
            val topY = min(openY, closeY)
            val bottomY = max(openY, closeY)
            val bodyHeight = max(bottomY - topY, 3f)

            drawLine(
                color = color,
                start = Offset(x, topY),
                end = Offset(x, topY + bodyHeight),
                strokeWidth = bodyWidth,
                cap = StrokeCap.Square
            )
        }
    }
}
