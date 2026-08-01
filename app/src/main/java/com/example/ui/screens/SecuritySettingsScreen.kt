package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            text = "Security Settings",
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
        .padding(horizontal = 16.dp)
    ) {
      Spacer(modifier = Modifier.height(height = 16.dp))
      
      Column(
        modifier = Modifier
          .fillMaxWidth(fraction = 1.0f)
          .background(color = Color.White, shape = RoundedCornerShape(size = 20.dp))
          .border(width = 1.dp, color = Color(0xFFF1F5F9), shape = RoundedCornerShape(size = 20.dp))
          .padding(all = 20.dp)
      ) {
        Text(
          text = "Authentication",
          style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
          color = Color(0xFF64748B)
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        
        SettingSwitchRow(
          icon = Icons.Filled.Fingerprint,
          title = "Biometrics Login",
          subtitle = "Use fingerprint or face to login",
          checked = isBiometricsEnabled,
          onCheckedChange = { isBiometricsEnabled = it }
        )
        
        Spacer(modifier = Modifier.height(height = 12.dp))
        HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
        Spacer(modifier = Modifier.height(height = 12.dp))
        
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
          .background(color = Color.White, shape = RoundedCornerShape(size = 20.dp))
          .border(width = 1.dp, color = Color(0xFFF1F5F9), shape = RoundedCornerShape(size = 20.dp))
          .padding(all = 20.dp)
      ) {
        Text(
          text = "Devices",
          style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
          color = Color(0xFF64748B)
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        
        Row(
          modifier = Modifier.fillMaxWidth(fraction = 1.0f),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Filled.Devices,
              contentDescription = null,
              tint = Color(0xFF1E60F9),
              modifier = Modifier.size(size = 24.dp)
            )
            Spacer(modifier = Modifier.width(width = 16.dp))
            Column {
              Text(
                text = "Session Manager",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF0F172A)
              )
              Spacer(modifier = Modifier.height(height = 2.dp))
              Text(
                text = "3 active devices",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF64748B)
              )
            }
          }
          Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = Color(0xFF94A3B8)
          )
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
          .padding(bottom = 16.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFFFEF2F2),
          contentColor = Color(0xFFEF4444)
        ),
        shape = RoundedCornerShape(size = 16.dp)
      ) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(size = 18.dp))
        Spacer(modifier = Modifier.width(width = 8.dp))
        Text(
          text = "EMERGENCY LOCK",
          style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
          color = Color(0xFFEF4444)
        )
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
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = Color(0xFF1E60F9),
        modifier = Modifier.size(size = 24.dp)
      )
      Spacer(modifier = Modifier.width(width = 16.dp))
      Column {
        Text(
          text = title,
          style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
          color = Color(0xFF0F172A)
        )
        Spacer(modifier = Modifier.height(height = 2.dp))
        Text(
          text = subtitle,
          style = MaterialTheme.typography.labelSmall,
          color = Color(0xFF64748B)
        )
      }
    }
    Switch(
      checked = checked,
      onCheckedChange = onCheckedChange,
      colors = SwitchDefaults.colors(
        checkedThumbColor = Color.White,
        checkedTrackColor = Color(0xFF1E60F9),
        uncheckedThumbColor = Color.White,
        uncheckedTrackColor = Color(0xFFE2E8F0)
      )
    )
  }
}
