package com.mdclicker.shift.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Indigo500,
    onPrimary = TextOnPrimary,
    primaryContainer = Indigo700,
    onPrimaryContainer = Indigo50,
    secondary = Amber500,
    onSecondary = TextPrimary,
    secondaryContainer = Amber700,
    background = DarkBackground,
    onBackground = TextOnPrimary,
    surface = DarkSurface,
    onSurface = TextOnPrimary,
    surfaceVariant = DarkSurfaceVariant,
    error = Red500,
    onError = TextOnPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = Indigo800,
    onPrimary = TextOnPrimary,
    primaryContainer = Indigo100,
    onPrimaryContainer = Indigo900,
    secondary = Amber600,
    onSecondary = TextPrimary,
    secondaryContainer = Amber500,
    background = LightBackground,
    onBackground = TextPrimary,
    surface = LightSurface,
    onSurface = TextPrimary,
    surfaceVariant = Indigo50,
    error = Red700,
    onError = TextOnPrimary
)

@Composable
fun MDClickerShiftTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
