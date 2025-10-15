package com.roque.epicmedalsapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val LightColorScheme = lightColorScheme(
    primary = Color(0xFFC9A227),      // Oro elegante (botones principales)
    onPrimary = Color(0xFF000000),    // Texto sobre dorado
    secondary = Color(0xFF1DB954),    // Verde esmeralda (acento)
    onSecondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFF7F5AF0),     // Morado para resaltar membresías VIP
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFFF9F9F9),   // Fondo claro general
    onBackground = Color(0xFF1A1A1A), // Texto principal
    surface = Color(0xFFFFFFFF),      // Tarjetas, modales
    onSurface = Color(0xFF333333),
    error = Color(0xFFD72638)         // Rojo pérdida
)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFD700),      // Dorado brillante principal
    onPrimary = Color(0xFF000000),    // Texto sobre dorado
    secondary = Color(0xFF1DB954),    // Verde esmeralda (ganancia)
    onSecondary = Color(0xFF001B0E),
    tertiary = Color(0xFF9B6BDF),     // Morado elegante (VIP)
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFF000000),   // Fondo general oscuro
    onBackground = Color(0xFFF5F5F5),
    surface = Color(0xFF141414),      // Tarjetas, paneles, bottom bar
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