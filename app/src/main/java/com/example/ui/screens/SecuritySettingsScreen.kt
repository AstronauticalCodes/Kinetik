package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.LockPerson
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DataMedium
import com.example.ui.theme.md_theme_dark_onSurface
import com.example.ui.theme.md_theme_dark_onSurfaceVariant
import com.example.ui.theme.primary_fixed_dim
import com.example.ui.theme.surface_container_high
import com.example.ui.theme.surface_container_low

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun SecuritySettingsScreen(
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
): Unit {
  var isBiometricsEnabled by remember { mutableStateOf(value = true) }
  var isTwoFactorEnabled by remember { mutableStateOf(value = true) }

  Scaffold(
    modifier = modifier.fillMaxSize(fraction = 1.0f),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "SECURITY",
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
      
      Column(
        modifier = Modifier
          .fillMaxWidth(fraction = 1.0f)
          .background(color = surface_container_low, shape = RoundedCornerShape(size = 16.dp))
          .padding(all = 16.dp)
      ) {
        Text(text = "Authentication", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
        Spacer(modifier = Modifier.height(height = 16.dp))
        
        SettingSwitchRow(
          icon = Icons.Filled.Fingerprint,
          title = "Biometrics Login",
          subtitle = "Use fingerprint or face to login",
          checked = isBiometricsEnabled,
          onCheckedChange = { isBiometricsEnabled = it }
        )
        
        Spacer(modifier = Modifier.height(height = 16.dp))
        
        SettingSwitchRow(
          icon = Icons.Filled.LockPerson,
          title = "Two-Factor Authentication",
          subtitle = "Secure with Authenticator App",
          checked = isTwoFactorEnabled,
          onCheckedChange = { isTwoFactorEnabled = it }
        )
      }
      
      Spacer(modifier = Modifier.height(height = 16.dp))
      
      Column(
        modifier = Modifier
          .fillMaxWidth(fraction = 1.0f)
          .background(color = surface_container_low, shape = RoundedCornerShape(size = 16.dp))
          .padding(all = 16.dp)
      ) {
        Text(text = "Devices", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
        Spacer(modifier = Modifier.height(height = 16.dp))
        
        Row(
          modifier = Modifier.fillMaxWidth(fraction = 1.0f),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Filled.Devices, contentDescription = null, tint = primary_fixed_dim, modifier = Modifier.size(size = 24.dp))
            Spacer(modifier = Modifier.width(width = 16.dp))
            Column {
              Text(text = "Session Manager", style = DataMedium, color = md_theme_dark_onSurface)
              Text(text = "3 active devices", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
            }
          }
          Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = md_theme_dark_onSurfaceVariant)
        }
      }
      
      Spacer(modifier = Modifier.weight(weight = 1.0f))
      val context = androidx.compose.ui.platform.LocalContext.current
      Button(
        onClick = { 
          android.widget.Toast.makeText(context, "Account Locked for Security", android.widget.Toast.LENGTH_SHORT).show()
          (context as? android.app.Activity)?.finishAffinity()
        },
        modifier = Modifier
          .fillMaxWidth(fraction = 1.0f)
          .height(height = 56.dp)
          .padding(bottom = 24.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(color = 0x20FFB4AB),
          contentColor = Color(color = 0xFFFFB4AB)
        ),
        shape = RoundedCornerShape(size = 12.dp)
      ) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null, modifier = Modifier.size(size = 18.dp))
        Spacer(modifier = Modifier.width(width = 8.dp))
        Text(text = "EMERGENCY LOCK", style = MaterialTheme.typography.labelLarge)
      }
    }
  }
}

@Composable
public fun SettingSwitchRow(
  icon: ImageVector,
  title: String,
  subtitle: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit
): Unit {
  Row(
    modifier = Modifier.fillMaxWidth(fraction = 1.0f),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(imageVector = icon, contentDescription = null, tint = primary_fixed_dim, modifier = Modifier.size(size = 24.dp))
      Spacer(modifier = Modifier.width(width = 16.dp))
      Column {
        Text(text = title, style = DataMedium, color = md_theme_dark_onSurface)
        Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
      }
    }
    Switch(
      checked = checked,
      onCheckedChange = onCheckedChange,
      colors = SwitchDefaults.colors(
        checkedThumbColor = Color(color = 0xFF001F24),
        checkedTrackColor = primary_fixed_dim,
        uncheckedThumbColor = md_theme_dark_onSurfaceVariant,
        uncheckedTrackColor = surface_container_high
      )
    )
  }
}
