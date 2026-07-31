package com.example

import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkColorScheme
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.KinetikBluePrimary
import com.example.ui.theme.KinetikGreenGain
import com.example.ui.theme.KinetikRedLoss
import com.example.ui.theme.KinetikShapes
import com.example.ui.theme.LightBackground
import com.example.ui.theme.LightColorScheme
import com.example.ui.theme.LightOutline
import com.example.ui.theme.LightSurface
import com.example.ui.theme.LightSurfaceVariant
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.viewmodel.CryptoViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ThemeInfrastructureTest {

    @Test
    fun testLightAndDarkThemeTokens() {
        assertEquals(LightBackground, LightColorScheme.background)
        assertEquals(LightSurface, LightColorScheme.surface)
        assertEquals(LightSurfaceVariant, LightColorScheme.surfaceVariant)
        assertEquals(LightOutline, LightColorScheme.outline)
        assertEquals(LightTextPrimary, LightColorScheme.onBackground)
        assertEquals(LightTextSecondary, LightColorScheme.onSurfaceVariant)
        assertEquals(KinetikBluePrimary, LightColorScheme.primary)
        assertEquals(KinetikGreenGain, LightColorScheme.secondary)
        assertEquals(KinetikRedLoss, LightColorScheme.error)

        assertEquals(DarkBackground, DarkColorScheme.background)
        assertEquals(DarkSurface, DarkColorScheme.surface)
        assertEquals(DarkTextPrimary, DarkColorScheme.onBackground)
    }

    @Test
    fun testColorSchemesAndShapes() {
        assertNotNull(KinetikShapes.small)
        assertNotNull(KinetikShapes.medium)
        assertNotNull(KinetikShapes.large)
        assertNotNull(KinetikShapes.extraLarge)
    }

    @Test
    fun testReactiveDarkModeToggleInViewModel() {
        val viewModel = CryptoViewModel()
        assertFalse(viewModel.uiState.value.isDarkMode)

        viewModel.toggleDarkMode()
        assertTrue(viewModel.uiState.value.isDarkMode)

        viewModel.toggleDarkMode()
        assertFalse(viewModel.uiState.value.isDarkMode)

        viewModel.setDarkMode(true)
        assertTrue(viewModel.uiState.value.isDarkMode)

        viewModel.setDarkMode(false)
        assertFalse(viewModel.uiState.value.isDarkMode)
    }
}
