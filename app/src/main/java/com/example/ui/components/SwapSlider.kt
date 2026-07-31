package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun SwapSlider(
    text: String = "Slide to Confirm Swap",
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val density = LocalDensity.current
    var containerWidthPx by remember { mutableFloatStateOf(0f) }
    val handleSize = 48.dp
    val handleSizePx = with(density) { handleSize.toPx() }
    val paddingPx = with(density) { 4.dp.toPx() }

    val maxDragPx = (containerWidthPx - handleSizePx - (paddingPx * 2)).coerceAtLeast(0f)
    var dragOffsetPx by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }

    val animatedOffset by animateFloatAsState(
        targetValue = if (isDragging) dragOffsetPx else 0f,
        label = "slider_offset"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(CircleShape)
            .background(if (enabled) Color(0xFF1E60F9) else Color(0xFF94A3B8))
            .onGloballyPositioned { containerWidthPx = it.size.width.toFloat() },
        contentAlignment = Alignment.CenterStart
    ) {
        // Centered Label Text
        Text(
            text = text,
            color = Color.White.copy(
                alpha = (1f - (animatedOffset / maxDragPx.coerceAtLeast(1f))).coerceIn(0.2f, 1f)
            ),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )

        // Draggable Circular Thumb
        Box(
            modifier = Modifier
                .padding(start = 4.dp)
                .offset { IntOffset(animatedOffset.roundToInt(), 0) }
                .size(handleSize)
                .clip(CircleShape)
                .background(Color.White)
                .draggable(
                    orientation = Orientation.Horizontal,
                    enabled = enabled,
                    state = rememberDraggableState { delta ->
                        isDragging = true
                        dragOffsetPx = (dragOffsetPx + delta).coerceIn(0f, maxDragPx)
                    },
                    onDragStopped = {
                        if (maxDragPx > 0 && dragOffsetPx >= maxDragPx * 0.75f) {
                            dragOffsetPx = maxDragPx
                            onConfirm()
                        }
                        isDragging = false
                        dragOffsetPx = 0f
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.DoubleArrow,
                contentDescription = "Slide Arrow",
                tint = Color(0xFF1E60F9),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
