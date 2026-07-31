package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.ui.theme.KinetikTheme
import com.example.ui.navigation.AppNavGraph
import com.example.ui.navigation.BottomNavigationBar
import com.example.viewmodel.CryptoViewModel
import com.example.viewmodel.CryptoViewModelFactory

public class MainActivity : ComponentActivity() {
  private val viewModel: CryptoViewModel by viewModels { CryptoViewModelFactory(this) }

  override fun onCreate(savedInstanceState: Bundle?): Unit {
    super.onCreate(savedInstanceState = savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val uiState by viewModel.uiState.collectAsState()
      KinetikTheme(darkTheme = uiState.isDarkMode) {
        val navController: NavHostController = rememberNavController()
        Scaffold(
          modifier = Modifier.fillMaxSize(fraction = 1.0f),
          bottomBar = {
            BottomNavigationBar(navController = navController)
          }
        ) { innerPadding ->
          AppNavGraph(
            navController = navController,
            viewModel = viewModel,
            modifier = Modifier.padding(paddingValues = innerPadding)
          )
        }
      }
    }
  }
}
