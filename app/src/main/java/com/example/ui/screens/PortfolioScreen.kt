package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AssetBadge
import com.example.ui.components.DonutChart
import com.example.ui.components.DonutSegment
import com.example.viewmodel.CryptoViewModel
import com.example.viewmodel.CryptoViewModelFactory
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun PortfolioScreen(
  onNavigateToHistory: () -> Unit = {},
  onNavigateToAsset: (String) -> Unit = {},
  modifier: Modifier = Modifier,
  viewModel: CryptoViewModel = viewModel(factory = CryptoViewModelFactory(LocalContext.current))
): Unit {
  val uiState by viewModel.uiState.collectAsState()
  var selectedTimeframe by remember { mutableStateOf("1M") }

  val totalNetWorth = uiState.totalNetWorth
  val dailyPnl = uiState.dailyPnl

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = Color(0xFFFAFAFA),
    topBar = {
      TopAppBar(
        title = {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(end = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Analytics",
              fontSize = 28.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0F172A)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              listOf("1W", "1M", "3M", "ALL").forEach { tf ->
                val isSelected = tf == selectedTimeframe
                Box(
                  modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isSelected) Color(0xFF1E60F9) else Color(0xFFF0F3FA))
                    .clickable { selectedTimeframe = tf }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = tf,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) Color.White else Color(0xFF64748B)
                  )
                }
              }
            }
          }
        },
        actions = {
          IconButton(onClick = onNavigateToHistory) {
            Icon(
              imageVector = Icons.Default.History,
              contentDescription = "History",
              tint = Color(0xFF0F172A)
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.Transparent
        )
      )
    }
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 32.dp)
    ) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(20.dp))
            .padding(20.dp)
        ) {
          Column {
            Text(
              text = "Asset Allocation",
              fontSize = 16.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(16.dp))

            val donutSegments = listOf(
              DonutSegment("BTC", 45f, "$64,276", Color(0xFF1E60F9)),
              DonutSegment("ETH", 25f, "$35,709", Color(0xFF10B981)),
              DonutSegment("SOL", 15f, "$21,425", Color(0xFFF59E0B)),
              DonutSegment("Others", 15f, "$21,425", Color(0xFF8B5CF6))
            )

            val totalStr = if (totalNetWorth > 0) "$${(totalNetWorth / 1000).toInt()}K" else "$142K"

            DonutChart(
              segments = donutSegments,
              centerValue = totalStr,
              centerLabel = "Portfolio"
            )
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          val pnlPct = if (totalNetWorth > 0) (dailyPnl / totalNetWorth) * 100 else 2.33
          val formattedNetWorthStr = if (totalNetWorth > 0) String.format(Locale.US, "$%,.0f", totalNetWorth) else "$142,836"
          val formattedDailyPnlStr = "${if (dailyPnl >= 0) "+" else ""}${String.format(Locale.US, "$%,.0f", dailyPnl)}"

          Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
              title = "Total Value",
              value = formattedNetWorthStr,
              subtitle = "+$3,248 today",
              isPositive = true,
              modifier = Modifier.weight(1f)
            )
            StatCard(
              title = "24h P&L",
              value = formattedDailyPnlStr,
              subtitle = String.format(Locale.US, "%+.2f%%", pnlPct),
              isPositive = dailyPnl >= 0,
              modifier = Modifier.weight(1f)
            )
          }
          Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
              title = "All-Time P&L",
              value = "+$42,836",
              subtitle = "+42.8%",
              isPositive = true,
              modifier = Modifier.weight(1f)
            )
            StatCard(
              title = "Avg Buy Price",
              value = "$58,240",
              subtitle = "BTC",
              isPositive = true,
              modifier = Modifier.weight(1f)
            )
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
          text = "Holdings",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF0F172A),
          modifier = Modifier.padding(bottom = 12.dp)
        )
      }

      if (uiState.holdings.isEmpty()) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(16.dp))
              .background(Color.White)
              .padding(24.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(text = "No holdings yet", color = Color(0xFF64748B))
          }
        }
      } else {
        items(uiState.holdings.size) { index ->
          val holding = uiState.holdings[index]
          val ticker = uiState.tickers.find {
            it.symbol == "${holding.assetSymbol}USDT" || it.symbol == holding.assetSymbol
          }
          val currentPrice = if (holding.isFiat) 1.0 else (ticker?.lastPrice?.toDoubleOrNull() ?: holding.avgBuyPriceUsd)
          val value = holding.amount * currentPrice
          val changePct = ticker?.priceChangePercent?.toDoubleOrNull() ?: 2.45
          val isPositive = changePct >= 0
          val valueStr = String.format(Locale.US, "$%,.0f", value)
          val amountStr = "${String.format(Locale.US, "%.3f", holding.amount)} ${holding.assetSymbol}"
          val roiValue = (currentPrice - holding.avgBuyPriceUsd) * holding.amount
          val pnlStr = "${if (roiValue >= 0) "+" else ""}${String.format(Locale.US, "$%,.0f", roiValue)}"

          val allocationFraction = if (totalNetWorth > 0) (value / totalNetWorth).toFloat().coerceIn(0f, 1f) else 0.45f

          HoldingCard(
            symbol = holding.assetSymbol,
            name = holding.name,
            amount = amountStr,
            value = valueStr,
            pnl = pnlStr,
            allocationFraction = allocationFraction,
            isPositive = isPositive,
            onClick = { onNavigateToAsset(holding.assetSymbol) }
          )
          Spacer(modifier = Modifier.height(12.dp))
        }
      }
    }
  }
}

@Composable
fun StatCard(
  title: String,
  value: String,
  subtitle: String,
  isPositive: Boolean,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(16.dp))
      .background(Color.White)
      .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
      .padding(16.dp)
  ) {
    Column {
      Text(
        text = title,
        fontSize = 12.sp,
        color = Color(0xFF64748B)
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = value,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0F172A)
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = subtitle,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = if (isPositive) Color(0xFF10B981) else Color(0xFFEF4444)
      )
    }
  }
}

@Composable
fun HoldingCard(
  symbol: String,
  name: String,
  amount: String,
  value: String,
  pnl: String,
  allocationFraction: Float,
  isPositive: Boolean,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(Color.White)
      .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
      .clickable { onClick() }
      .padding(16.dp)
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          AssetBadge(symbol = symbol)
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = name,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0F172A)
            )
            Text(
              text = amount,
              fontSize = 12.sp,
              color = Color(0xFF64748B)
            )
          }
        }
        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
          )
          Text(
            text = pnl,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isPositive) Color(0xFF10B981) else Color(0xFFEF4444)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      LinearProgressIndicator(
        progress = { allocationFraction },
        modifier = Modifier
          .fillMaxWidth()
          .height(6.dp)
          .clip(CircleShape),
        color = Color(0xFF1E60F9),
        trackColor = Color(0xFFF0F3FA)
      )
    }
  }
}
