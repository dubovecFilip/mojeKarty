package com.example.mojekarty.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mojekarty.model.Card
import com.example.mojekarty.ui.components.LoyaltyCardItem

/**
 * Obrazovka na zobrazenie detailného náhľadu karty (preview).
 *
 * Zobrazuje kartu zväčšene cez celý displej.
 * Kliknutím kdekoľvek na obrazovku sa používateľ vráti späť.
 */
@Composable
fun PreviewCardScreen(
    card: Card,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            // Celá obrazovka funguje ako tlačidlo späť.
            .clickable { onBack() },
        contentAlignment = Alignment.Center
    ) {
        LoyaltyCardItem(
            card = card,
            modifier = Modifier
                .widthIn(max = 600.dp)
                .padding(horizontal = 16.dp),
            height = 180.dp,
            barcodeHeight = 180.dp
        )
    }
}
