package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Tune
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun TradeScreen(
  modifier: Modifier = Modifier,
  onNavigateToSearch: () -> Unit = {}
): Unit {
  Scaffold(
    modifier = modifier.fillMaxSize(fraction = 1.0f),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "TRADE",
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
        .padding(horizontal = 16.dp)
    ) {
      Spacer(modifier = Modifier.height(height = 16.dp))
      // From Box
      Column(
        modifier = Modifier
          .fillMaxWidth(fraction = 1.0f)
          .background(color = surface_container_low, shape = RoundedCornerShape(size = 16.dp))
          .padding(all = 16.dp)
      ) {
        Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f), horizontalArrangement = Arrangement.SpaceBetween) {
          Text(text = "Pay From", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
          Text(text = "Bal: 1.2405 ETH", style = DataMedium, color = primary_fixed_dim)
        }
        Spacer(modifier = Modifier.height(height = 8.dp))
        Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
          Row(
            modifier = Modifier
              .background(color = surface_container_high, shape = RoundedCornerShape(size = 8.dp))
              .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(imageVector = Icons.Filled.CurrencyBitcoin, contentDescription = null, tint = Color.White, modifier = Modifier.size(size = 16.dp))
            Spacer(modifier = Modifier.width(width = 4.dp))
            Text(text = "ETH", style = MaterialTheme.typography.headlineMedium, color = md_theme_dark_onSurface)
            Icon(imageVector = Icons.Filled.ExpandMore, contentDescription = null, tint = md_theme_dark_onSurfaceVariant)
          }
          Column(horizontalAlignment = Alignment.End) {
            Text(text = "0.00", style = DataLarge.copy(fontSize = 40.sp), color = primary_fixed_dim)
            Text(text = "≈ $0.00", style = DataMedium, color = md_theme_dark_onSurfaceVariant)
          }
        }
      }
      
      // Swap Icon
      Box(modifier = Modifier.fillMaxWidth(fraction = 1.0f).height(height = 0.dp), contentAlignment = Alignment.Center) {
         Box(modifier = Modifier.size(size = 40.dp).background(color = surface_container_highest, shape = CircleShape), contentAlignment = Alignment.Center) {
           Icon(imageVector = Icons.Filled.SwapVert, contentDescription = "Swap", tint = primary_fixed_dim)
         }
      }

      // To Box
      Column(
        modifier = Modifier
          .fillMaxWidth(fraction = 1.0f)
          .background(color = surface_container_low, shape = RoundedCornerShape(size = 16.dp))
          .padding(all = 16.dp)
      ) {
        Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f), horizontalArrangement = Arrangement.SpaceBetween) {
          Text(text = "Receive To", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
          Text(text = "Bal: 0.00 USDC", style = DataMedium, color = secondary_fixed_dim)
        }
        Spacer(modifier = Modifier.height(height = 8.dp))
        Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
          Row(
            modifier = Modifier
              .background(color = surface_container_high, shape = RoundedCornerShape(size = 8.dp))
              .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(imageVector = Icons.Filled.AttachMoney, contentDescription = null, tint = Color.White, modifier = Modifier.size(size = 16.dp))
            Spacer(modifier = Modifier.width(width = 4.dp))
            Text(text = "USDC", style = MaterialTheme.typography.headlineMedium, color = md_theme_dark_onSurface)
            Icon(imageVector = Icons.Filled.ExpandMore, contentDescription = null, tint = md_theme_dark_onSurfaceVariant)
          }
          Column(horizontalAlignment = Alignment.End) {
            Text(text = "0.00", style = DataLarge.copy(fontSize = 40.sp), color = secondary_fixed_dim)
            Text(text = "1 ETH ≈ 3,450.12 USDC", style = DataMedium, color = md_theme_dark_onSurfaceVariant)
          }
        }
      }

      Spacer(modifier = Modifier.height(height = 16.dp))
      
      Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(imageVector = Icons.Filled.Tune, contentDescription = null, tint = md_theme_dark_onSurfaceVariant, modifier = Modifier.size(size = 18.dp))
          Spacer(modifier = Modifier.width(width = 4.dp))
          Text(text = "Slippage Tolerance", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
          Box(modifier = Modifier.background(color = surface_container_highest, shape = RoundedCornerShape(size = 4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) { Text(text = "0.1%", style = DataMedium) }
          Box(modifier = Modifier.background(color = primary_fixed_dim, shape = RoundedCornerShape(size = 4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) { Text(text = "0.5%", style = DataMedium, color = Color(color = 0xFF001F24)) }
          Box(modifier = Modifier.background(color = surface_container_highest, shape = RoundedCornerShape(size = 4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) { Text(text = "1.0%", style = DataMedium) }
        }
      }

      Spacer(modifier = Modifier.weight(weight = 1.0f))

      // Placeholder for Keypad
      Column(modifier = Modifier.fillMaxWidth(fraction = 1.0f), verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f), horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
          KeypadButton(text = "1", modifier = Modifier.weight(weight = 1.0f))
          KeypadButton(text = "2", modifier = Modifier.weight(weight = 1.0f))
          KeypadButton(text = "3", modifier = Modifier.weight(weight = 1.0f))
        }
        Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f), horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
          KeypadButton(text = "4", modifier = Modifier.weight(weight = 1.0f))
          KeypadButton(text = "5", modifier = Modifier.weight(weight = 1.0f))
          KeypadButton(text = "6", modifier = Modifier.weight(weight = 1.0f))
        }
        Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f), horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
          KeypadButton(text = "7", modifier = Modifier.weight(weight = 1.0f))
          KeypadButton(text = "8", modifier = Modifier.weight(weight = 1.0f))
          KeypadButton(text = "9", modifier = Modifier.weight(weight = 1.0f))
        }
        Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f), horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
          KeypadButton(text = ".", modifier = Modifier.weight(weight = 1.0f))
          KeypadButton(text = "0", modifier = Modifier.weight(weight = 1.0f))
          KeypadButton(text = "DEL", modifier = Modifier.weight(weight = 1.0f))
        }
      }

      Spacer(modifier = Modifier.height(height = 16.dp))

      // Swipe to Swap
      Box(
        modifier = Modifier
          .fillMaxWidth(fraction = 1.0f)
          .height(height = 56.dp)
          .background(color = surface_container_low, shape = RoundedCornerShape(size = 28.dp)),
        contentAlignment = Alignment.Center
      ) {
        Text(text = "Swipe to Swap", style = MaterialTheme.typography.labelSmall, color = primary_fixed_dim)
        Box(modifier = Modifier.align(alignment = Alignment.CenterStart).padding(start = 4.dp).size(size = 48.dp).background(color = primary_fixed_dim, shape = CircleShape), contentAlignment = Alignment.Center) {
           Icon(imageVector = Icons.Filled.DoubleArrow, contentDescription = null, tint = Color(color = 0xFF001F24))
        }
      }
      Spacer(modifier = Modifier.height(height = 24.dp))
    }
  }
}

@Composable
public fun KeypadButton(
  text: String,
  modifier: Modifier = Modifier
): Unit {
  Box(
    modifier = modifier
      .height(height = 56.dp)
      .background(color = surface_container_low, shape = RoundedCornerShape(size = 12.dp)),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text,
      style = DataLarge.copy(fontSize = 24.sp),
      color = md_theme_dark_onSurface
    )
  }
}
