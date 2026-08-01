package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class OrderBookEntry(
    val price: String,
    val amount: String,
    val depthRatio: Float
)

@Composable
fun OrderBookView(
    symbol: String,
    asks: List<OrderBookEntry> = listOf(
        OrderBookEntry("$67,650.00", "0.842 BTC", 0.6f),
        OrderBookEntry("$67,580.00", "1.230 BTC", 0.8f)
    ),
    bids: List<OrderBookEntry> = listOf(
        OrderBookEntry("$67,420.00", "3.215 BTC", 1.0f),
        OrderBookEntry("$67,380.00", "1.840 BTC", 0.7f)
    ),
    spreadText: String = "Spread $230 (0.34%)",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Order Book",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        asks.forEach { ask ->
            OrderBookRow(
                price = ask.price,
                amount = ask.amount,
                depthRatio = ask.depthRatio,
                priceColor = Color(0xFFEF4444),
                depthColor = Color(0x1AEF4444)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = spreadText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF64748B)
            )
        }

        bids.forEach { bid ->
            OrderBookRow(
                price = bid.price,
                amount = bid.amount,
                depthRatio = bid.depthRatio,
                priceColor = Color(0xFF22C55E),
                depthColor = Color(0x1A22C55E)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun OrderBookRow(
    price: String,
    amount: String,
    depthRatio: Float,
    priceColor: Color,
    depthColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = depthRatio.coerceIn(0f, 1f))
                .align(Alignment.CenterStart)
                .background(depthColor, RoundedCornerShape(4.dp))
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = price,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = priceColor
            )
            Text(
                text = amount,
                fontSize = 13.sp,
                color = Color(0xFF64748B)
            )
        }
    }
}
