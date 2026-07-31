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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.TickerResponse
import com.example.ui.components.AssetBadge
import com.example.ui.components.Sparkline
import com.example.viewmodel.CryptoState
import com.example.viewmodel.CryptoViewModel
import com.example.viewmodel.CryptoViewModelFactory
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
public fun DashboardScreen(
  animatedVisibilityScope: AnimatedVisibilityScope,
  sharedTransitionScope: SharedTransitionScope,
  onNavigateToAsset: (String) -> Unit,
  onNavigateToTrade: () -> Unit = {},
  modifier: Modifier = Modifier,
  viewModel: CryptoViewModel = viewModel(factory = CryptoViewModelFactory(LocalContext.current))
): Unit {
  val state by viewModel.state.collectAsState()
  val uiState by viewModel.uiState.collectAsState()
  val candles by viewModel.candles.collectAsState()

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = Color(0xFFFAFAFA),
    topBar = {
      DashboardTopHeader(userName = "Alex Rivera")
    }
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      contentPadding = PaddingValues(bottom = 32.dp)
    ) {
      item {
        PortfolioGradientCard(
          totalNetWorth = uiState.totalNetWorth,
          dailyPnl = uiState.dailyPnl,
          onDeposit = {},
          onWithdraw = {},
          onSwap = onNavigateToTrade
        )
      }
      item {
        MarketMoversSection(
          movers = uiState.topMovers,
          onNavigateToAsset = onNavigateToAsset
        )
      }
      item {
        WatchlistSection(
          animatedVisibilityScope = animatedVisibilityScope,
          sharedTransitionScope = sharedTransitionScope,
          onNavigateToAsset = onNavigateToAsset,
          state = state,
          candles = candles
        )
      }
    }
  }
}

@Composable
fun DashboardTopHeader(userName: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 16.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column {
      Text(
        text = "Good morning,",
        fontSize = 14.sp,
        color = Color(0xFF64748B)
      )
      Text(
        text = userName,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0F172A)
      )
    }
    Row(
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Bell notification icon pill
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(Color(0xFFF0F3FA)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Notifications,
          contentDescription = "Notifications",
          tint = Color(0xFF0F172A),
          modifier = Modifier.size(20.dp)
        )
      }
      // User Profile Initials Avatar
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(Color(0xFF2563EB)),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "AR",
          color = Color.White,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}

@Composable
fun PortfolioGradientCard(
  totalNetWorth: Double,
  dailyPnl: Double,
  onDeposit: () -> Unit,
  onWithdraw: () -> Unit,
  onSwap: () -> Unit
) {
  val formattedNetWorth = if (totalNetWorth > 0) String.format(Locale.US, "$%,.2f", totalNetWorth) else "$142,836.50"
  val isPositive = dailyPnl >= 0
  val pnlPctStr = if (totalNetWorth > 0) String.format(Locale.US, "%+.2f%%", (dailyPnl / totalNetWorth) * 100) else "+2.45%"

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 8.dp)
      .clip(RoundedCornerShape(24.dp))
      .background(
        Brush.linearGradient(
          colors = listOf(Color(0xFF1E60F9), Color(0xFF1D4ED8))
        )
      )
      .padding(24.dp)
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Total Portfolio Value",
          color = Color.White.copy(alpha = 0.85f),
          fontSize = 13.sp,
          fontWeight = FontWeight.Medium
        )
        Row(
          modifier = Modifier
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.2f))
            .padding(horizontal = 10.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.TrendingUp,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = pnlPctStr,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = formattedNetWorth,
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Action Pills Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        QuickActionButton(
          text = "Deposit",
          icon = Icons.Default.ArrowDownward,
          onClick = onDeposit,
          modifier = Modifier.weight(1f)
        )
        QuickActionButton(
          text = "Withdraw",
          icon = Icons.Default.ArrowUpward,
          onClick = onWithdraw,
          modifier = Modifier.weight(1f)
        )
        QuickActionButton(
          text = "Swap",
          icon = Icons.Default.SwapHoriz,
          onClick = onSwap,
          modifier = Modifier.weight(1f)
        )
      }
    }
  }
}

@Composable
fun QuickActionButton(
  text: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .height(44.dp)
      .clip(CircleShape)
      .background(Color.White)
      .clickable { onClick() },
    contentAlignment = Alignment.Center
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = text,
        tint = Color(0xFF0F172A),
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = text,
        color = Color(0xFF0F172A),
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold
      )
    }
  }
}

@Composable
fun MarketMoversSection(
  movers: List<TickerResponse>,
  onNavigateToAsset: (String) -> Unit
) {
  Column(modifier = Modifier.padding(top = 20.dp, bottom = 8.dp)) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Market Movers",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0F172A)
      )
      Text(
        text = "See All",
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF2563EB),
        modifier = Modifier.clickable { }
      )
    }

    val displayList = if (movers.isNotEmpty()) movers.take(6) else listOf(
      TickerResponse("BTCUSDT", "1500.0", "2.45", "67420.00"),
      TickerResponse("ETHUSDT", "65.0", "1.82", "3512.80"),
      TickerResponse("SOLUSDT", "9.2", "5.31", "184.20"),
      TickerResponse("BNBUSDT", "-5.3", "-0.91", "578.40")
    )

    LazyRow(
      contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      items(displayList) { ticker ->
        val symbol = ticker.symbol.replace("USDT", "")
        val pct = ticker.priceChangePercent.toDoubleOrNull() ?: 0.0
        val isPositive = pct >= 0
        val pctStr = String.format(Locale.US, "%+.2f%%", pct)
        val priceStr = ticker.lastPrice.toDoubleOrNull()?.let { String.format(Locale.US, "$%,.2f", it) } ?: "$${ticker.lastPrice}"

        Box(
          modifier = Modifier
            .width(130.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
            .clickable { onNavigateToAsset(symbol) }
            .padding(14.dp)
        ) {
          Column {
            Text(
              text = symbol,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = priceStr,
              fontSize = 14.sp,
              fontWeight = FontWeight.Medium,
              color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = pctStr,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = if (isPositive) Color(0xFF22C55E) else Color(0xFFEF4444)
            )
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun WatchlistSection(
  animatedVisibilityScope: AnimatedVisibilityScope,
  sharedTransitionScope: SharedTransitionScope,
  onNavigateToAsset: (String) -> Unit,
  state: CryptoState,
  candles: Map<String, List<com.example.ui.components.Candle>>
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 12.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "My Watchlist",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0F172A)
      )
      Text(
        text = "Edit",
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF2563EB),
        modifier = Modifier.clickable { }
      )
    }

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(24.dp))
        .background(Color.White)
        .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(24.dp))
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
      Column {
        val displayTickers = if (state.tickers.isNotEmpty()) state.tickers.take(5) else listOf(
          TickerResponse("BTCUSDT", "1500.0", "2.45", "67420.00"),
          TickerResponse("ETHUSDT", "65.0", "1.82", "3512.80"),
          TickerResponse("SOLUSDT", "9.2", "5.31", "184.20"),
          TickerResponse("BNBUSDT", "-5.3", "-0.91", "578.40")
        )

        displayTickers.forEachIndexed { index, ticker ->
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

          WatchlistItemRow(
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

          if (index < displayTickers.size - 1) {
            HorizontalDivider(
              color = Color(0xFFF1F5F9),
              thickness = 1.dp,
              modifier = Modifier.padding(horizontal = 8.dp)
            )
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun WatchlistItemRow(
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
      .padding(vertical = 12.dp, horizontal = 4.dp),
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
          text = "$symbol · Mkt Cap",
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
