package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.viewmodel.CryptoViewModel
import com.example.viewmodel.CryptoViewModelFactory
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun TransactionHistoryScreen(
  onNavigateBack: () -> Unit,
  onNavigateToReceipt: (String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: CryptoViewModel = viewModel(factory = CryptoViewModelFactory(LocalContext.current))
): Unit {
  var showFilterSheet by remember { mutableStateOf(value = false) }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val uiState by viewModel.uiState.collectAsState()

  Scaffold(
    modifier = modifier.fillMaxSize(fraction = 1.0f),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Transactions",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, fontSize = 24.sp),
            color = Color(0xFF0F172A)
          )
        },
        navigationIcon = {
          IconButton(onClick = onNavigateBack) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = Color(0xFF0F172A)
            )
          }
        },
        actions = {
          IconButton(onClick = { showFilterSheet = true }) {
            Icon(
              imageVector = Icons.Filled.FilterList,
              contentDescription = "Filter",
              tint = Color(0xFF1E60F9)
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.Transparent
        )
      )
    },
    containerColor = Color(0xFFFAFAFA)
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize(fraction = 1.0f)
        .padding(paddingValues = innerPadding)
    ) {
      LazyRow(
        modifier = Modifier.fillMaxWidth(fraction = 1.0f).padding(vertical = 8.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
      ) {
        item { FilterChip(label = "All", isSelected = true) }
        item { FilterChip(label = "Deposits", isSelected = false) }
        item { FilterChip(label = "Withdrawals", isSelected = false) }
        item { FilterChip(label = "Trades", isSelected = false) }
      }
      
      LazyColumn(
        modifier = Modifier.fillMaxSize(fraction = 1.0f).padding(horizontal = 16.dp)
      ) {
        item {
          Spacer(modifier = Modifier.height(height = 12.dp))
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .background(color = Color.White, shape = RoundedCornerShape(size = 20.dp))
              .border(width = 1.dp, color = Color(0xFFF1F5F9), shape = RoundedCornerShape(size = 20.dp))
              .padding(all = 16.dp)
          ) {
            Text(
              text = "RECENT TRANSACTIONS",
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
              color = Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.height(height = 12.dp))

            if (uiState.transactions.isEmpty()) {
              TransactionItem(
                type = "Received", asset = "BTC", amount = "+0.042 BTC", fiat = "+$2,642.10",
                icon = Icons.Filled.CallReceived, color = Color(0xFF22C55E),
                onClick = { onNavigateToReceipt("TX-9982") }
              )
              HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
              TransactionItem(
                type = "Sent", asset = "ETH", amount = "-1.2 ETH", fiat = "-$3,842.12",
                icon = Icons.Filled.CallMade, color = Color(0xFFEF4444),
                onClick = { onNavigateToReceipt("TX-9981") }
              )
            } else {
              uiState.transactions.forEachIndexed { index, tx ->
                val isPositive = tx.type == "BUY" || tx.type == "SWAP"
                val icon = if (isPositive) Icons.Filled.CallReceived else Icons.Filled.CallMade
                val color = if (isPositive) Color(0xFF22C55E) else Color(0xFFEF4444)
                val amountStr = "${String.format(Locale.US, "%.4f", tx.fromAmount)} ${tx.fromAsset}"
                val fiatStr = "$${String.format(Locale.US, "%.2f", tx.fromAmount * tx.priceUsd)}"

                TransactionItem(
                  type = tx.type,
                  asset = "${tx.fromAsset} → ${tx.toAsset}",
                  amount = amountStr,
                  fiat = fiatStr,
                  icon = icon,
                  color = color,
                  onClick = { onNavigateToReceipt("TX-${tx.id}") }
                )
                if (index < uiState.transactions.size - 1) {
                  HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                }
              }
            }
          }
          Spacer(modifier = Modifier.height(height = 24.dp))
        }
      }
    }
    
    if (showFilterSheet) {
      FilterSortSheet(
        sheetState = sheetState,
        onDismissRequest = { showFilterSheet = false },
        onApply = { showFilterSheet = false }
      )
    }
  }
}

@Composable
public fun FilterChip(label: String, isSelected: Boolean): Unit {
  Box(
    modifier = Modifier
      .background(
        color = if (isSelected) Color(0xFF1E60F9) else Color(0xFFF0F3FA),
        shape = RoundedCornerShape(size = 16.dp)
      )
      .clickable { }
      .padding(horizontal = 16.dp, vertical = 8.dp)
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelMedium.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium),
      color = if (isSelected) Color.White else Color(0xFF64748B)
    )
  }
}

@Composable
public fun TransactionItem(
  type: String, asset: String, amount: String, fiat: String, icon: ImageVector, color: Color, onClick: () -> Unit
): Unit {
  Row(
    modifier = Modifier
      .fillMaxWidth(fraction = 1.0f)
      .clickable { onClick() }
      .padding(vertical = 12.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier
          .size(size = 40.dp)
          .background(color = Color(0xFFF0F3FA), shape = CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(size = 20.dp))
      }
      Spacer(modifier = Modifier.width(width = 12.dp))
      Column {
        Text(
          text = "$type $asset",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
          color = Color(0xFF0F172A)
        )
        Spacer(modifier = Modifier.height(height = 2.dp))
        Text(
          text = "Completed",
          style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
          color = Color(0xFF64748B)
        )
      }
    }
    Column(horizontalAlignment = Alignment.End) {
      Text(
        text = amount,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
        color = color
      )
      Spacer(modifier = Modifier.height(height = 2.dp))
      Text(
        text = fiat,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
        color = Color(0xFF64748B)
      )
    }
  }
}
