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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.viewmodel.CryptoViewModel
import com.example.ui.theme.DataLarge
import com.example.ui.theme.DataMedium
import com.example.ui.theme.md_theme_dark_onSurface
import com.example.ui.theme.md_theme_dark_onSurfaceVariant
import com.example.ui.theme.md_theme_dark_primaryContainer
import com.example.ui.theme.primary_fixed_dim
import com.example.ui.theme.secondary_fixed_dim
import com.example.ui.theme.surface_container_high
import com.example.ui.theme.surface_container_highest
import com.example.ui.theme.surface_container_low

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
public fun DashboardScreen(
  animatedVisibilityScope: AnimatedVisibilityScope,
  sharedTransitionScope: SharedTransitionScope,
  onNavigateToAsset: (String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: CryptoViewModel = viewModel(factory = com.example.viewmodel.CryptoViewModelFactory(androidx.compose.ui.platform.LocalContext.current))
): Unit {
  val state by viewModel.state.collectAsState()
  val uiState by viewModel.uiState.collectAsState()
  val candles by viewModel.candles.collectAsState()

  with(sharedTransitionScope) {
    Scaffold(
      modifier = modifier.fillMaxSize(fraction = 1.0f),
      topBar = {
        TopAppBar(
          title = {
            Column {
              Text(
                text = "NETWORK: MAINNET",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, letterSpacing = 2.sp, fontWeight = FontWeight.Bold),
                color = primary_fixed_dim.copy(alpha = 0.8f)
              )
              Text(
                text = "Dashboard",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
            }
          },
          actions = {
            Box(
              modifier = Modifier
                .padding(end = 16.dp)
                .size(size = 48.dp)
                .background(color = Color.White.copy(alpha = 0.05f), shape = CircleShape)
                .border(width = 1.dp, color = Color.White.copy(alpha = 0.1f), shape = CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Box(
                modifier = Modifier
                  .size(size = 32.dp)
                  .background(color = primary_fixed_dim.copy(alpha = 0.8f), shape = CircleShape)
              )
            }
          },
          colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
          )
        )
      },
      containerColor = Color.Transparent
    ) { innerPadding ->
      LazyColumn(
        modifier = Modifier
          .fillMaxSize(fraction = 1.0f)
          .padding(paddingValues = innerPadding),
        contentPadding = PaddingValues(bottom = 24.dp)
      ) {
        item {
          TickerTapeSection(state.tickers)
        }
        item {
          Column(
            modifier = Modifier
              .fillMaxWidth(fraction = 1.0f)
              .padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 12.dp)
          ) {
            PortfolioSummarySection(
              totalNetWorth = uiState.totalNetWorth,
              dailyPnl = uiState.dailyPnl
            )
            Row(
              modifier = Modifier.fillMaxWidth(fraction = 1.0f),
              horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
            ) {
              RsiIndexCard(modifier = Modifier.weight(weight = 1.0f))
              YieldAprCard(modifier = Modifier.weight(weight = 1.0f))
            }
            WatchlistSection(
              animatedVisibilityScope = animatedVisibilityScope,
              sharedTransitionScope = this@with,
              onNavigateToAsset = onNavigateToAsset,
              state = state,
              candles = candles
            )
            TopMoversSection(
              movers = uiState.topMovers,
              onNavigateToAsset = onNavigateToAsset
            )
          }
        }
      }
    }
  }
}


@Composable
public fun PortfolioSummarySection(
  totalNetWorth: Double = 0.0,
  dailyPnl: Double = 0.0
): Unit {
  val formattedNetWorth = if (totalNetWorth > 0) String.format(java.util.Locale.US, "$%,.2f", totalNetWorth) else "$0.00"
  val isPositivePnl = dailyPnl >= 0
  val formattedPnlStr = "${if (isPositivePnl) "+" else ""}${String.format(java.util.Locale.US, "$%,.2f", dailyPnl)} TODAY"
  val pnlPctStr = if (totalNetWorth > 0) String.format(java.util.Locale.US, "%+.2f%%", (dailyPnl / totalNetWorth) * 100) else "+0.00%"

  Column(
    modifier = Modifier
      .fillMaxWidth(fraction = 1.0f)
      .padding(horizontal = 16.dp)
      .padding(bottom = 24.dp)
  ) {
    Text(
      text = "TOTAL PORTFOLIO VALUE",
      style = MaterialTheme.typography.labelSmall,
      color = md_theme_dark_onSurfaceVariant,
      letterSpacing = 1.sp
    )
    Spacer(modifier = Modifier.height(height = 4.dp))
    Text(
      text = formattedNetWorth,
      style = DataLarge.copy(fontSize = 40.sp),
      color = md_theme_dark_primaryContainer
    )
    Spacer(modifier = Modifier.height(height = 8.dp))
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        modifier = Modifier
          .background(color = (if (isPositivePnl) Color(0xFF007117) else Color(0xFFB3261E)).copy(alpha = 0.1f), shape = CircleShape)
          .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Filled.TrendingUp,
          contentDescription = "Up",
          tint = if (isPositivePnl) secondary_fixed_dim else Color(0xFFFFB4AB),
          modifier = Modifier.size(size = 16.dp)
        )
        Spacer(modifier = Modifier.width(width = 4.dp))
        Text(
          text = pnlPctStr,
          style = DataMedium,
          color = if (isPositivePnl) secondary_fixed_dim else Color(0xFFFFB4AB)
        )
      }
      Spacer(modifier = Modifier.width(width = 8.dp))
      Text(
        text = formattedPnlStr,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
        color = md_theme_dark_onSurfaceVariant
      )
    }
  }
}

