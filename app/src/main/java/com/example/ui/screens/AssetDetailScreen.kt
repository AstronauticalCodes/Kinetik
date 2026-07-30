package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.ui.theme.primary_fixed_dim
import com.example.ui.theme.secondary_fixed_dim
import com.example.ui.theme.surface_container_high
import com.example.ui.theme.surface_container_low

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
public fun AssetDetailScreen(
  symbol: String,
  animatedVisibilityScope: AnimatedVisibilityScope,
  sharedTransitionScope: SharedTransitionScope,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier,
  viewModel: CryptoViewModel = viewModel()
): Unit {
  var showOrderSheet by remember { mutableStateOf(value = false) }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  val state by viewModel.state.collectAsState()
  val candles by viewModel.candles.collectAsState()

  // Find ticker for current symbol, suffixing with USDT
  val tickerSymbol = "${symbol}USDT"
  val currentTicker = state.tickers.find { it.symbol == tickerSymbol }
  
  val price = currentTicker?.lastPrice?.toFloatOrNull()?.let { String.format(java.util.Locale.US, "%.2f", it) } ?: "64,120.32"
  val changePct = currentTicker?.priceChangePercent?.toFloatOrNull() ?: 2.4f
  val changeStr = "${if (changePct >= 0) "+" else ""}${String.format(java.util.Locale.US, "%.2f", changePct)}%"
  val isPositive = changePct >= 0

  // Trigger klines fetch if not already present
  androidx.compose.runtime.LaunchedEffect(symbol) {
    if (candles[tickerSymbol] == null) {
      viewModel.fetchKlines(tickerSymbol, "1h", 24)
    }
  }

  with(sharedTransitionScope) {
    Scaffold(
      modifier = modifier.fillMaxSize(fraction = 1.0f),
      topBar = {
        TopAppBar(
          title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(size = 32.dp)
                  .clip(shape = CircleShape)
                  .background(color = primary_fixed_dim.copy(alpha = 0.2f))
                  .sharedElement(
                    state = rememberSharedContentState(key = "icon-$symbol"),
                    animatedVisibilityScope = animatedVisibilityScope
                  ),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Filled.CurrencyBitcoin,
                  contentDescription = null,
                  tint = primary_fixed_dim,
                  modifier = Modifier.size(size = 18.dp)
                )
              }
              Spacer(modifier = Modifier.width(width = 12.dp))
              Text(
                text = symbol,
                style = MaterialTheme.typography.titleMedium,
                color = md_theme_dark_onSurface
              )
            }
          },
          navigationIcon = {
            IconButton(onClick = onNavigateBack) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = md_theme_dark_onSurfaceVariant
              )
            }
          },
          actions = {
            val context = androidx.compose.ui.platform.LocalContext.current
            IconButton(onClick = { android.widget.Toast.makeText(context, "Added to Watchlist", android.widget.Toast.LENGTH_SHORT).show() }) {
              Icon(
                imageVector = Icons.Filled.StarBorder,
                contentDescription = "Star",
                tint = md_theme_dark_onSurfaceVariant
              )
            }
            var showMenu by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
            IconButton(onClick = { showMenu = true }) {
              Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "More",
                tint = md_theme_dark_onSurfaceVariant
              )
              androidx.compose.material3.DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
              ) {
                androidx.compose.material3.DropdownMenuItem(
                  text = { Text("Share") },
                  onClick = { 
                    showMenu = false
                    val sendIntent = android.content.Intent().apply {
                      action = android.content.Intent.ACTION_SEND
                      putExtra(android.content.Intent.EXTRA_TEXT, "Check out Bitcoin on AI Studio App!")
                      type = "text/plain"
                    }
                    val shareIntent = android.content.Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                  }
                )
                androidx.compose.material3.DropdownMenuItem(
                  text = { Text("Set Price Alert") },
                  onClick = { 
                    showMenu = false
                    android.widget.Toast.makeText(context, "Price alert set", android.widget.Toast.LENGTH_SHORT).show() 
                  }
                )
                androidx.compose.material3.DropdownMenuItem(
                  text = { Text("View Project Website") },
                  onClick = { 
                    showMenu = false
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://bitcoin.org"))
                    context.startActivity(intent)
                  }
                )
              }
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
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 80.dp)
      ) {
        item {
          Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(height = 8.dp))
            Text(
              text = "$$price",
              style = DataLarge.copy(fontSize = 40.sp),
              color = Color.White
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
            Text(
              text = changeStr,
              style = DataMedium.copy(fontSize = 16.sp),
              color = if (isPositive) secondary_fixed_dim else Color(color = 0xFFFFB4AB)
            )
          }
          Spacer(modifier = Modifier.height(height = 24.dp))
        }
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth(fraction = 1.0f)
              .height(height = 280.dp)
              .padding(horizontal = 16.dp)
              .background(color = Color(color = 0xFF131315), shape = RoundedCornerShape(size = 24.dp))
              .border(width = 1.dp, color = Color.White.copy(alpha = 0.1f), shape = RoundedCornerShape(size = 24.dp)),
            contentAlignment = Alignment.Center
          ) {
            val assetCandles = candles[tickerSymbol] ?: emptyList()
            if (assetCandles.isEmpty()) {
              Text("Loading chart...", color = Color.White.copy(alpha = 0.5f))
            } else {
              com.example.ui.components.CandlestickChart(
                data = assetCandles,
                modifier = Modifier.padding(16.dp)
              )
            }
          }
          Spacer(modifier = Modifier.height(height = 24.dp))
        }
        item {
          Row(
            modifier = Modifier
              .fillMaxWidth(fraction = 1.0f)
              .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
          ) {
            InfoCard(title = "24H HIGH", value = "$65,240", modifier = Modifier.weight(weight = 1.0f))
            InfoCard(title = "24H LOW", value = "$63,890", modifier = Modifier.weight(weight = 1.0f))
            InfoCard(title = "VOL", value = "24.1B", modifier = Modifier.weight(weight = 1.0f))
          }
          Spacer(modifier = Modifier.height(height = 24.dp))
        }
        item {
          // Order Book Placeholder
          Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(text = "ORDER BOOK", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
            Spacer(modifier = Modifier.height(height = 12.dp))
            Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f), horizontalArrangement = Arrangement.SpaceBetween) {
               Column(modifier = Modifier.weight(weight = 1.0f)) {
                 Text(text = "BIDS", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = secondary_fixed_dim)
                 OrderRow(price = "64,120", amount = "0.24", color = secondary_fixed_dim)
                 OrderRow(price = "64,118", amount = "1.52", color = secondary_fixed_dim)
                 OrderRow(price = "64,115", amount = "0.84", color = secondary_fixed_dim)
               }
               Spacer(modifier = Modifier.width(width = 16.dp))
               Column(modifier = Modifier.weight(weight = 1.0f)) {
                 Text(text = "ASKS", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = Color(color = 0xFFFFB4AB))
                 OrderRow(price = "64,121", amount = "0.11", color = Color(color = 0xFFFFB4AB))
                 OrderRow(price = "64,124", amount = "2.30", color = Color(color = 0xFFFFB4AB))
                 OrderRow(price = "64,125", amount = "1.45", color = Color(color = 0xFFFFB4AB))
               }
            }
          }
          Spacer(modifier = Modifier.height(height = 24.dp))
        }
        item {
          // Actions
          Row(
            modifier = Modifier
              .fillMaxWidth(fraction = 1.0f)
              .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
          ) {
            Button(
              onClick = { showOrderSheet = true },
              modifier = Modifier
                .weight(weight = 1.0f)
                .height(height = 56.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = primary_fixed_dim,
                contentColor = Color(color = 0xFF001F24)
              ),
              shape = RoundedCornerShape(size = 12.dp)
            ) {
              Text(text = "BUY", style = MaterialTheme.typography.labelLarge)
            }
            Button(
              onClick = { showOrderSheet = true },
              modifier = Modifier
                .weight(weight = 1.0f)
                .height(height = 56.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = surface_container_high,
                contentColor = md_theme_dark_onSurface
              ),
              shape = RoundedCornerShape(size = 12.dp)
            ) {
              Text(text = "SELL", style = MaterialTheme.typography.labelLarge)
            }
          }
        }
      }
      
      if (showOrderSheet) {
        OrderConfirmationSheet(
          sheetState = sheetState,
          onDismissRequest = { showOrderSheet = false },
          onConfirm = { showOrderSheet = false },
          asset = symbol,
          amount = "0.01",
          price = "$64,120.32",
          total = "$641.20"
        )
      }
    }
  }
}

@Composable
public fun InfoCard(title: String, value: String, modifier: Modifier = Modifier): Unit {
  Column(
    modifier = modifier
      .background(color = Color.White.copy(alpha = 0.03f), shape = RoundedCornerShape(size = 12.dp))
      .border(width = 1.dp, color = Color.White.copy(alpha = 0.05f), shape = RoundedCornerShape(size = 12.dp))
      .padding(all = 12.dp)
  ) {
    Text(text = title, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = Color.White.copy(alpha = 0.4f))
    Spacer(modifier = Modifier.height(height = 4.dp))
    Text(text = value, style = DataMedium.copy(fontSize = 14.sp), color = Color.White)
  }
}

@Composable
public fun OrderRow(price: String, amount: String, color: Color): Unit {
  Row(
    modifier = Modifier
      .fillMaxWidth(fraction = 1.0f)
      .padding(vertical = 4.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(text = price, style = DataMedium.copy(fontSize = 12.sp), color = color)
    Text(text = amount, style = DataMedium.copy(fontSize = 12.sp), color = md_theme_dark_onSurfaceVariant)
  }
}
