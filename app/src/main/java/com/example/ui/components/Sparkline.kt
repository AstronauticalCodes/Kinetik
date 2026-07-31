package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
public fun Sparkline(
  data: List<Float>,
  color: Color,
  modifier: Modifier = Modifier,
  strokeWidth: Float = 4f
): Unit {
  Canvas(modifier = modifier.fillMaxSize(fraction = 1.0f)) {
    if (data.isEmpty()) return@Canvas

    val max = data.maxOrNull() ?: 1f
    val min = data.minOrNull() ?: 0f
    val range = max - min
    val stepX = size.width / (data.size - 1).coerceAtLeast(1)

    val path = Path()
    data.forEachIndexed { index, value ->
      val x = index * stepX
      
      val y = if (range == 0f) size.height / 2 else size.height - ((value - min) / range * size.height)
      
      if (index == 0) {
        path.moveTo(x, y)
      } else {
        path.lineTo(x, y)
      }
    }

    drawPath(
      path = path,
      color = color,
      style = Stroke(
        width = strokeWidth,
        cap = StrokeCap.Round,
        join = StrokeJoin.Round
      )
    )
  }
}
