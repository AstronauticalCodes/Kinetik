package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
  bullishColor: Color = Color(0xFF66C285), 
  bearishColor: Color = Color(0xFFFFB4AB)  
): Unit {
  Canvas(modifier = modifier.fillMaxSize(fraction = 1.0f)) {
    if (data.isEmpty()) return@Canvas

    val maxPrice = data.maxOf { it.high }
    val minPrice = data.minOf { it.low }
    val range = maxPrice - minPrice
    val candleWidth = size.width / data.size
    val spacing = candleWidth * 0.2f
    val bodyWidth = candleWidth - spacing

    data.forEachIndexed { index, candle ->
      val x = index * candleWidth + candleWidth / 2
      val isBullish = candle.close > candle.open
      val color = if (isBullish) bullishColor else bearishColor

      val highY = size.height - ((candle.high - minPrice) / range * size.height)
      val lowY = size.height - ((candle.low - minPrice) / range * size.height)
      val openY = size.height - ((candle.open - minPrice) / range * size.height)
      val closeY = size.height - ((candle.close - minPrice) / range * size.height)

      
      drawLine(
        color = color,
        start = Offset(x, highY.toFloat()),
        end = Offset(x, lowY.toFloat()),
        strokeWidth = 2f,
        cap = StrokeCap.Round
      )

      
      val topY = min(openY, closeY)
      val bottomY = max(openY, closeY)
      val bodyHeight = max(bottomY - topY, 2f) 

      drawLine(
        color = color,
        start = Offset(x, topY.toFloat()),
        end = Offset(x, bottomY.toFloat()),
        strokeWidth = bodyWidth,
        cap = StrokeCap.Square
      )
    }
  }
}
