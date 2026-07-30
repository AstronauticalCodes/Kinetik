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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.theme.DataLarge
import com.example.ui.theme.DataMedium
import com.example.ui.theme.md_theme_dark_onSurface
import com.example.ui.theme.md_theme_dark_onSurfaceVariant
import com.example.ui.theme.md_theme_dark_primaryContainer
import com.example.ui.theme.primary_fixed_dim
import com.example.ui.theme.secondary_fixed_dim
import com.example.ui.theme.surface_container_high
import com.example.ui.theme.surface_container_low
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

  val totalNetWorth = uiState.totalNetWorth
  val dailyPnl = uiState.dailyPnl

  Scaffold(
    modifier = modifier.fillMaxSize(fraction = 1.0f),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "PORTFOLIO",
            style = MaterialTheme.typography.headlineMedium,
            color = md_theme_dark_primaryContainer,
            letterSpacing = 2.sp
          )
        },
        actions = {
          IconButton(onClick = onNavigateToHistory) {
            Icon(
              imageVector = Icons.Filled.History,
              contentDescription = "History",
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
    LazyColumn(
      modifier = Modifier
        .fillMaxSize(fraction = 1.0f)
        .padding(paddingValues = innerPadding),
      contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 24.dp)
    ) {
      item {
        Spacer(modifier = Modifier.height(height = 16.dp))
        // Balance Banner Card
        Box(
          modifier = Modifier
            .fillMaxWidth(fraction = 1.0f)
            .height(height = 240.dp)
            .background(color = surface_container_low.copy(alpha = 0.5f), shape = RoundedCornerShape(size = 16.dp)),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "BALANCE", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
            Text(
              text = if (totalNetWorth > 0) String.format(Locale.US, "$%,.2f", totalNetWorth) else "$0.00",
              style = DataLarge.copy(fontSize = 32.sp),
              color = md_theme_dark_primaryContainer
            )
          }
        }
        Spacer(modifier = Modifier.height(height = 24.dp))
      }
      item {
        Row(
          modifier = Modifier.fillMaxWidth(fraction = 1.0f),
          horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
          Column(
            modifier = Modifier
              .weight(weight = 2.0f)
              .background(color = surface_container_high, shape = RoundedCornerShape(size = 8.dp))
              .padding(all = 16.dp)
          ) {
             Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f), horizontalArrangement = Arrangement.SpaceBetween) {
               Text(text = "Net Worth (USD)", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
               Icon(imageVector = Icons.Filled.AccountBalanceWallet, contentDescription = null, tint = primary_fixed_dim, modifier = Modifier.size(size = 18.dp))
             }
             Spacer(modifier = Modifier.height(height = 4.dp))
             Row(verticalAlignment = Alignment.Bottom) {
               Text(
                 text = if (totalNetWorth > 0) String.format(Locale.US, "$%,.2f", totalNetWorth) else "$0.00",
                 style = DataLarge.copy(fontSize = 24.sp),
                 color = primary_fixed_dim
               )
               Spacer(modifier = Modifier.width(width = 8.dp))
               val pnlPct = if (totalNetWorth > 0) (dailyPnl / totalNetWorth) * 100 else 0.0
               Text(
                 text = String.format(Locale.US, "%+.1f%%", pnlPct),
                 style = DataMedium,
                 color = if (pnlPct >= 0) secondary_fixed_dim else Color(0xFFFFB4AB)
               )
             }
          }
        }
        Spacer(modifier = Modifier.height(height = 8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(fraction = 1.0f),
          horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
          Column(
            modifier = Modifier
              .weight(weight = 1.0f)
              .background(color = surface_container_low, shape = RoundedCornerShape(size = 8.dp))
              .padding(all = 16.dp)
          ) {
             Text(text = "Daily PNL", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
             Spacer(modifier = Modifier.height(height = 4.dp))
             Text(
               text = "${if (dailyPnl >= 0) "+" else ""}${String.format(Locale.US, "$%,.2f", dailyPnl)}",
               style = DataMedium,
               color = if (dailyPnl >= 0) secondary_fixed_dim else Color(0xFFFFB4AB)
             )
          }
          Column(
            modifier = Modifier
              .weight(weight = 1.0f)
              .background(color = surface_container_low, shape = RoundedCornerShape(size = 8.dp))
              .padding(all = 16.dp)
          ) {
             Text(text = "All Time PNL", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
             Spacer(modifier = Modifier.height(height = 4.dp))
             Text(text = "+$42,890.11", style = DataMedium, color = secondary_fixed_dim)
          }
        }
        Spacer(modifier = Modifier.height(height = 24.dp))
      }
      item {
        Row(
          modifier = Modifier.fillMaxWidth(fraction = 1.0f),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "CURRENT HOLDINGS", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "EDIT", style = MaterialTheme.typography.labelSmall, color = primary_fixed_dim)
            Spacer(modifier = Modifier.width(width = 4.dp))
            Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit", tint = primary_fixed_dim, modifier = Modifier.size(size = 14.dp))
          }
        }
        Spacer(modifier = Modifier.height(height = 16.dp))
      }

      if (uiState.holdings.isEmpty()) {
        item {
          Text("No holdings yet", color = Color.White.copy(alpha = 0.5f))
        }
      } else {
        items(count = uiState.holdings.size) { index ->
          val holding = uiState.holdings[index]
          val ticker = uiState.tickers.find {
            it.symbol == "${holding.assetSymbol}USDT" || it.symbol == holding.assetSymbol
          }
          val currentPrice = if (holding.isFiat) 1.0 else (ticker?.lastPrice?.toDoubleOrNull() ?: holding.avgBuyPriceUsd)
          val value = holding.amount * currentPrice
          val changePct = ticker?.priceChangePercent?.toDoubleOrNull() ?: 0.0
          val isPositive = changePct >= 0
          val changeStr = "${if (isPositive) "+" else ""}${String.format(Locale.US, "%.2f", changePct)}%"
          val priceStr = String.format(Locale.US, "$%,.2f", currentPrice)
          val valueStr = String.format(Locale.US, "$%,.2f", value)
          val amountStr = "${String.format(Locale.US, "%.4f", holding.amount)} ${holding.assetSymbol}"
          val avgBuyStr = String.format(Locale.US, "$%,.2f", holding.avgBuyPriceUsd)
          val roiValue = (currentPrice - holding.avgBuyPriceUsd) * holding.amount
          val roiStr = "${if (roiValue >= 0) "+" else ""}${String.format(Locale.US, "$%,.2f", roiValue)}"

          HoldingItem(
            name = holding.name,
            amount = amountStr,
            price = priceStr,
            change = changeStr,
            avgBuy = avgBuyStr,
            roi = roiStr,
            value = valueStr,
            isPositive = isPositive,
            onClick = { onNavigateToAsset(holding.assetSymbol) }
          )
          Spacer(modifier = Modifier.height(height = 8.dp))
        }
      }
    }
  }
}

