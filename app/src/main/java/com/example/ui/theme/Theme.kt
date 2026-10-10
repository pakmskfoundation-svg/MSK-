package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = MskElectricBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1E3A8A),
    onPrimaryContainer = Color(0xFFDBEAFE),
    secondary = MskCyanBright,
    onSecondary = Color(0xFF042F2E),
    secondaryContainer = Color(0xFF0E3A47),
    onSecondaryContainer = Color(0xFFCFFAFE),
    tertiary = MskNeonViolet,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF3B1D70),
    onTertiaryContainer = Color(0xFFEDE9FE),
    background = MskMidnightNavy,
    onBackground = MskTextPrimaryDark,
    surface = MskObsidianSurface,
    onSurface = MskTextPrimaryDark,
    surfaceVariant = MskElevatedGlass,
    onSurfaceVariant = MskTextSecondaryDark,
    outline = MskCardBorderDark,
    error = MskCoralError,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = MskPrimaryLight,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBEAFE),
    onPrimaryContainer = Color(0xFF1E3A8A),
    secondary = MskSecondaryLight,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCFFAFE),
    onSecondaryContainer = Color(0xFF164E63),
    tertiary = MskTertiaryLight,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFEDE9FE),
    onTertiaryContainer = Color(0xFF4C1D95),
    background = MskLightBackground,
    onBackground = MskTextPrimaryLight,
    surface = MskLightSurface,
    onSurface = MskTextPrimaryLight,
    surfaceVariant = MskLightElevated,
    onSurfaceVariant = MskTextSecondaryLight,
    outline = MskCardBorderLight,
    error = Color(0xFFDC2626),
    onError = Color.White
)

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}
