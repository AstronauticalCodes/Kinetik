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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.ui.components.CandlestickChart
import com.example.ui.components.OrderBookView
import com.example.viewmodel.CryptoViewModel
import com.example.viewmodel.CryptoViewModelFactory
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
public fun AssetDetailScreen(
  symbol: String,
  animatedVisibilityScope: AnimatedVisibilityScope,
  sharedTransitionScope: SharedTransitionScope,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier,
  viewModel: CryptoViewModel = viewModel(factory = CryptoViewModelFactory(LocalContext.current))
): Unit {
  var showOrderSheet by remember { mutableStateOf(false) }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var isFavorite by remember { mutableStateOf(false) }
  var selectedTimeframe by remember { mutableStateOf("1D") }

  val state by viewModel.state.collectAsState()
  val candles by viewModel.candles.collectAsState()

  val tickerSymbol = "${symbol}USDT"
  val currentTicker = state.tickers.find { it.symbol == tickerSymbol || it.symbol == symbol }
  
  val priceVal = currentTicker?.lastPrice?.toFloatOrNull() ?: 67420.00f
  val priceStr = String.format(Locale.US, "$%,.2f", priceVal)
  val changePct = currentTicker?.priceChangePercent?.toFloatOrNull() ?: 2.45f
  val isPositive = changePct >= 0
  val changeStr = "${if (isPositive) "+" else ""}${String.format(Locale.US, "%.2f", changePct)}%"
  val changeAmtStr = "${if (isPositive) "+" else ""}${String.format(Locale.US, "$%,.2f", priceVal * (changePct / 100f))}"

  val highStr = currentTicker?.highPrice?.toDoubleOrNull()?.let { String.format(Locale.US, "$%,.2f", it) } ?: "$68,240.00"
  val lowStr = currentTicker?.lowPrice?.toDoubleOrNull()?.let { String.format(Locale.US, "$%,.2f", it) } ?: "$65,820.00"
  val volStr = currentTicker?.volume?.toDoubleOrNull()?.let { String.format(Locale.US, "$%,.2fB", it / 1_000_000.0) } ?: "$28.4B"

  val fullName = when (symbol.uppercase(Locale.ROOT)) {
    "BTC" -> "Bitcoin"
    "ETH" -> "Ethereum"
    "SOL" -> "Solana"
    "BNB" -> "BNB"
    else -> symbol
  }

  LaunchedEffect(symbol) {
    if (candles[tickerSymbol] == null) {
      viewModel.fetchKlines(tickerSymbol, "1h", 24)
    }
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = Color(0xFFFAFAFA),
    topBar = {
      TopAppBar(
        title = {
          Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(
              text = fullName,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0F172A)
            )
            Text(
              text = "$symbol / USD",
              fontSize = 12.sp,
              color = Color(0xFF64748B)
            )
          }
        },
        navigationIcon = {
          Box(
            modifier = Modifier
              .padding(start = 12.dp)
              .size(40.dp)
              .clip(CircleShape)
              .background(Color(0xFFF0F3FA))
              .clickable { onNavigateBack() },
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = Color(0xFF0F172A),
              modifier = Modifier.size(20.dp)
            )
          }
        },
        actions = {
          Box(
            modifier = Modifier
              .padding(end = 12.dp)
              .size(40.dp)
              .clip(CircleShape)
              .background(Color(0xFFF0F3FA))
              .clickable { isFavorite = !isFavorite },
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
              contentDescription = "Favorite",
              tint = if (isFavorite) Color(0xFFEAB308) else Color(0xFF0F172A),
              modifier = Modifier.size(20.dp)
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.Transparent
        )
      )
    },
    bottomBar = {
      BuySellBottomBar(
        onBuy = { showOrderSheet = true },
        onSell = { showOrderSheet = true }
      )
    }
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      contentPadding = PaddingValues(bottom = 24.dp)
    ) {
      item {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
          Text(
            text = priceStr,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = changeAmtStr,
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold,
              color = if (isPositive) Color(0xFF22C55E) else Color(0xFFEF4444)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
              modifier = Modifier
                .clip(CircleShape)
                .background(if (isPositive) Color(0xFFDCFCE7) else Color(0xFFFEE2E2))
                .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
              Text(
                text = changeStr,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isPositive) Color(0xFF22C55E) else Color(0xFFEF4444)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Today",
              fontSize = 13.sp,
              color = Color(0xFF64748B)
            )
          }
        }
      }

      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(20.dp))
            .padding(16.dp)
        ) {
          val assetCandles = candles[tickerSymbol] ?: emptyList()
          CandlestickChart(data = assetCandles)
        }
      }

      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf("1H", "4H", "1D", "1W", "1M").forEach { tf ->
            val isSelected = tf == selectedTimeframe
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(CircleShape)
                .background(if (isSelected) Color(0xFF2563EB) else Color(0xFFF0F3FA))
                .clickable { selectedTimeframe = tf }
                .padding(vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = tf,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else Color(0xFF64748B)
              )
            }
          }
        }
      }

      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
              Text(text = "24h High", fontSize = 12.sp, color = Color(0xFF64748B))
              Spacer(modifier = Modifier.height(4.dp))
              Text(text = highStr, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF22C55E))
            }
            Box(modifier = Modifier.width(1.dp).height(32.dp).background(Color(0xFFE2E8F0)))
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
              Text(text = "24h Low", fontSize = 12.sp, color = Color(0xFF64748B))
              Spacer(modifier = Modifier.height(4.dp))
              Text(text = lowStr, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444))
            }
            Box(modifier = Modifier.width(1.dp).height(32.dp).background(Color(0xFFE2E8F0)))
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
              Text(text = "Volume", fontSize = 12.sp, color = Color(0xFF64748B))
              Spacer(modifier = Modifier.height(4.dp))
              Text(text = volStr, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
            }
          }
        }
      }

      item {
        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
          OrderBookView(symbol = symbol)
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
        price = priceStr,
        total = String.format(Locale.US, "$%,.2f", priceVal * 0.01f)
      )
    }
  }
}

@Composable
fun BuySellBottomBar(
  onBuy: () -> Unit,
  onSell: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color.White)
      .border(1.dp, Color(0xFFF1F5F9))
      .padding(horizontal = 20.dp, vertical = 12.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Button(
        onClick = onBuy,
        modifier = Modifier
          .weight(1f)
          .height(52.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFF009E60),
          contentColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.ArrowUpward,
            contentDescription = "Buy",
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "Buy", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
      }

      Button(
        onClick = onSell,
        modifier = Modifier
          .weight(1f)
          .height(52.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFFD92D20),
          contentColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.ArrowDownward,
            contentDescription = "Sell",
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "Sell", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
