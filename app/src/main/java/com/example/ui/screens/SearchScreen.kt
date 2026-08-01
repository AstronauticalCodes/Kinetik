package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.collectAsState
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
import com.example.ui.components.AssetBadge
import com.example.viewmodel.CryptoViewModel

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
            placeholder = { Text("Search assets...", color = Color(0xFF94A3B8)) },
            singleLine = true,
            leadingIcon = {
              Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = Color(0xFF94A3B8)
              )
            },
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = Color(0xFFF1F5F9),
              unfocusedContainerColor = Color(0xFFF1F5F9),
              focusedBorderColor = Color(0xFF1E60F9),
              unfocusedBorderColor = Color.Transparent,
              focusedTextColor = Color(0xFF0F172A),
              unfocusedTextColor = Color(0xFF0F172A),
              cursorColor = Color(0xFF1E60F9)
            ),
            trailingIcon = {
              if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { searchQuery = "" }) {
                  Icon(Icons.Filled.Close, contentDescription = "Clear", tint = Color(0xFF64748B))
                }
              }
            }
          )
        },
        navigationIcon = {
          IconButton(onClick = onNavigateBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF0F172A))
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.Transparent
        )
      )
    },
    containerColor = Color(0xFFFAFAFA)
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(horizontal = 16.dp)
    ) {
      if (searchQuery.isEmpty()) {
        Column(
          modifier = Modifier.fillMaxSize().padding(32.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            tint = Color(0xFFCBD5E1),
            modifier = Modifier.fillMaxWidth().weight(1f)
          )
        }
      } else {
        if (filteredAssets.isEmpty()) {
          Text(
            text = "No results found for \"$searchQuery\"",
            color = Color(0xFF64748B),
            modifier = Modifier.padding(16.dp)
          )
        } else {
          Spacer(modifier = Modifier.height(16.dp))
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .background(color = Color.White, shape = RoundedCornerShape(20.dp))
              .border(width = 1.dp, color = Color(0xFFF1F5F9), shape = RoundedCornerShape(20.dp))
              .padding(vertical = 8.dp)
          ) {
            filteredAssets.forEachIndexed { index, (name, symbol) ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { onNavigateToAsset(symbol) }
                  .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                AssetBadge(symbol = symbol, size = 40.dp)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                  Text(
                    text = symbol,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF0F172A)
                  )
                  Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF64748B)
                  )
                }
              }
              if (index < filteredAssets.size - 1) {
                HorizontalDivider(
                  color = Color(0xFFF1F5F9),
                  thickness = 1.dp,
                  modifier = Modifier.padding(horizontal = 16.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}
