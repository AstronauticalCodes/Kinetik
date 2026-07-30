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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Verified
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
import com.example.ui.theme.DataMedium
import com.example.ui.theme.md_theme_dark_onSurface
import com.example.ui.theme.md_theme_dark_onSurfaceVariant
import com.example.ui.theme.md_theme_dark_primaryContainer
import com.example.ui.theme.primary_fixed_dim
import com.example.ui.theme.surface_container_high
import com.example.ui.theme.surface_container_highest
import com.example.ui.theme.surface_container_low

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun ProfileScreen(
  onNavigateToDeposit: () -> Unit = {},
  onNavigateToSecurity: () -> Unit = {},
  onNavigateToSearch: () -> Unit = {},
  modifier: Modifier = Modifier
): Unit {
  Scaffold(
    modifier = modifier.fillMaxSize(fraction = 1.0f),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "PROFILE",
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
    LazyColumn(
      modifier = Modifier
        .fillMaxSize(fraction = 1.0f)
        .padding(paddingValues = innerPadding)
        .padding(horizontal = 16.dp),
    ) {
      item {
        Spacer(modifier = Modifier.height(height = 16.dp))
        // User Profile Card
        Column(
          modifier = Modifier
            .fillMaxWidth(fraction = 1.0f)
            .background(color = surface_container_high, shape = RoundedCornerShape(size = 16.dp))
            .padding(all = 24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(size = 80.dp)
              .background(color = primary_fixed_dim, shape = RoundedCornerShape(size = 16.dp)),
            contentAlignment = Alignment.Center
          ) {
             Icon(imageVector = Icons.Filled.Person, contentDescription = null, tint = Color(color = 0xFF001F24), modifier = Modifier.size(size = 40.dp))
             Box(
               modifier = Modifier
                 .align(alignment = Alignment.BottomEnd)
                 .background(color = surface_container_highest, shape = RoundedCornerShape(size = 4.dp))
                 .padding(horizontal = 4.dp, vertical = 2.dp)
             ) {
               Row(verticalAlignment = Alignment.CenterVertically) {
                 Icon(imageVector = Icons.Filled.Verified, contentDescription = null, tint = primary_fixed_dim, modifier = Modifier.size(size = 12.dp))
                 Spacer(modifier = Modifier.width(width = 2.dp))
                 Text(text = "PRO", style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp), color = primary_fixed_dim)
               }
             }
          }
          Spacer(modifier = Modifier.height(height = 16.dp))
          Text(text = "Alex Vanguard", style = MaterialTheme.typography.headlineMedium, color = md_theme_dark_onSurface)
          Text(text = "ID: 88294-TX-09", style = DataMedium, color = md_theme_dark_onSurfaceVariant)
        }
        Spacer(modifier = Modifier.height(height = 16.dp))
      }
      item {
        // Quick Action Grid
        Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f), horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
          Column(
            modifier = Modifier
              .weight(weight = 1.0f)
              .background(color = md_theme_dark_primaryContainer, shape = RoundedCornerShape(size = 12.dp))
              .clickable { onNavigateToDeposit() }
              .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(imageVector = Icons.Filled.SouthWest, contentDescription = null, tint = Color(color = 0xFF001F24))
            Spacer(modifier = Modifier.height(height = 8.dp))
            Text(text = "Deposit", style = MaterialTheme.typography.labelSmall, color = Color(color = 0xFF001F24))
          }
          Column(
            modifier = Modifier
              .weight(weight = 1.0f)
              .background(color = surface_container_high, shape = RoundedCornerShape(size = 12.dp))
              .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(imageVector = Icons.Filled.NorthEast, contentDescription = null, tint = primary_fixed_dim)
            Spacer(modifier = Modifier.height(height = 8.dp))
            Text(text = "Withdraw", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurface)
          }
        }
        Spacer(modifier = Modifier.height(height = 8.dp))
        Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f), horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
          Column(
            modifier = Modifier
              .weight(weight = 1.0f)
              .background(color = surface_container_high, shape = RoundedCornerShape(size = 12.dp))
              .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(imageVector = Icons.Filled.GroupAdd, contentDescription = null, tint = Color(color = 0xFF13FF43))
            Spacer(modifier = Modifier.height(height = 8.dp))
            Text(text = "Invite", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurface)
          }
          Column(
            modifier = Modifier
              .weight(weight = 1.0f)
              .background(color = surface_container_high, shape = RoundedCornerShape(size = 12.dp))
              .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(imageVector = Icons.Filled.SupportAgent, contentDescription = null, tint = Color(color = 0xFFFFB4AB))
            Spacer(modifier = Modifier.height(height = 8.dp))
            Text(text = "Support", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurface)
          }
        }
        Spacer(modifier = Modifier.height(height = 24.dp))
      }
      item {
        // Settings List
        Column(
          modifier = Modifier.fillMaxWidth(fraction = 1.0f).background(color = surface_container_low, shape = RoundedCornerShape(size = 16.dp)).padding(all = 16.dp)
        ) {
          Text(text = "Preferences", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
          Spacer(modifier = Modifier.height(height = 16.dp))
          Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "App Preferences", style = MaterialTheme.typography.bodyMedium, color = md_theme_dark_onSurface)
            Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = md_theme_dark_onSurfaceVariant)
          }
          Spacer(modifier = Modifier.height(height = 16.dp))
          Row(
            modifier = Modifier
              .fillMaxWidth(fraction = 1.0f)
              .clickable { onNavigateToSecurity() }
              .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "Security Settings", style = MaterialTheme.typography.bodyMedium, color = md_theme_dark_onSurface)
            Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = md_theme_dark_onSurfaceVariant)
          }
          Spacer(modifier = Modifier.height(height = 8.dp))
          Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Notifications", style = MaterialTheme.typography.bodyMedium, color = md_theme_dark_onSurface)
            Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = md_theme_dark_onSurfaceVariant)
          }
          Spacer(modifier = Modifier.height(height = 16.dp))
          Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Privacy & Safety", style = MaterialTheme.typography.bodyMedium, color = md_theme_dark_onSurface)
            Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = md_theme_dark_onSurfaceVariant)
          }
        }
        Spacer(modifier = Modifier.height(height = 24.dp))
      }
    }
  }
}

