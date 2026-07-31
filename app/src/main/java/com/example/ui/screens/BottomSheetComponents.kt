package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DataLarge
import com.example.ui.theme.DataMedium
import com.example.ui.theme.md_theme_dark_onSurface
import com.example.ui.theme.md_theme_dark_onSurfaceVariant
import com.example.ui.theme.primary_fixed_dim
import com.example.ui.theme.secondary_fixed_dim
import com.example.ui.theme.surface_container_high
import com.example.ui.theme.surface_container_low

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun OrderConfirmationSheet(
  sheetState: SheetState,
  onDismissRequest: () -> Unit,
  onConfirm: () -> Unit,
  asset: String = "BTC",
  amount: String = "0.01",
  price: String = "$64,120.32",
  total: String = "$641.20",
  payAsset: String = asset,
  receiveAsset: String = "BTC",
  payAmount: String = amount,
  receiveAmount: String = amount,
  rate: String = price,
  networkFee: String = "0.0001 BTC",
  errorMessage: String? = null
): Unit {
  ModalBottomSheet(
    onDismissRequest = onDismissRequest,
    sheetState = sheetState,
    containerColor = Color(color = 0xFF0A0A0C).copy(alpha = 0.95f), 
    scrimColor = Color.Black.copy(alpha = 0.6f)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth(fraction = 1.0f)
        .padding(all = 24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(text = "CONFIRM ORDER", style = MaterialTheme.typography.titleMedium, color = md_theme_dark_onSurface)
      Spacer(modifier = Modifier.height(height = 24.dp))

      if (errorMessage != null) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .background(color = Color(0xFF3B1216), shape = RoundedCornerShape(8.dp))
            .border(width = 1.dp, color = Color(0xFFFFB4AB), shape = RoundedCornerShape(8.dp))
            .padding(all = 12.dp)
        ) {
          Text(text = errorMessage, color = Color(0xFFFFB4AB), style = MaterialTheme.typography.bodyMedium)
        }
      }
      
      Text(text = "You are swapping", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
      Text(text = "$payAmount $payAsset → $receiveAmount $receiveAsset", style = DataLarge.copy(fontSize = 24.sp), color = primary_fixed_dim)
      
      Spacer(modifier = Modifier.height(height = 24.dp))
      
      Column(
        modifier = Modifier
          .fillMaxWidth(fraction = 1.0f)
          .background(color = surface_container_low, shape = RoundedCornerShape(size = 12.dp))
          .border(width = 1.dp, color = Color.White.copy(alpha = 0.05f), shape = RoundedCornerShape(size = 12.dp))
          .padding(all = 16.dp)
      ) {
        OrderRowContent(label = "Pay Amount", value = "$payAmount $payAsset")
        Spacer(modifier = Modifier.height(height = 12.dp))
        OrderRowContent(label = "Receive Amount", value = "$receiveAmount $receiveAsset")
        Spacer(modifier = Modifier.height(height = 12.dp))
        if (rate.isNotEmpty()) {
          OrderRowContent(label = "Exchange Rate", value = rate)
          Spacer(modifier = Modifier.height(height = 12.dp))
        }
        OrderRowContent(label = "Network Fee", value = networkFee)
        Spacer(modifier = Modifier.height(height = 12.dp))
        Box(modifier = Modifier.fillMaxWidth(fraction = 1.0f).height(height = 1.dp).background(color = Color.White.copy(alpha = 0.1f)))
        Spacer(modifier = Modifier.height(height = 12.dp))
        OrderRowContent(label = "Total Cost", value = total, valueColor = primary_fixed_dim)
      }
      
      Spacer(modifier = Modifier.height(height = 24.dp))
      
      Button(
        onClick = onConfirm,
        modifier = Modifier
          .fillMaxWidth(fraction = 1.0f)
          .height(height = 56.dp)
          .padding(bottom = 16.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = primary_fixed_dim,
          contentColor = Color(color = 0xFF001F24)
        ),
        shape = RoundedCornerShape(size = 12.dp)
      ) {
        Text(text = "SWIPE TO CONFIRM", style = MaterialTheme.typography.labelLarge)
      }
    }
  }
}

@Composable
public fun OrderRowContent(label: String, value: String, valueColor: Color = md_theme_dark_onSurface): Unit {
  Row(
    modifier = Modifier.fillMaxWidth(fraction = 1.0f),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(text = label, style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
    Text(text = value, style = DataMedium, color = valueColor)
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun FilterSortSheet(
  sheetState: SheetState,
  onDismissRequest: () -> Unit,
  onApply: () -> Unit
): Unit {
  ModalBottomSheet(
    onDismissRequest = onDismissRequest,
    sheetState = sheetState,
    containerColor = Color(color = 0xFF0A0A0C)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth(fraction = 1.0f)
        .padding(horizontal = 24.dp)
        .padding(bottom = 32.dp, top = 8.dp)
    ) {
      Text(text = "FILTER & SORT", style = MaterialTheme.typography.titleMedium, color = md_theme_dark_onSurface)
      Spacer(modifier = Modifier.height(height = 24.dp))
      
      Text(text = "Sort By", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
      Spacer(modifier = Modifier.height(height = 12.dp))
      LazyRow(horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        item { FilterChipSelection(label = "Date (Newest)", isSelected = true) }
        item { FilterChipSelection(label = "Date (Oldest)", isSelected = false) }
        item { FilterChipSelection(label = "Amount (High)", isSelected = false) }
      }
      
      Spacer(modifier = Modifier.height(height = 24.dp))
      
      Text(text = "Transaction Type", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
      Spacer(modifier = Modifier.height(height = 12.dp))
      LazyRow(horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        item { FilterChipSelection(label = "All Types", isSelected = true) }
        item { FilterChipSelection(label = "Deposits", isSelected = false) }
        item { FilterChipSelection(label = "Withdrawals", isSelected = false) }
      }
      
      Spacer(modifier = Modifier.height(height = 32.dp))
      
      Button(
        onClick = onApply,
        modifier = Modifier
          .fillMaxWidth(fraction = 1.0f)
          .height(height = 56.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = primary_fixed_dim,
          contentColor = Color(color = 0xFF001F24)
        ),
        shape = RoundedCornerShape(size = 12.dp)
      ) {
        Text(text = "APPLY FILTERS", style = MaterialTheme.typography.labelLarge)
      }
    }
  }
}

@Composable
public fun FilterChipSelection(label: String, isSelected: Boolean): Unit {
  Row(
    modifier = Modifier
      .background(
        color = if (isSelected) primary_fixed_dim.copy(alpha = 0.2f) else surface_container_low,
        shape = RoundedCornerShape(size = 16.dp)
      )
      .border(
        width = 1.dp,
        color = if (isSelected) primary_fixed_dim else Color.Transparent,
        shape = RoundedCornerShape(size = 16.dp)
      )
      .padding(horizontal = 16.dp, vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    if (isSelected) {
      Icon(imageVector = Icons.Filled.Check, contentDescription = null, tint = primary_fixed_dim, modifier = Modifier.size(size = 14.dp))
      Spacer(modifier = Modifier.width(width = 4.dp))
    }
    Text(
      text = label,
      style = MaterialTheme.typography.labelMedium,
      color = if (isSelected) primary_fixed_dim else md_theme_dark_onSurfaceVariant
    )
  }
}
