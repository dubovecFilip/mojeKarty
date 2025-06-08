package com.example.mojekarty.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Svetlá farebná shcéma aplikácie.
 *
 * Definuje primárne, sekundárne a pozadie farby,
 * ktoré nadobúdajú Material 3 komponenty.
 */
val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1565C0),    // Hlavná farba (napr. tlačídlá)
    onPrimary = Color.White,        // Farba textu na primárnej farbe
    secondary = Color(0xFF64B5F6),  // Sekundárna farba
    background = Color.White,       // Farba pozadia aplikácie
    onBackground = Color.Black,     // Farba textu na pozadí
    surface = Color(0xFFE7E7E7)     // Farba povrchov (napr. karty, dialógy)
)

/**
 * Tmavá farebná shcéma aplikácie.
 *
 * Používaná v prípade zapnutého tmavého režimu.
 */
val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = Color.Black,
    secondary = Color(0xFF42A5F5),
    background = Color(0xFF212121),
    onBackground = Color.White,
    surface = Color(0xFF101010)
)