package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

public val ArchivoNarrow: FontFamily = FontFamily.SansSerif
public val HankenGrotesk: FontFamily = FontFamily.SansSerif
public val JetBrainsMono: FontFamily = FontFamily.Monospace

public val AppTypography: Typography = Typography(
  displayLarge = TextStyle(
    fontFamily = ArchivoNarrow,
    fontWeight = FontWeight.Bold,
    fontSize = 48.sp,
    lineHeight = 52.8.sp,
    letterSpacing = (-0.96).sp
  ),
  headlineMedium = TextStyle(
    fontFamily = ArchivoNarrow,
    fontWeight = FontWeight.SemiBold,
    fontSize = 24.sp,
    lineHeight = 28.8.sp,
    letterSpacing = 0.24.sp
  ),
  bodyMedium = TextStyle(
    fontFamily = HankenGrotesk,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.sp
  ),
  labelSmall = TextStyle(
    fontFamily = ArchivoNarrow,
    fontWeight = FontWeight.Bold,
    fontSize = 12.sp,
    lineHeight = 12.sp,
    letterSpacing = 1.2.sp
  )
)

public val DataLarge: TextStyle = TextStyle(
  fontFamily = JetBrainsMono,
  fontWeight = FontWeight.SemiBold,
  fontSize = 20.sp,
  lineHeight = 24.sp,
  letterSpacing = (-0.2).sp
)

public val DataMedium: TextStyle = TextStyle(
  fontFamily = JetBrainsMono,
  fontWeight = FontWeight.Medium,
  fontSize = 14.sp,
  lineHeight = 16.8.sp,
  letterSpacing = 0.sp
)
