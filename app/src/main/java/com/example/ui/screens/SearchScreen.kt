package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.viewmodel.CryptoViewModel
import com.example.ui.theme.primary_fixed_dim

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun SearchScreen(
  onNavigateBack: () -> Unit,
  onNavigateToAsset: (String) -> Unit,
  viewModel: CryptoViewModel = viewModel()
): Unit {
  var searchQuery by remember { mutableStateOf("") }
  
  val state by viewModel.state.collectAsState()
  
  val sampleAssets = state.tickers.map { 
    Pair(it.symbol.replace("USDT", ""), it.symbol.replace("USDT", ""))
  }
  
  val filteredAssets = if (searchQuery.isBlank()) {
    emptyList()
  } else {
    sampleAssets.filter { it.first.contains(searchQuery, ignoreCase = true) || it.second.contains(searchQuery, ignoreCase = true) }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search assets...", color = Color.White.copy(alpha = 0.5f)) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = Color.Transparent,
              unfocusedContainerColor = Color.Transparent,
              focusedBorderColor = Color.Transparent,
              unfocusedBorderColor = Color.Transparent,
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White,
              cursorColor = primary_fixed_dim
            ),
            trailingIcon = {
              if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { searchQuery = "" }) {
                  Icon(Icons.Filled.Close, contentDescription = "Clear", tint = Color.White.copy(alpha = 0.5f))
                }
              }
            }
          )
        },
        navigationIcon = {
          IconButton(onClick = onNavigateBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color(0xFF0A0A0C)
        )
      )
    },
    containerColor = Color(0xFF0A0A0C)
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      if (searchQuery.isEmpty()) {
        Column(
          modifier = Modifier.fillMaxSize().padding(32.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(Icons.Filled.Search, contentDescription = null, tint = Color.White.copy(alpha = 0.1f), modifier = Modifier.fillMaxWidth().weight(1f))
        }
      } else {
        if (filteredAssets.isEmpty()) {
          Text(
            text = "No results found for \"$searchQuery\"",
            color = Color.White.copy(alpha = 0.5f),
            modifier = Modifier.padding(16.dp)
          )
        } else {
          filteredAssets.forEach { (name, symbol) ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToAsset(symbol) }
                .padding(horizontal = 16.dp, vertical = 16.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = symbol, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
              Spacer(modifier = Modifier.width(16.dp))
              Text(text = name, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.6f))
            }
          }
        }
      }
    }
  }
}
