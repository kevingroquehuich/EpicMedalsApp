package com.roque.epicmedalsapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LightColorScheme = lightColorScheme(
    primary = Color(0xFFC9A227),
    onPrimary = Color(0xFF000000),
    secondary = Color(0xFF1DB954),
    onSecondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFF7F5AF0),
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFFF9F9F9),
    onBackground = Color(0xFF1A1A1A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF333333),
    error = Color(0xFFD72638)
)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFD700),
    onPrimary = Color(0xFF000000),
    secondary = Color(0xFF1DB954),
    onSecondary = Color(0xFF001B0E),
    tertiary = Color(0xFF9B6BDF),
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFF000000),
    onBackground = Color(0xFFF5F5F5),
    surface = Color(0xFF141414),
    onSurface = Color(0xFFE0E0E0),
    error = Color(0xFFFF3B3B)
)

@Composable
fun EpicMedalsAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}