package com.example.mojekarty.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mojekarty.model.Card
import com.example.mojekarty.ui.components.LoyaltyCardItem

@Composable
fun PreviewCardScreen(
    card: Card,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