@Composable
public fun TickerTapeSection(tickers: List<com.example.data.TickerResponse>): Unit {
  LazyRow(
    modifier = Modifier
      .fillMaxWidth(fraction = 1.0f)
      .background(color = surface_container_low.copy(alpha = 0.3f))
      .border(width = 1.dp, color = md_theme_dark_onSurfaceVariant.copy(alpha = 0.05f))
      .padding(vertical = 8.dp),
    contentPadding = PaddingValues(horizontal = 16.dp),
    horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
  ) {
    if (tickers.isEmpty()) {
      items(count = 5) {
        Text("Loading...", color = Color.White.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = 16.dp))
      }
    } else {
      items(count = tickers.size) { index ->
        val ticker = tickers[index]
        val isPositive = ticker.priceChangePercent.toFloatOrNull()?.let { it >= 0 } ?: true
        Row(
          modifier = Modifier
            .background(color = surface_container_highest.copy(alpha = 0.4f), shape = RoundedCornerShape(size = 8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "${index + 1}", style = MaterialTheme.typography.labelSmall, color = primary_fixed_dim)
          Spacer(modifier = Modifier.width(width = 8.dp))
          Text(text = ticker.symbol.replace("USDT", ""), style = DataMedium, color = md_theme_dark_onSurface)
          Spacer(modifier = Modifier.width(width = 8.dp))
          Text(
            text = "$${ticker.lastPrice.toFloatOrNull()?.let { String.format(java.util.Locale.US, "%.2f", it) } ?: ticker.lastPrice}", 
            style = DataMedium, 
            color = if (!isPositive) Color(color = 0xFFFFB4AB) else secondary_fixed_dim
          )
        }
      }
    }
  }
}

@Composable
public fun TopMoversSection(
  movers: List<com.example.data.TickerResponse> = emptyList(),
  onNavigateToAsset: (String) -> Unit = {}
): Unit {
  Column(
    modifier = Modifier
      .fillMaxWidth(fraction = 1.0f)
      .padding(horizontal = 16.dp)
      .padding(top = 24.dp, bottom = 32.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(fraction = 1.0f),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Top Movers",
        style = MaterialTheme.typography.headlineMedium,
        color = md_theme_dark_onSurface
      )
      Icon(
        imageVector = Icons.Filled.Bolt,
        contentDescription = "Movers",
        tint = primary_fixed_dim
      )
    }
    Spacer(modifier = Modifier.height(height = 16.dp))

    val displayList = movers.take(4)
    if (displayList.isEmpty()) {
      Row(
        modifier = Modifier.fillMaxWidth(fraction = 1.0f),
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
      ) {
        MoverCard(
          modifier = Modifier.weight(weight = 1.0f),
          symbol = "BTC",
          change = "+2.5%",
          price = "$65,000.00"
        )
        MoverCard(
          modifier = Modifier.weight(weight = 1.0f),
          symbol = "ETH",
          change = "+12.8%",
          price = "$3,500.00"
        )
      }
    } else {
      val rows = displayList.chunked(2)
      rows.forEach { rowItems ->
        Row(
          modifier = Modifier.fillMaxWidth(fraction = 1.0f),
          horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
        ) {
          rowItems.forEach { ticker ->
            val symbol = ticker.symbol.replace("USDT", "")
            val pct = ticker.priceChangePercent.toDoubleOrNull() ?: 0.0
            val pctStr = String.format(java.util.Locale.US, "%+.2f%%", pct)
            val price = ticker.lastPrice.toDoubleOrNull()?.let { String.format(java.util.Locale.US, "$%,.2f", it) } ?: "$${ticker.lastPrice}"
            val isPositive = pct >= 0

            MoverCard(
              modifier = Modifier.weight(weight = 1.0f).clickable { onNavigateToAsset(symbol) },
              symbol = symbol,
              change = pctStr,
              price = price,
              isPositive = isPositive
            )
          }
          if (rowItems.size == 1) {
            Spacer(modifier = Modifier.weight(weight = 1.0f))
          }
        }
        Spacer(modifier = Modifier.height(height = 12.dp))
      }
    }
  }
}

