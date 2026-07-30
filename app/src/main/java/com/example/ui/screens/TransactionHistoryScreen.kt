package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DataMedium
import com.example.ui.theme.md_theme_dark_onSurface
import com.example.ui.theme.md_theme_dark_onSurfaceVariant
import com.example.ui.theme.primary_fixed_dim
import com.example.ui.theme.secondary_fixed_dim
import com.example.ui.theme.surface_container_low

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun TransactionHistoryScreen(
  onNavigateBack: () -> Unit,
  onNavigateToReceipt: (String) -> Unit,
  modifier: Modifier = Modifier
): Unit {
  var showFilterSheet by remember { mutableStateOf(value = false) }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  Scaffold(
    modifier = modifier.fillMaxSize(fraction = 1.0f),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "TRANSACTIONS",
            style = MaterialTheme.typography.titleMedium,
            color = md_theme_dark_onSurface
          )
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
          IconButton(onClick = { showFilterSheet = true }) {
            Icon(
              imageVector = Icons.Filled.FilterList,
              contentDescription = "Filter",
              tint = primary_fixed_dim
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
      // Filter Chips Placeholder
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
          Spacer(modifier = Modifier.height(height = 16.dp))
          Text(text = "TODAY", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
          Spacer(modifier = Modifier.height(height = 8.dp))
        }
        item {
          TransactionItem(
            type = "Received", asset = "BTC", amount = "+0.042 BTC", fiat = "+$2,642.10",
            icon = Icons.Filled.CallReceived, color = secondary_fixed_dim,
            onClick = { onNavigateToReceipt("TX-9982") }
          )
        }
        item {
          TransactionItem(
            type = "Sent", asset = "ETH", amount = "-1.2 ETH", fiat = "-$3,842.12",
            icon = Icons.Filled.CallMade, color = md_theme_dark_onSurface,
            onClick = { onNavigateToReceipt("TX-9981") }
          )
        }
        item {
          Spacer(modifier = Modifier.height(height = 16.dp))
          Text(text = "YESTERDAY", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
          Spacer(modifier = Modifier.height(height = 8.dp))
        }
        item {
          TransactionItem(
            type = "Trade", asset = "SOL", amount = "+42.5 SOL", fiat = "+$6,142.00",
            icon = Icons.Filled.CallReceived, color = secondary_fixed_dim,
            onClick = { onNavigateToReceipt("TX-9980") }
          )
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
        color = if (isSelected) primary_fixed_dim else surface_container_low,
        shape = RoundedCornerShape(size = 16.dp)
      )
      .padding(horizontal = 16.dp, vertical = 8.dp)
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelMedium,
      color = if (isSelected) Color(color = 0xFF001F24) else md_theme_dark_onSurfaceVariant
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
          .background(color = surface_container_low, shape = CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(size = 20.dp))
      }
      Spacer(modifier = Modifier.width(width = 12.dp))
      Column {
        Text(text = "$type $asset", style = DataMedium, color = md_theme_dark_onSurface)
        Text(text = "Completed", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = md_theme_dark_onSurfaceVariant)
      }
    }
    Column(horizontalAlignment = Alignment.End) {
      Text(text = amount, style = DataMedium, color = color)
      Text(text = fiat, style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp), color = md_theme_dark_onSurfaceVariant)
    }
  }
}
