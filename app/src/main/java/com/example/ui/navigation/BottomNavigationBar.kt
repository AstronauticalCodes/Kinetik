package com.example.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ui.theme.primary_fixed_dim

public sealed class BottomNavItem(
  public val route: String,
  public val title: String,
  public val icon: ImageVector
) {
  public object Dashboard : BottomNavItem(route = "dashboard", title = "HOME", icon = Icons.Filled.Dashboard)
  public object Markets : BottomNavItem(route = "markets", title = "MARKET", icon = Icons.Filled.ShowChart)
  public object Portfolio : BottomNavItem(route = "portfolio", title = "ASSETS", icon = Icons.Filled.AccountBalanceWallet)
  public object Trade : BottomNavItem(route = "trade", title = "TRADE", icon = Icons.Filled.SwapHoriz)
  public object Profile : BottomNavItem(route = "profile", title = "PROFILE", icon = Icons.Filled.Person)
}

@Composable
public fun BottomNavigationBar(
  navController: NavHostController,
  modifier: Modifier = Modifier
): Unit {
  val items: List<BottomNavItem> = listOf(
    BottomNavItem.Dashboard,
    BottomNavItem.Markets,
    BottomNavItem.Trade,
    BottomNavItem.Portfolio,
    BottomNavItem.Profile
  )

  val navBackStackEntry: androidx.navigation.NavBackStackEntry? by navController.currentBackStackEntryAsState()
  val currentDestination: NavDestination? = navBackStackEntry?.destination

  Box(
    modifier = modifier
      .fillMaxWidth(fraction = 1.0f)
      .background(color = Color(color = 0xFF0A0A0C))
      .border(width = 1.dp, color = Color.White.copy(alpha = 0.1f))
      .windowInsetsPadding(insets = WindowInsets.navigationBars)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth(fraction = 1.0f)
        .padding(horizontal = 24.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      items.forEach { item ->
        val selected: Boolean = currentDestination?.hierarchy?.any { it.route == item.route } == true
        
        if (item == BottomNavItem.Trade) {
          Box(
            modifier = Modifier
              .offset(y = (-16).dp)
              .size(size = 48.dp)
              .shadow(elevation = 20.dp, shape = CircleShape, spotColor = primary_fixed_dim, ambientColor = primary_fixed_dim)
              .background(color = primary_fixed_dim, shape = CircleShape)
              .clickable {
                navController.navigate(route = item.route) {
                  popUpTo(id = navController.graph.findStartDestination().id) {
                    saveState = true
                  }
                  launchSingleTop = true
                  restoreState = true
                }
              },
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = item.icon,
              contentDescription = item.title,
              tint = Color(color = 0xFF0A0A0C),
              modifier = Modifier.size(size = 28.dp)
            )
          }
        } else {
          Column(
            modifier = Modifier
              .clickable {
                navController.navigate(route = item.route) {
                  popUpTo(id = navController.graph.findStartDestination().id) {
                    saveState = true
                  }
                  launchSingleTop = true
                  restoreState = true
                }
              },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = item.icon,
              contentDescription = item.title,
              tint = if (selected) primary_fixed_dim else Color.White.copy(alpha = 0.4f),
              modifier = Modifier.size(size = 24.dp)
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
            Text(
              text = item.title,
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                letterSpacing = (-0.5).sp,
                fontWeight = FontWeight.Bold
              ),
              color = if (selected) primary_fixed_dim else Color.White.copy(alpha = 0.4f)
            )
          }
        }
      }
    }
  }
}