@Composable
public fun MoverCard(
  modifier: Modifier = Modifier,
  symbol: String,
  change: String,
  price: String,
  isPositive: Boolean = true
): Unit {
  Column(
    modifier = modifier
      .background(color = surface_container_high.copy(alpha = 0.4f), shape = RoundedCornerShape(size = 12.dp))
      .border(width = 1.dp, color = Color.White.copy(alpha = 0.05f), shape = RoundedCornerShape(size = 12.dp))
      .padding(all = 16.dp)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(size = 32.dp)
          .clip(shape = CircleShape)
          .background(color = primary_fixed_dim.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Filled.CurrencyBitcoin,
          contentDescription = null,
          tint = primary_fixed_dim,
          modifier = Modifier.size(size = 18.dp)
        )
      }
      Spacer(modifier = Modifier.width(width = 8.dp))
      Text(
        text = symbol,
        style = MaterialTheme.typography.labelSmall,
        color = md_theme_dark_onSurface
      )
    }
    Spacer(modifier = Modifier.height(height = 8.dp))
    Text(
      text = change,
      style = DataLarge,
      color = if (isPositive) secondary_fixed_dim else Color(0xFFFFB4AB)
    )
    Spacer(modifier = Modifier.height(height = 4.dp))
    Text(
      text = price,
      style = DataMedium,
      color = md_theme_dark_onSurfaceVariant
    )
  }
}

@Composable
public fun RsiIndexCard(modifier: Modifier = Modifier): Unit {
  Column(
    modifier = modifier
      .background(color = Color.White.copy(alpha = 0.03f), shape = RoundedCornerShape(size = 16.dp))
      .border(width = 1.dp, color = Color.White.copy(alpha = 0.05f), shape = RoundedCornerShape(size = 16.dp))
      .padding(all = 16.dp)
  ) {
    Text(
      text = "RSI INDEX",
      style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, letterSpacing = 2.sp),
      color = Color.White.copy(alpha = 0.4f)
    )
    Spacer(modifier = Modifier.height(height = 16.dp))
    Row(
      modifier = Modifier.fillMaxWidth(fraction = 1.0f),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.Bottom
    ) {
      Text(text = "64.2", style = DataLarge.copy(fontSize = 24.sp), color = Color.White)
      Box(modifier = Modifier.width(width = 4.dp).height(height = 32.dp).background(color = secondary_fixed_dim, shape = CircleShape))
    }
  }
}

@Composable
public fun YieldAprCard(modifier: Modifier = Modifier): Unit {
  Column(
    modifier = modifier
      .background(color = Color.White.copy(alpha = 0.03f), shape = RoundedCornerShape(size = 16.dp))
      .border(width = 1.dp, color = Color.White.copy(alpha = 0.05f), shape = RoundedCornerShape(size = 16.dp))
      .padding(all = 16.dp)
  ) {
    Text(
      text = "YIELD APR",
      style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, letterSpacing = 2.sp),
      color = Color.White.copy(alpha = 0.4f)
    )
    Spacer(modifier = Modifier.height(height = 16.dp))
    Row(
      modifier = Modifier.fillMaxWidth(fraction = 1.0f),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.Bottom
    ) {
      Text(text = "8.4%", style = DataLarge.copy(fontSize = 24.sp), color = primary_fixed_dim)
      Box(
        modifier = Modifier
          .border(width = 1.dp, color = primary_fixed_dim.copy(alpha = 0.3f), shape = RoundedCornerShape(size = 4.dp))
          .padding(horizontal = 4.dp, vertical = 2.dp)
      ) {
        Text(text = "STAKE", style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp), color = primary_fixed_dim)
      }
    }
  }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
