package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ElegantDarkColorScheme =
  darkColorScheme(
    primary = PrintPilotPrimary,
    onPrimary = PrintPilotOnPrimary,
    primaryContainer = PrintPilotPrimaryContainer,
    onPrimaryContainer = PrintPilotOnPrimaryContainer,
    secondary = PrintPilotSecondary,
    onSecondary = PrintPilotOnPrimary,
    secondaryContainer = PrintPilotSecondaryContainer,
    onSecondaryContainer = PrintPilotOnSecondaryContainer,
    tertiary = PrintPilotGold,
    onTertiary = PrintPilotGoldLight,
    tertiaryContainer = PrintPilotGoldContainer,
    onTertiaryContainer = PrintPilotGoldDark,
    background = PrintPilotBackground,
    onBackground = PrintPilotOnBackground,
    surface = PrintPilotSurface,
    onSurface = PrintPilotOnSurface,
    surfaceVariant = PrintPilotSurfaceHighest,
    onSurfaceVariant = PrintPilotOnSurfaceVariant,
    outline = PrintPilotOutline,
    outlineVariant = PrintPilotOutlineVariant,
    error = PrintPilotError,
    onError = Color(0xFF601410),
    errorContainer = PrintPilotErrorContainer,
    onErrorContainer = Color(0xFFF9DEDC)
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(colorScheme = ElegantDarkColorScheme, typography = Typography, content = content)
}


