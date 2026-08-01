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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.viewmodel.CryptoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun ProfileScreen(
  viewModel: CryptoViewModel = viewModel(),
  onNavigateToDeposit: () -> Unit = {},
  onNavigateToSecurity: () -> Unit = {},
  onNavigateToSearch: () -> Unit = {},
  modifier: Modifier = Modifier
): Unit {
  val uiState by viewModel.uiState.collectAsState()

  Scaffold(
    modifier = modifier.fillMaxSize(fraction = 1.0f),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF0F172A)
          )
        },
        actions = {
          IconButton(onClick = onNavigateToSearch) {
            Icon(
              imageVector = Icons.Filled.Search,
              contentDescription = "Search",
              tint = Color(0xFF0F172A)
            )
          }
          Box(
            modifier = Modifier
              .padding(end = 16.dp)
              .size(size = 36.dp)
              .clip(shape = CircleShape)
              .background(color = Color(0xFF1E60F9)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Filled.Person,
              contentDescription = "User",
              tint = Color.White,
              modifier = Modifier.size(size = 20.dp)
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
    LazyColumn(
      modifier = Modifier
        .fillMaxSize(fraction = 1.0f)
        .padding(paddingValues = innerPadding)
        .padding(horizontal = 16.dp),
    ) {
      item {
        Spacer(modifier = Modifier.height(height = 16.dp))
        
        Column(
          modifier = Modifier
            .fillMaxWidth(fraction = 1.0f)
            .background(color = Color.White, shape = RoundedCornerShape(size = 24.dp))
            .border(width = 1.dp, color = Color(0xFFF1F5F9), shape = RoundedCornerShape(size = 24.dp))
            .padding(all = 24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(size = 80.dp)
              .background(color = Color(0xFF1E60F9), shape = RoundedCornerShape(size = 20.dp)),
            contentAlignment = Alignment.Center
          ) {
             Icon(
               imageVector = Icons.Filled.Person,
               contentDescription = null,
               tint = Color.White,
               modifier = Modifier.size(size = 44.dp)
             )
             Box(
               modifier = Modifier
                 .align(alignment = Alignment.BottomEnd)
                 .background(color = Color(0xFFEFF6FF), shape = RoundedCornerShape(size = 6.dp))
                 .border(width = 1.dp, color = Color(0xFFBFDBFE), shape = RoundedCornerShape(size = 6.dp))
                 .padding(horizontal = 6.dp, vertical = 2.dp)
             ) {
               Row(verticalAlignment = Alignment.CenterVertically) {
                 Icon(
                   imageVector = Icons.Filled.Verified,
                   contentDescription = null,
                   tint = Color(0xFF1E60F9),
                   modifier = Modifier.size(size = 12.dp)
                 )
                 Spacer(modifier = Modifier.width(width = 2.dp))
                 Text(
                   text = "PRO",
                   style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                   color = Color(0xFF1E60F9)
                 )
               }
             }
          }
          Spacer(modifier = Modifier.height(height = 16.dp))
          Text(
            text = "Alex Vanguard",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF0F172A)
          )
          Spacer(modifier = Modifier.height(height = 4.dp))
          Text(
            text = "ID: 88294-TX-09",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF64748B)
          )
        }
        Spacer(modifier = Modifier.height(height = 16.dp))
      }
      item {
        Row(
          modifier = Modifier.fillMaxWidth(fraction = 1.0f),
          horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
        ) {
          Column(
            modifier = Modifier
              .weight(weight = 1.0f)
              .background(color = Color(0xFF1E60F9), shape = RoundedCornerShape(size = 16.dp))
              .clickable { onNavigateToDeposit() }
              .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(imageVector = Icons.Filled.SouthWest, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.height(height = 8.dp))
            Text(
              text = "Deposit",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
              color = Color.White
            )
          }
          Column(
            modifier = Modifier
              .weight(weight = 1.0f)
              .background(color = Color.White, shape = RoundedCornerShape(size = 16.dp))
              .border(width = 1.dp, color = Color(0xFFF1F5F9), shape = RoundedCornerShape(size = 16.dp))
              .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(imageVector = Icons.Filled.NorthEast, contentDescription = null, tint = Color(0xFF1E60F9))
            Spacer(modifier = Modifier.height(height = 8.dp))
            Text(
              text = "Withdraw",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
              color = Color(0xFF0F172A)
            )
          }
        }
        Spacer(modifier = Modifier.height(height = 12.dp))
        Row(
          modifier = Modifier.fillMaxWidth(fraction = 1.0f),
          horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
        ) {
          Column(
            modifier = Modifier
              .weight(weight = 1.0f)
              .background(color = Color.White, shape = RoundedCornerShape(size = 16.dp))
              .border(width = 1.dp, color = Color(0xFFF1F5F9), shape = RoundedCornerShape(size = 16.dp))
              .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(imageVector = Icons.Filled.GroupAdd, contentDescription = null, tint = Color(0xFF22C55E))
            Spacer(modifier = Modifier.height(height = 8.dp))
            Text(
              text = "Invite",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
              color = Color(0xFF0F172A)
            )
          }
          Column(
            modifier = Modifier
              .weight(weight = 1.0f)
              .background(color = Color.White, shape = RoundedCornerShape(size = 16.dp))
              .border(width = 1.dp, color = Color(0xFFF1F5F9), shape = RoundedCornerShape(size = 16.dp))
              .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(imageVector = Icons.Filled.SupportAgent, contentDescription = null, tint = Color(0xFF64748B))
            Spacer(modifier = Modifier.height(height = 8.dp))
            Text(
              text = "Support",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
              color = Color(0xFF0F172A)
            )
          }
        }
        Spacer(modifier = Modifier.height(height = 24.dp))
      }
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth(fraction = 1.0f)
            .background(color = Color.White, shape = RoundedCornerShape(size = 20.dp))
            .border(width = 1.dp, color = Color(0xFFF1F5F9), shape = RoundedCornerShape(size = 20.dp))
            .padding(all = 20.dp)
        ) {
          Text(
            text = "Preferences",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
            color = Color(0xFF64748B)
          )
          Spacer(modifier = Modifier.height(height = 16.dp))
          Row(
            modifier = Modifier
              .fillMaxWidth(fraction = 1.0f)
              .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Dark Mode",
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
              color = Color(0xFF0F172A)
            )
            Switch(
              checked = uiState.isDarkMode,
              onCheckedChange = { viewModel.toggleDarkMode() },
              colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF1E60F9),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFE2E8F0)
              )
            )
          }
          Spacer(modifier = Modifier.height(height = 8.dp))
          HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
          Spacer(modifier = Modifier.height(height = 12.dp))
          Row(
            modifier = Modifier
              .fillMaxWidth(fraction = 1.0f)
              .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "App Preferences",
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
              color = Color(0xFF0F172A)
            )
            Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = Color(0xFF94A3B8))
          }
          Spacer(modifier = Modifier.height(height = 4.dp))
          Row(
            modifier = Modifier
              .fillMaxWidth(fraction = 1.0f)
              .clickable { onNavigateToSecurity() }
              .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Security Settings",
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
              color = Color(0xFF0F172A)
            )
            Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = Color(0xFF94A3B8))
          }
          Spacer(modifier = Modifier.height(height = 4.dp))
          Row(
            modifier = Modifier
              .fillMaxWidth(fraction = 1.0f)
              .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Notifications",
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
              color = Color(0xFF0F172A)
            )
            Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = Color(0xFF94A3B8))
          }
          Spacer(modifier = Modifier.height(height = 4.dp))
          Row(
            modifier = Modifier
              .fillMaxWidth(fraction = 1.0f)
              .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Privacy & Safety",
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
              color = Color(0xFF0F172A)
            )
            Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = Color(0xFF94A3B8))
          }
        }
        Spacer(modifier = Modifier.height(height = 24.dp))
      }
    }
  }
}
