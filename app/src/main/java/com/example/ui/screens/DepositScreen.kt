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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DataMedium
import com.example.ui.theme.md_theme_dark_onSurface
import com.example.ui.theme.md_theme_dark_onSurfaceVariant
import com.example.ui.theme.primary_fixed_dim
import com.example.ui.theme.surface_container_high
import com.example.ui.theme.surface_container_low

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun DepositScreen(
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
): Unit {
  Scaffold(
    modifier = modifier.fillMaxSize(fraction = 1.0f),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "DEPOSIT",
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
        .padding(horizontal = 16.dp)
    ) {
      Spacer(modifier = Modifier.height(height = 16.dp))
      
      // Network Selector Placeholder
      Column(
        modifier = Modifier
          .fillMaxWidth(fraction = 1.0f)
          .background(color = surface_container_high.copy(alpha = 0.5f), shape = RoundedCornerShape(size = 12.dp))
          .border(width = 1.dp, color = Color.White.copy(alpha = 0.05f), shape = RoundedCornerShape(size = 12.dp))
          .padding(all = 16.dp)
      ) {
        Text(text = "SELECT NETWORK", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
        Spacer(modifier = Modifier.height(height = 8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(fraction = 1.0f),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Ethereum (ERC20)", style = DataMedium.copy(fontSize = 16.sp), color = md_theme_dark_onSurface)
          Text(text = "CHANGE", style = MaterialTheme.typography.labelSmall, color = primary_fixed_dim)
        }
      }
      
      Spacer(modifier = Modifier.height(height = 32.dp))

      // QR Code
      Box(
        modifier = Modifier
          .fillMaxWidth(fraction = 1.0f)
          .background(color = Color.White, shape = RoundedCornerShape(size = 16.dp))
          .padding(all = 32.dp),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Filled.QrCode2,
          contentDescription = "QR Code",
          tint = Color.Black,
          modifier = Modifier.size(size = 200.dp)
        )
      }
      
      Spacer(modifier = Modifier.height(height = 32.dp))

      // Wallet Address
      Text(text = "WALLET ADDRESS", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
      Spacer(modifier = Modifier.height(height = 8.dp))
      Row(
        modifier = Modifier
          .fillMaxWidth(fraction = 1.0f)
          .background(color = surface_container_low.copy(alpha = 0.8f), shape = RoundedCornerShape(size = 12.dp))
          .border(width = 1.dp, color = primary_fixed_dim.copy(alpha = 0.3f), shape = RoundedCornerShape(size = 12.dp))
          .padding(all = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        val context = androidx.compose.ui.platform.LocalContext.current
        val depositAddress = "0x8920...3B8c"
        Text(
          text = depositAddress,
          style = DataMedium.copy(fontSize = 16.sp),
          color = md_theme_dark_onSurface
        )
        Row(horizontalArrangement = Arrangement.spacedBy(space = 16.dp)) {
          Icon(
            imageVector = Icons.Filled.ContentCopy,
            contentDescription = "Copy",
            tint = primary_fixed_dim,
            modifier = Modifier
              .size(size = 20.dp)
              .clickable {
                val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as? android.content.ClipboardManager
                val clip = android.content.ClipData.newPlainText("Deposit Address", depositAddress)
                clipboard?.setPrimaryClip(clip)
                android.widget.Toast.makeText(context, "Address copied to clipboard", android.widget.Toast.LENGTH_SHORT).show()
              }
          )
          Icon(
            imageVector = Icons.Filled.Share,
            contentDescription = "Share",
            tint = md_theme_dark_onSurfaceVariant,
            modifier = Modifier
              .size(size = 20.dp)
              .clickable {
                val intent = android.content.Intent().apply {
                  action = android.content.Intent.ACTION_SEND
                  putExtra(android.content.Intent.EXTRA_TEXT, depositAddress)
                  type = "text/plain"
                }
                context.startActivity(android.content.Intent.createChooser(intent, "Share Deposit Address"))
              }
          )
        }
      }
      
      Spacer(modifier = Modifier.height(height = 16.dp))
      
      Text(
        text = "Send only ETH to this deposit address. Sending coin or token other than ETH to this address may result in the loss of your deposit.",
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
        color = Color(color = 0xFFFFB4AB),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(fraction = 1.0f)
      )
      
      Spacer(modifier = Modifier.weight(weight = 1.0f))
      
      Button(
        onClick = onNavigateBack,
        modifier = Modifier
          .fillMaxWidth(fraction = 1.0f)
          .height(height = 56.dp)
          .padding(bottom = 24.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = primary_fixed_dim,
          contentColor = Color(color = 0xFF001F24)
        ),
        shape = RoundedCornerShape(size = 12.dp)
      ) {
        Text(text = "DONE", style = MaterialTheme.typography.labelLarge)
      }
    }
  }
}
