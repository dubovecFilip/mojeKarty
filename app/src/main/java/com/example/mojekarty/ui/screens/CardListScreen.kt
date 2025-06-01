package com.example.mojekarty.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mojekarty.model.Card
import com.example.mojekarty.ui.components.LoyaltyCardItem

@Composable
fun CardListScreen(
    cards: List<Card>,
    onCardClick: (Card) -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(cards) { card ->
                LoyaltyCardItem(card = card, onClick = { onCardClick(card) })
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}