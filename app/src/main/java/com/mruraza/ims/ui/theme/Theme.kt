package com.mruraza.ims.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = GroceryGreen,
    onPrimary = OnGroceryGreen,
    secondary = GroceryAccent,
    tertiary = GroceryGold,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant
)

private val LightColorScheme = lightColorScheme(
    primary = GroceryGreen,
    onPrimary = OnGroceryGreen,
    secondary = GroceryAccent,
    tertiary = GroceryGold,
    background = GroceryBackground,
    surface = GrocerySurface,
    surfaceVariant = GrocerySurfaceVariant
)

@Composable
fun IMSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
