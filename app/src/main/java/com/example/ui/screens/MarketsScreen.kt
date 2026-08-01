package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.TickerResponse
import com.example.ui.components.AssetBadge
import com.example.ui.components.FilterChips
import com.example.ui.components.Sparkline
import com.example.viewmodel.CryptoViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
public fun MarketsScreen(
  animatedVisibilityScope: AnimatedVisibilityScope,
  sharedTransitionScope: SharedTransitionScope,
  onNavigateToAsset: (String) -> Unit,
  onNavigateToSearch: () -> Unit = {},
  modifier: Modifier = Modifier,
  viewModel: CryptoViewModel = viewModel()
): Unit {
  val state by viewModel.state.collectAsState()
  val candles by viewModel.candles.collectAsState()
  var selectedCategory by remember { mutableStateOf("All") }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = Color(0xFFFAFAFA),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Markets",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
          )
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.Transparent
        )
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 4.dp)
          .clip(CircleShape)
          .background(Color(0xFFF1F5F9))
          .clickable { onNavigateToSearch() }
          .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.CenterStart
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = Color(0xFF94A3B8)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Search coins, tokens...",
            color = Color(0xFF94A3B8),
            fontSize = 14.sp
          )
        }
      }

      FilterChips(
        categories = listOf("All", "DeFi", "Layer 1", "Layer 2", "NFT"),
        selectedCategory = selectedCategory,
        onCategorySelected = { selectedCategory = it }
      )

      MarketSummaryStatsCard(
        mktCap = "$2.48T",
        vol24h = "$94.2B",
        btcDom = "53.4%"
      )

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 12.dp)
          .clip(RoundedCornerShape(20.dp))
          .background(Color.White)
          .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(20.dp))
      ) {
        Column {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "Name",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFF64748B)
            )
            Text(
              text = "Price / 24h",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFF64748B)
            )
          }

          HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)

          val displayList = if (state.tickers.isNotEmpty()) state.tickers else listOf(
            TickerResponse("BTCUSDT", "1500.0", "2.45", "67420.00"),
            TickerResponse("ETHUSDT", "65.0", "1.82", "3512.80"),
            TickerResponse("SOLUSDT", "9.2", "5.31", "184.20"),
            TickerResponse("BNBUSDT", "-5.3", "-0.91", "578.40")
          )

          LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 16.dp)
          ) {
            items(displayList.size) { index ->
              val ticker = displayList[index]
              val symbol = ticker.symbol.replace("USDT", "")
              val name = when (symbol) {
                "BTC" -> "Bitcoin"
                "ETH" -> "Ethereum"
                "SOL" -> "Solana"
                "BNB" -> "BNB"
                else -> symbol
              }
              val priceVal = ticker.lastPrice.toFloatOrNull() ?: 0f
              val priceStr = String.format(Locale.US, "$%,.2f", priceVal)
              val changePct = ticker.priceChangePercent.toFloatOrNull() ?: 0f
              val isPositive = changePct >= 0
              val changeStr = "${if (isPositive) "+" else ""}${String.format(Locale.US, "%.2f", changePct)}%"

              val assetCandles = candles[ticker.symbol] ?: emptyList()
              val sparklineData = if (assetCandles.isNotEmpty()) {
                assetCandles.map { it.close }
              } else {
                if (isPositive) listOf(10f, 12f, 11f, 14f, 13f, 16f) else listOf(16f, 14f, 15f, 12f, 13f, 10f)
              }

              MarketAssetRow(
                symbol = symbol,
                name = name,
                price = priceStr,
                change = changeStr,
                isPositive = isPositive,
                sparklineData = sparklineData,
                animatedVisibilityScope = animatedVisibilityScope,
                sharedTransitionScope = sharedTransitionScope,
                onClick = { onNavigateToAsset(symbol) }
              )

              if (index < displayList.size - 1) {
                HorizontalDivider(
                  color = Color(0xFFF1F5F9),
                  thickness = 1.dp,
                  modifier = Modifier.padding(horizontal = 16.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun MarketSummaryStatsCard(
  mktCap: String,
  vol24h: String,
  btcDom: String
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 8.dp)
      .clip(RoundedCornerShape(16.dp))
      .background(Color.White)
      .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
      .padding(16.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      StatColumn(label = "Mkt Cap", value = mktCap, modifier = Modifier.weight(1f))
      Box(modifier = Modifier.width(1.dp).height(32.dp).background(Color(0xFFE2E8F0)))
      StatColumn(label = "24h Vol", value = vol24h, modifier = Modifier.weight(1f))
      Box(modifier = Modifier.width(1.dp).height(32.dp).background(Color(0xFFE2E8F0)))
      StatColumn(label = "BTC Dom", value = btcDom, modifier = Modifier.weight(1f))
    }
  }
}

@Composable
private fun StatColumn(
  label: String,
  value: String,
  modifier: Modifier = Modifier
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier
  ) {
    Text(
      text = label,
      fontSize = 12.sp,
      color = Color(0xFF64748B)
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = value,
      fontSize = 15.sp,
      fontWeight = FontWeight.Bold,
      color = Color(0xFF0F172A)
    )
  }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MarketAssetRow(
  symbol: String,
  name: String,
  price: String,
  change: String,
  isPositive: Boolean,
  sparklineData: List<Float>,
  animatedVisibilityScope: AnimatedVisibilityScope,
  sharedTransitionScope: SharedTransitionScope,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(vertical = 12.dp, horizontal = 16.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.weight(1.2f)
    ) {
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
          text = "$symbol · $1.32T",
          fontSize = 12.sp,
          color = Color(0xFF64748B)
        )
      }
    }

    Box(
      modifier = Modifier
        .weight(0.8f)
        .height(32.dp)
        .padding(horizontal = 8.dp)
    ) {
      Sparkline(
        data = sparklineData,
        color = if (isPositive) Color(0xFF22C55E) else Color(0xFFEF4444),
        strokeWidth = 3f
      )
    }

    Column(
      horizontalAlignment = Alignment.End,
      modifier = Modifier.weight(1f)
    ) {
      Text(
        text = price,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0F172A)
      )
      Text(
        text = change,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = if (isPositive) Color(0xFF22C55E) else Color(0xFFEF4444)
      )
    }
  }
}
