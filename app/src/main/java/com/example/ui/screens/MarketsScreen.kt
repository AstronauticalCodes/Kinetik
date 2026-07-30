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
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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

  with(sharedTransitionScope) {
    Scaffold(
      modifier = modifier.fillMaxSize(fraction = 1.0f),
      topBar = {
        TopAppBar(
          title = {
            Text(
              text = "MARKETS",
              style = MaterialTheme.typography.headlineMedium,
              color = md_theme_dark_primaryContainer,
              letterSpacing = 2.sp
            )
          },
          actions = {
            IconButton(onClick = onNavigateToSearch) {
              Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = md_theme_dark_onSurfaceVariant
              )
            }
            Box(
              modifier = Modifier
                .padding(end = 16.dp)
                .size(size = 32.dp)
                .clip(shape = CircleShape)
                .background(color = primary_fixed_dim),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "User",
                tint = Color(color = 0xFF001F24),
                modifier = Modifier.size(size = 18.dp)
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
      Column(
        modifier = Modifier
          .fillMaxSize(fraction = 1.0f)
          .padding(paddingValues = innerPadding)
      ) {
        Box(
          modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onNavigateToSearch() }
        ) {
          OutlinedTextField(
            value = "",
            onValueChange = {},
            enabled = false,
            modifier = Modifier.fillMaxWidth(fraction = 1.0f),
            placeholder = {
              Text(text = "Search pairs, sectors, or addresses...", color = md_theme_dark_onSurfaceVariant.copy(alpha = 0.5f))
            },
            leadingIcon = {
              Icon(imageVector = Icons.Filled.Search, contentDescription = "Search", tint = primary_fixed_dim)
            },
            colors = OutlinedTextFieldDefaults.colors(
              disabledBorderColor = primary_fixed_dim.copy(alpha = 0.3f),
              disabledContainerColor = surface_container_highest,
              disabledTextColor = md_theme_dark_onSurfaceVariant.copy(alpha = 0.5f),
              disabledPlaceholderColor = md_theme_dark_onSurfaceVariant.copy(alpha = 0.5f),
              disabledLeadingIconColor = primary_fixed_dim
            ),
            shape = RoundedCornerShape(size = 8.dp)
          )
        }
        
        val selectedCategoryState = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(0) }
        LazyRow(
          modifier = Modifier.fillMaxWidth(fraction = 1.0f),
          contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
          val categories: List<String> = listOf("ALL ASSETS", "DEFI", "LAYER 1", "AI", "METAVERSE", "GAMING")
          items(count = categories.size) { index ->
            val category: String = categories[index]
            val isSelected: Boolean = index == selectedCategoryState.value
            Box(
              modifier = Modifier
                .background(
                  color = if (isSelected) primary_fixed_dim.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.05f),
                  shape = RoundedCornerShape(size = 12.dp)
                )
                .border(
                  width = 1.dp,
                  color = if (isSelected) primary_fixed_dim.copy(alpha = 0.5f) else Color.Transparent,
                  shape = RoundedCornerShape(size = 12.dp)
                )
                .clickable { selectedCategoryState.value = index }
                .padding(horizontal = 16.dp, vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = category,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) primary_fixed_dim else Color.White.copy(alpha = 0.6f)
              )
            }
          }
        }
        
        // Market News Feed
        LazyRow(
          modifier = Modifier
            .fillMaxWidth(fraction = 1.0f)
            .background(color = surface_container_low.copy(alpha = 0.5f))
            .border(width = 1.dp, color = Color.White.copy(alpha = 0.05f))
            .padding(vertical = 12.dp),
          contentPadding = PaddingValues(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {
          item { MarketNewsItem(sentiment = "BULLISH", text = "SEC approves new Spot ETFs") }
          item { MarketNewsItem(sentiment = "NEUTRAL", text = "Network upgrade scheduled for epoch 402", color = md_theme_dark_onSurface) }
          item { MarketNewsItem(sentiment = "BEARISH", text = "Regulatory concerns in EU region", color = Color(color = 0xFFFFB4AB)) }
        }

        Row(
          modifier = Modifier
            .fillMaxWidth(fraction = 1.0f)
            .padding(horizontal = 16.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(text = "ASSET / 24H TREND", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant.copy(alpha = 0.6f))
          Text(text = "PRICE / CHG", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant.copy(alpha = 0.6f))
        }
        
        LazyColumn(
          modifier = Modifier.fillMaxWidth(fraction = 1.0f),
          contentPadding = PaddingValues(bottom = 24.dp)
        ) {
          if (state.tickers.isEmpty()) {
            item {
              Text(
                text = "Loading assets...",
                color = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
              )
            }
          } else {
            items(count = state.tickers.size) { index ->
              val ticker = state.tickers[index]
              val symbol = ticker.symbol.replace("USDT", "")
              val price = ticker.lastPrice.toFloatOrNull()?.let { String.format(java.util.Locale.US, "%.2f", it) } ?: ticker.lastPrice
              val changePct = ticker.priceChangePercent.toFloatOrNull() ?: 0f
              val isPositive = changePct >= 0
              val changeStr = "${if(isPositive) "+" else ""}${String.format(java.util.Locale.US, "%.2f", changePct)}%"

              val assetCandles = candles[ticker.symbol] ?: emptyList()
              val sparklineData = assetCandles.map { it.close }

              MarketAssetRow(
                symbol = symbol,
                name = symbol, // Use symbol as name for simplicity unless we map them
                price = "$$price",
                change = changeStr,
                isPositive = isPositive,
                sparklineData = sparklineData,
                animatedVisibilityScope = animatedVisibilityScope,
                sharedTransitionScope = this@with,
                onClick = { onNavigateToAsset(symbol) }
              )
            }
          }
        }
      }
    }
  }
}

@Composable
public fun MarketNewsItem(sentiment: String, text: String, color: Color = secondary_fixed_dim): Unit {
  Row(
    modifier = Modifier
      .background(color = surface_container_highest, shape = RoundedCornerShape(size = 8.dp))
      .padding(horizontal = 12.dp, vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(imageVector = Icons.Filled.FiberManualRecord, contentDescription = null, tint = color, modifier = Modifier.size(size = 8.dp))
    Spacer(modifier = Modifier.width(width = 8.dp))
    Text(text = sentiment, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = color)
    Spacer(modifier = Modifier.width(width = 12.dp))
    Text(text = text, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp), color = md_theme_dark_onSurface)
  }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
public fun MarketAssetRow(
  symbol: String,
  name: String,
  price: String,
  change: String,
  isPositive: Boolean,
  sparklineData: List<Float> = emptyList(),
  animatedVisibilityScope: AnimatedVisibilityScope,
  sharedTransitionScope: SharedTransitionScope,
  onClick: () -> Unit
): Unit {
  with(sharedTransitionScope) {
    Row(
      modifier = Modifier
        .fillMaxWidth(fraction = 1.0f)
        .padding(horizontal = 16.dp, vertical = 4.dp)
        .background(color = Color(color = 0xFF131315), shape = RoundedCornerShape(size = 16.dp))
        .border(width = 1.dp, color = Color.White.copy(alpha = 0.05f), shape = RoundedCornerShape(size = 16.dp))
        .clickable { onClick() }
        .padding(horizontal = 16.dp, vertical = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(weight = 1.0f)
      ) {
        Box(
          modifier = Modifier
            .size(size = 40.dp)
            .background(color = primary_fixed_dim.copy(alpha = 0.1f), shape = RoundedCornerShape(size = 12.dp))
            .border(width = 1.dp, color = primary_fixed_dim.copy(alpha = 0.2f), shape = RoundedCornerShape(size = 12.dp))
            .sharedElement(
              state = rememberSharedContentState(key = "icon-$symbol"),
              animatedVisibilityScope = animatedVisibilityScope
            ),
          contentAlignment = Alignment.Center
        ) {
          Text(text = symbol.take(n = 1), color = primary_fixed_dim, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        }
        Spacer(modifier = Modifier.width(width = 16.dp))
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = symbol, style = DataLarge, color = Color.White)
            if (symbol == "BTC") {
               Spacer(modifier = Modifier.width(width = 8.dp))
               Box(
                 modifier = Modifier.background(color = secondary_fixed_dim.copy(alpha = 0.1f), shape = RoundedCornerShape(size = 4.dp)).padding(horizontal = 4.dp, vertical = 2.dp)
               ) {
                 Text(text = "RANK 1", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = secondary_fixed_dim)
               }
            }
          }
          Text(text = name, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp), color = Color.White.copy(alpha = 0.4f))
        }
      }
      
      // Sparkline placeholder
      Box(
        modifier = Modifier
          .weight(weight = 1.0f)
          .height(height = 40.dp)
          .padding(horizontal = 16.dp)
      ) {
        val finalData = if (sparklineData.isNotEmpty()) sparklineData else (if (isPositive) listOf(1f, 3f, 2f, 4f, 3.5f, 5f, 6f) else listOf(6f, 5f, 5.5f, 4f, 4.5f, 3f, 2f))
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
          style = DataMedium,
          color = if (isPositive) secondary_fixed_dim else Color(color = 0xFFFFB4AB)
        )
      }
    }
  }
}
