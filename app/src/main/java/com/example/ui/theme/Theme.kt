package com.example.ui.theme

import android.app.Activity
import android.view.Window
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

public val LightColorScheme: ColorScheme = lightColorScheme(
  primary = KinetikBluePrimary,
  onPrimary = Color.White,
  primaryContainer = LightSurfaceVariant,
  onPrimaryContainer = LightTextPrimary,
  secondary = KinetikGreenGain,
  onSecondary = Color.White,
  background = LightBackground,
  onBackground = LightTextPrimary,
  surface = LightSurface,
  onSurface = LightTextPrimary,
  surfaceVariant = LightSurfaceVariant,
  onSurfaceVariant = LightTextSecondary,
  outline = LightOutline,
  error = KinetikRedLoss,
  onError = Color.White
)

public val DarkColorScheme: ColorScheme = darkColorScheme(
  primary = KinetikBluePrimary,
  onPrimary = Color.White,
  primaryContainer = DarkSurfaceVariant,
  onPrimaryContainer = DarkTextPrimary,
  secondary = KinetikGreenGain,
  onSecondary = Color.White,
  background = DarkBackground,
  onBackground = DarkTextPrimary,
  surface = DarkSurface,
  onSurface = DarkTextPrimary,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = DarkTextSecondary,
  outline = DarkOutline,
  error = KinetikRedLoss,
  onError = Color.White
)

@Composable
public fun KinetikTheme(
  darkTheme: Boolean = false,
  content: @Composable () -> Unit
): Unit {
  val colorScheme: ColorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  val view: android.view.View = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window: Window = (view.context as Activity).window
      window.statusBarColor = colorScheme.background.toArgb()
      window.navigationBarColor = colorScheme.background.toArgb()
      val insetsController: WindowInsetsControllerCompat = WindowCompat.getInsetsController(window, view)
      insetsController.isAppearanceLightStatusBars = !darkTheme
      insetsController.isAppearanceLightNavigationBars = !darkTheme
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    shapes = KinetikShapes,
    typography = AppTypography,
    content = content
  )
}

@Composable
public fun CyberAssetTheme(
  darkTheme: Boolean = false,
  content: @Composable () -> Unit
): Unit {
  KinetikTheme(darkTheme = darkTheme, content = content)
}

