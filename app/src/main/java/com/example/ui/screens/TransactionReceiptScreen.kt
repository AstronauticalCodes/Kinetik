package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
public fun TransactionReceiptScreen(
  txId: String,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
): Unit {
  Scaffold(
    modifier = modifier.fillMaxSize(fraction = 1.0f),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "RECEIPT",
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
        .padding(horizontal = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(height = 32.dp))
      
      // Status Icon
      Box(
        modifier = Modifier
          .size(size = 64.dp)
          .background(color = secondary_fixed_dim.copy(alpha = 0.2f), shape = CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Filled.Check,
          contentDescription = "Success",
          tint = secondary_fixed_dim,
          modifier = Modifier.size(size = 32.dp)
        )
      }
      
      Spacer(modifier = Modifier.height(height = 16.dp))
      
      Text(text = "Transaction Complete", style = MaterialTheme.typography.titleMedium, color = secondary_fixed_dim)
      Text(text = "Dec 12, 2024 at 14:32 PM", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
      
      Spacer(modifier = Modifier.height(height = 32.dp))
      
      Text(text = "+0.042 BTC", style = DataLarge.copy(fontSize = 32.sp), color = md_theme_dark_onSurface)
      
      Spacer(modifier = Modifier.height(height = 40.dp))
      
      // Receipt Details
      Column(
        modifier = Modifier
          .fillMaxWidth(fraction = 1.0f)
          .background(color = surface_container_low, shape = RoundedCornerShape(size = 16.dp))
          .padding(all = 24.dp)
      ) {
        ReceiptRow(label = "Type", value = "Deposit")
        Spacer(modifier = Modifier.height(height = 16.dp))
        ReceiptRow(label = "Amount", value = "0.042 BTC")
        Spacer(modifier = Modifier.height(height = 16.dp))
        ReceiptRow(label = "Network Fee", value = "0.0001 BTC")
        Spacer(modifier = Modifier.height(height = 16.dp))
        ReceiptRow(label = "Status", value = "Completed", valueColor = secondary_fixed_dim)
        
        Spacer(modifier = Modifier.height(height = 24.dp))
        Box(modifier = Modifier.fillMaxWidth(fraction = 1.0f).height(height = 1.dp).background(color = Color.White.copy(alpha = 0.1f)))
        Spacer(modifier = Modifier.height(height = 24.dp))
        
        Text(text = "Transaction ID", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
        Spacer(modifier = Modifier.height(height = 8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(fraction = 1.0f),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = txId, style = DataMedium.copy(fontSize = 12.sp), color = primary_fixed_dim)
          Icon(imageVector = Icons.Filled.ContentCopy, contentDescription = "Copy", tint = primary_fixed_dim, modifier = Modifier.size(size = 16.dp))
        }
      }
      
      Spacer(modifier = Modifier.weight(weight = 1.0f))
      
      Button(
        onClick = { /* View on Explorer */ },
        modifier = Modifier
          .fillMaxWidth(fraction = 1.0f)
          .height(height = 56.dp)
          .border(width = 1.dp, color = primary_fixed_dim.copy(alpha = 0.5f), shape = RoundedCornerShape(size = 12.dp)),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color.Transparent,
          contentColor = primary_fixed_dim
        ),
        shape = RoundedCornerShape(size = 12.dp)
      ) {
        Text(text = "VIEW ON EXPLORER", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.width(width = 8.dp))
        Icon(imageVector = Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(size = 16.dp))
      }
      
      Spacer(modifier = Modifier.height(height = 24.dp))
    }
  }
}

@Composable
public fun ReceiptRow(label: String, value: String, valueColor: Color = md_theme_dark_onSurface): Unit {
  Row(
    modifier = Modifier.fillMaxWidth(fraction = 1.0f),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(text = label, style = MaterialTheme.typography.labelMedium, color = md_theme_dark_onSurfaceVariant)
    Text(text = value, style = DataMedium, color = valueColor)
  }
}
