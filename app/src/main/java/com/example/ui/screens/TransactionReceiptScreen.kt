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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun TransactionReceiptScreen(
  txId: String,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
): Unit {
  val context = androidx.compose.ui.platform.LocalContext.current

  Scaffold(
    modifier = modifier.fillMaxSize(fraction = 1.0f),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Transaction Receipt",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp),
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
        .padding(horizontal = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(height = 24.dp))
      
      Box(
        modifier = Modifier
          .size(size = 64.dp)
          .background(color = Color(0xFFDCFCE7), shape = CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Filled.Check,
          contentDescription = "Success",
          tint = Color(0xFF22C55E),
          modifier = Modifier.size(size = 32.dp)
        )
      }
      
      Spacer(modifier = Modifier.height(height = 16.dp))
      
      Text(
        text = "Transaction Complete",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp),
        color = Color(0xFF0F172A)
      )
      Spacer(modifier = Modifier.height(height = 4.dp))
      Text(
        text = "Dec 12, 2024 at 14:32 PM",
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 13.sp),
        color = Color(0xFF64748B)
      )
      
      Spacer(modifier = Modifier.height(height = 24.dp))
      
      Text(
        text = "+0.042 BTC",
        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp, fontWeight = FontWeight.Bold),
        color = Color(0xFF0F172A)
      )
      
      Spacer(modifier = Modifier.height(height = 32.dp))
      
      Column(
        modifier = Modifier
          .fillMaxWidth(fraction = 1.0f)
          .background(color = Color.White, shape = RoundedCornerShape(size = 24.dp))
          .border(width = 1.dp, color = Color(0xFFF1F5F9), shape = RoundedCornerShape(size = 24.dp))
          .padding(all = 24.dp)
      ) {
        ReceiptRow(label = "Type", value = "Deposit")
        Spacer(modifier = Modifier.height(height = 16.dp))
        ReceiptRow(label = "Amount", value = "0.042 BTC")
        Spacer(modifier = Modifier.height(height = 16.dp))
        ReceiptRow(label = "Network Fee", value = "0.0001 BTC")
        Spacer(modifier = Modifier.height(height = 16.dp))
        ReceiptRow(label = "Status", value = "Completed", valueColor = Color(0xFF22C55E))
        
        Spacer(modifier = Modifier.height(height = 20.dp))
        HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
        Spacer(modifier = Modifier.height(height = 20.dp))
        
        Text(
          text = "Transaction ID",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
          color = Color(0xFF64748B)
        )
        Spacer(modifier = Modifier.height(height = 8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(fraction = 1.0f),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = txId,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp, fontWeight = FontWeight.Medium),
            color = Color(0xFF1E60F9)
          )
          Icon(
            imageVector = Icons.Filled.ContentCopy,
            contentDescription = "Copy",
            tint = Color(0xFF1E60F9),
            modifier = Modifier
              .size(size = 18.dp)
              .clickable {
                val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as? android.content.ClipboardManager
                val clip = android.content.ClipData.newPlainText("Transaction ID", txId)
                clipboard?.setPrimaryClip(clip)
                android.widget.Toast.makeText(context, "Transaction ID copied", android.widget.Toast.LENGTH_SHORT).show()
              }
          )
        }
      }
      
      Spacer(modifier = Modifier.weight(weight = 1.0f))
      
      Button(
        onClick = { },
        modifier = Modifier
          .fillMaxWidth(fraction = 1.0f)
          .height(height = 56.dp)
          .border(width = 1.dp, color = Color(0xFFBFDBFE), shape = RoundedCornerShape(size = 16.dp)),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFFEFF6FF),
          contentColor = Color(0xFF1E60F9)
        ),
        shape = RoundedCornerShape(size = 16.dp)
      ) {
        Text(
          text = "VIEW ON EXPLORER",
          style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
          color = Color(0xFF1E60F9)
        )
        Spacer(modifier = Modifier.width(width = 8.dp))
        Icon(
          imageVector = Icons.AutoMirrored.Filled.OpenInNew,
          contentDescription = null,
          tint = Color(0xFF1E60F9),
          modifier = Modifier.size(size = 18.dp)
        )
      }
      
      Spacer(modifier = Modifier.height(height = 16.dp))
    }
  }
}

@Composable
public fun ReceiptRow(label: String, value: String, valueColor: Color = Color(0xFF0F172A)): Unit {
  Row(
    modifier = Modifier.fillMaxWidth(fraction = 1.0f),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.bodyMedium,
      color = Color(0xFF64748B)
    )
    Text(
      text = value,
      style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
      color = valueColor
    )
  }
}
