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
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.mojekarty.R

/**
 * Obrazovka so štatistikami používania kariet.
 *
 * Zobrazuje zoznam kariet usporiadaný podľa počtu použití (stlačení),
 * spolu s dátumom vytvorenia a samotným počtom.
 *
 * @param cards Zoznam kariet
 * @param clickCounts Mapovanie id karty na počet použití
 * @param modifier Modifier pre rozloženie obrazovky (voliteľný)
 */
@Composable
fun StatsScreen(
    cards: List<Card>,
    clickCounts: Map<Int, Int>,
    modifier: Modifier = Modifier
) {
    // Karty sú usporiadané podľa počtu použití, aby boli najpoužívanejšie hore.
    val sortedCards = cards.sortedByDescending { clickCounts[it.id] ?: 0 }

    if (sortedCards.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.str_stats_empty),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    } else {
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
                                text = stringResource(R.string.str_created) + ": ${formatDate(card.createdAt)}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Text("$count x", style = MaterialTheme.typography.bodyLarge)
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

/**
 * Formátuje timestamp (čas vytvorenia karty) na čitateľný slovenský dátum.
 *
 * Používa Locate("sk"), aby sa dátum zobrazoval v slovenskom formáte ("5. jún 2025")
 *
 * @param timestamp Timestamp (čas vytvorenia karty) v milisekundách
 * @return Formátovaný dátum ako String
 */
fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("d. MMMM yyyy", Locale("sk"))
    return sdf.format(Date(timestamp))
}