@Composable
public fun HoldingItem(
  name: String,
  amount: String,
  price: String,
  change: String,
  avgBuy: String,
  roi: String,
  value: String,
  isPositive: Boolean = true,
  onClick: () -> Unit = {}
): Unit {
  Column(
    modifier = Modifier
      .fillMaxWidth(fraction = 1.0f)
      .background(color = surface_container_low.copy(alpha = 0.8f), shape = RoundedCornerShape(size = 12.dp))
      .clickable { onClick() }
      .padding(all = 16.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(fraction = 1.0f),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(size = 40.dp)
            .clip(shape = CircleShape)
            .background(color = Color(color = 0xFFF7931A).copy(alpha = 0.2f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Filled.CurrencyBitcoin,
            contentDescription = null,
            tint = Color(color = 0xFFF7931A),
            modifier = Modifier.size(size = 24.dp)
          )
        }
        Spacer(modifier = Modifier.width(width = 16.dp))
        Column {
          Text(text = name, style = DataMedium.copy(fontSize = 16.sp), color = md_theme_dark_onSurface)
          Text(text = amount, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = md_theme_dark_onSurfaceVariant)
        }
      }
      Column(horizontalAlignment = Alignment.End) {
        Text(text = price, style = DataMedium, color = md_theme_dark_onSurface)
        Text(text = change, style = DataMedium.copy(fontSize = 12.sp), color = if (isPositive) secondary_fixed_dim else Color(color = 0xFFFFB4AB))
      }
    }
    Spacer(modifier = Modifier.height(height = 12.dp))
    Row(
      modifier = Modifier
        .fillMaxWidth(fraction = 1.0f)
        .border(width = 1.dp, color = Color.Transparent)
        .padding(top = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column {
        Text(text = "AVG BUY", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = md_theme_dark_onSurfaceVariant)
        Text(text = avgBuy, style = DataMedium.copy(fontSize = 12.sp), color = md_theme_dark_onSurface)
      }
      Column {
        Text(text = "ROI", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = md_theme_dark_onSurfaceVariant)
        Text(text = roi, style = DataMedium.copy(fontSize = 12.sp), color = if (isPositive) secondary_fixed_dim else Color(color = 0xFFFFB4AB))
      }
      Column(horizontalAlignment = Alignment.End) {
        Text(text = "VALUE", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = md_theme_dark_onSurfaceVariant)
        Text(text = value, style = DataMedium.copy(fontSize = 12.sp), color = md_theme_dark_onSurface)
      }
    }
  }
}
