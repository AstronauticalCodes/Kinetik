package com.example.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ui.screens.DashboardScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
public fun AppNavGraph(
  navController: NavHostController,
  modifier: Modifier = Modifier
): Unit {
  SharedTransitionLayout {
    NavHost(
      navController = navController,
      startDestination = "dashboard",
      modifier = modifier
    ) {
      composable(route = "dashboard") {
        DashboardScreen(
          animatedVisibilityScope = this,
          sharedTransitionScope = this@SharedTransitionLayout,
          onNavigateToAsset = { symbol ->
            navController.navigate("assetDetail/$symbol")
          }
        )
      }
      composable(route = "markets") {
        com.example.ui.screens.MarketsScreen(
          animatedVisibilityScope = this,
          sharedTransitionScope = this@SharedTransitionLayout,
          onNavigateToAsset = { symbol ->
            navController.navigate("assetDetail/$symbol")
          },
          onNavigateToSearch = { navController.navigate("search") }
        )
      }
      composable(route = "portfolio") {
        com.example.ui.screens.PortfolioScreen(
          onNavigateToHistory = { navController.navigate("transactionHistory") }
        )
      }
      composable(route = "trade") {
        com.example.ui.screens.TradeScreen(
          onNavigateToSearch = { navController.navigate("search") }
        )
      }
      composable(route = "search") {
        com.example.ui.screens.SearchScreen(
          onNavigateBack = { navController.popBackStack() },
          onNavigateToAsset = { symbol ->
            navController.navigate("assetDetail/$symbol")
          }
        )
      }
      composable(route = "profile") {
        com.example.ui.screens.ProfileScreen(
          onNavigateToDeposit = { navController.navigate("deposit") },
          onNavigateToSecurity = { navController.navigate("securitySettings") },
          onNavigateToSearch = { navController.navigate("search") }
        )
      }
      composable(route = "deposit") {
        com.example.ui.screens.DepositScreen(
          onNavigateBack = { navController.popBackStack() }
        )
      }
      composable(route = "securitySettings") {
        com.example.ui.screens.SecuritySettingsScreen(
          onNavigateBack = { navController.popBackStack() }
        )
      }
      composable(route = "transactionHistory") {
        com.example.ui.screens.TransactionHistoryScreen(
          onNavigateBack = { navController.popBackStack() },
          onNavigateToReceipt = { txId -> navController.navigate("transactionReceipt/$txId") }
        )
      }
      composable(route = "transactionReceipt/{txId}") { backStackEntry ->
        val txId = backStackEntry.arguments?.getString("txId") ?: "UNKNOWN"
        com.example.ui.screens.TransactionReceiptScreen(
          txId = txId,
          onNavigateBack = { navController.popBackStack() }
        )
      }
      composable(route = "assetDetail/{symbol}") { backStackEntry ->
        val symbol = backStackEntry.arguments?.getString("symbol") ?: "BTC"
        com.example.ui.screens.AssetDetailScreen(
          symbol = symbol,
          animatedVisibilityScope = this,
          sharedTransitionScope = this@SharedTransitionLayout,
          onNavigateBack = { navController.popBackStack() }
        )
      }
    }
  }
}
