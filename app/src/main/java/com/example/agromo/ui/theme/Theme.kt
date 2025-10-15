package com.example.agromo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2E7D32),     // Verde Agromo
    onPrimary = Color.White,
    secondary = Color(0xFF81C784),   // Verde claro
    tertiary = Color(0xFFA5D6A7),
    background = Color(0xFFF5F5F5),
    surface = Color.White,
    onSurface = Color.Black
)

@Composable
fun AgromoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,  // 🔒 siempre modo claro
        typography = Typography,
        content = content
    )
}
