package com.example.mojekarty.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Typography
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.mojekarty.R

/**
 * Globálna téma aplikácie mojeKarty.
 *
 * Nastavuje farebnú schému, typografiu a vzhľad status baru
 * podľa zvolenej svetlej/tmavej témy.
 *
 * @param useDarkTheme Zvolená téma (tmavá/svetlá)
 * @param content Obsah aplikácie, ktorý bude obalený témou
 */
@Composable
fun MojeKartyTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (useDarkTheme) DarkColorScheme else LightColorScheme

    // Nastavenie správnej farby prvkov v status bare podľa témy.
    val view = LocalView.current
    val context = LocalContext.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !useDarkTheme
        }
    }

    // Definícia vlastného písma Montserrat pre celú aplikáciu.
    val montserratFontFamily = FontFamily(
        Font(R.font.montserrat_regular, FontWeight.Normal),
        Font(R.font.montserrat_bold, FontWeight.Bold)
    )

    // Vlastná typografia aplikácie - jednotný vzhľad textov.
    val appTypography = Typography(
        bodyLarge = TextStyle(
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        titleLarge = TextStyle(
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        ),
        labelLarge = TextStyle(
            fontFamily = montserratFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = appTypography,
        shapes = Shapes(),
        content = content
    )
}