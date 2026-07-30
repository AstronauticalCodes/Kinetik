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
import com.example.ui.theme.CyberAssetTheme
import com.example.ui.navigation.AppNavGraph
import com.example.ui.navigation.BottomNavigationBar

public class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?): Unit {
    super.onCreate(savedInstanceState = savedInstanceState)
    enableEdgeToEdge()
    setContent {
      CyberAssetTheme {
        val navController: NavHostController = rememberNavController()
        Scaffold(
          modifier = Modifier.fillMaxSize(fraction = 1.0f),
          bottomBar = {
            BottomNavigationBar(navController = navController)
          }
        ) { innerPadding ->
          AppNavGraph(
            navController = navController,
            modifier = Modifier.padding(paddingValues = innerPadding)
          )
        }
      }
    }
  }
}