public fun WatchlistSection(
  animatedVisibilityScope: AnimatedVisibilityScope,
  sharedTransitionScope: SharedTransitionScope,
  onNavigateToAsset: (String) -> Unit,
  state: com.example.viewmodel.CryptoState,
  candles: Map<String, List<com.example.ui.components.Candle>>
): Unit {
  Column(
    modifier = Modifier
      .fillMaxWidth(fraction = 1.0f)
      .background(color = Color(color = 0xFF131315), shape = RoundedCornerShape(size = 24.dp))
      .border(width = 1.dp, color = Color.White.copy(alpha = 0.1f), shape = RoundedCornerShape(size = 24.dp))
      .padding(all = 16.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth(fraction = 1.0f)
        .padding(horizontal = 8.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "WATCHLIST",
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp, letterSpacing = 2.sp, fontWeight = FontWeight.Bold),
        color = Color.White
      )
      Text(
        text = "View All",
        style = MaterialTheme.typography.labelSmall,
        color = primary_fixed_dim
      )
    }
    Spacer(modifier = Modifier.height(height = 8.dp))

    if (state.tickers.isEmpty()) {
      Text("Loading watchlist...", color = Color.White.copy(alpha = 0.5f), modifier = Modifier.padding(16.dp))
    } else {
      state.tickers.take(4).forEach { ticker ->
        val symbol = ticker.symbol.replace("USDT", "")
        val price = ticker.lastPrice.toFloatOrNull()?.let { String.format(java.util.Locale.US, "%.2f", it) } ?: ticker.lastPrice
        val changePct = ticker.priceChangePercent.toFloatOrNull() ?: 0f
        val isPositive = changePct >= 0
        val changeStr = "${if(isPositive) "+" else ""}${String.format(java.util.Locale.US, "%.2f", changePct)}%"
        val color = if(symbol == "BTC") Color(0xFFFF9800) else if(symbol == "ETH") primary_fixed_dim else Color(0xFF9C27B0)

        val assetCandles = candles[ticker.symbol] ?: emptyList()
        val sparklineData = assetCandles.map { it.close }

        WatchlistItem(
          name = symbol, // simplified name for now
          symbol = symbol,
          price = "$$price",
          change = changeStr,
          isPositive = isPositive,
          color = color,
          sparklineData = sparklineData,
          animatedVisibilityScope = animatedVisibilityScope,
          sharedTransitionScope = sharedTransitionScope,
          onClick = { onNavigateToAsset(symbol) }
        )
      }
    }
  }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
public fun WatchlistItem(
  name: String,
  symbol: String,
  price: String,
  change: String,
  isPositive: Boolean = true,
  color: Color,
  sparklineData: List<Float> = emptyList(),
  animatedVisibilityScope: AnimatedVisibilityScope,
  sharedTransitionScope: SharedTransitionScope,
  onClick: () -> Unit
): Unit {
  with(sharedTransitionScope) {
    Row(
      modifier = Modifier
        .fillMaxWidth(fraction = 1.0f)
        .clickable { onClick() }
        .padding(horizontal = 8.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(size = 40.dp)
            .background(color = color.copy(alpha = 0.2f), shape = RoundedCornerShape(size = 12.dp))
            .border(width = 1.dp, color = color.copy(alpha = 0.2f), shape = RoundedCornerShape(size = 12.dp))
            .sharedElement(
              state = rememberSharedContentState(key = "icon-$symbol"),
              animatedVisibilityScope = animatedVisibilityScope
            ),
          contentAlignment = Alignment.Center
        ) {
          Text(text = symbol.take(n = 1), color = color, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        }
        Spacer(modifier = Modifier.width(width = 12.dp))
        Column {
          Text(text = name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
          Text(text = symbol, style = DataMedium.copy(fontSize = 10.sp), color = Color.White.copy(alpha = 0.4f))
        }
      }
      
      // Sparkline placeholder
      Box(
        modifier = Modifier
          .weight(weight = 1.0f)
          .height(height = 32.dp)
          .padding(horizontal = 16.dp)
      ) {
        val finalData = if (sparklineData.isNotEmpty()) sparklineData else (if (isPositive) listOf(1f, 2f, 1.5f, 3f, 2.5f, 4f, 5f) else listOf(5f, 4f, 4.5f, 3f, 3.5f, 2f, 1f))
        com.example.ui.components.Sparkline(
          data = finalData,
          color = if (isPositive) secondary_fixed_dim else Color(color = 0xFFFFB4AB),
          strokeWidth = 3f
        )
      }

      Column(
        horizontalAlignment = Alignment.End
      ) {
        Text(text = price, style = DataMedium, color = Color.White)
        Text(
          text = change,
          style = DataMedium.copy(fontSize = 10.sp),
          color = if (isPositive) secondary_fixed_dim else Color(color = 0xFFFFB4AB)
        )
      }
    }
  }
}
