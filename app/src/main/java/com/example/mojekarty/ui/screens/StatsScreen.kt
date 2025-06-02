package com.example.mojekarty.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mojekarty.model.Card
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material3.HorizontalDivider

@Composable
fun StatsScreen(
    cards: List<Card>,
    clickCounts: Map<Int, Int>,
    modifier: Modifier = Modifier
) {
    val sortedCards = cards.sortedByDescending { clickCounts[it.id] ?: 0 }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        sortedCards.forEach { card ->
            val count = clickCounts[card.id] ?: 0
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = card.companyName,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Vytvorená: ${formatDate(card.createdAt)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Text("$count×", style = MaterialTheme.typography.bodyLarge)
                }

                HorizontalDivider()

            }
        }
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("d. MMMM yyyy", Locale("sk"))
    return sdf.format(Date(timestamp))
}
