package com.mruraza.ims.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val GroceryGreen = Color(0xFF087F5B)
val OnGroceryGreen = Color(0xFFFFFFFF)
val GroceryAccent = Color(0xFF2F9E44)
val GroceryGold = Color(0xFFE09F3E)
val GroceryBackground = Color(0xFFF7FAF8)
val GrocerySurface = Color(0xFFFFFFFF)
val GrocerySurfaceVariant = Color(0xFFE8F3EE)
val DarkBackground = Color(0xFF101714)
val DarkSurface = Color(0xFF18211D)
val DarkSurfaceVariant = Color(0xFF26342E)

val Gray01 = Color(0xFFCFD2CD)
val Gray02 = Color(0xFFFBFBF2)
val Gray03 = Color(0xFFE5E6E4)
val Gray04 = Color(0xFFA6A2A2)
val Gray05 = Color(0xFF847577)
val VeryLightGrey = Color(0xFFF5F5F5)
val Green07 = GroceryGreen
val Green08 = GroceryAccent
val Green09 = Color(0xFF228B4B)
val Green10 = Color(0xFF006B4F)

val gradients = listOf(
    Brush.linearGradient(listOf(GroceryGreen, GroceryAccent)),
    Brush.linearGradient(listOf(GroceryAccent, GroceryGold)),
    Brush.linearGradient(listOf(GrocerySurfaceVariant, GroceryBackground))
)
