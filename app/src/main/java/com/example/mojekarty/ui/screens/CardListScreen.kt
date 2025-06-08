package com.example.mojekarty.ui.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.mojekarty.model.Card
import com.example.mojekarty.ui.components.LoyaltyCardItem
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration
import androidx.compose.ui.res.stringResource
import com.example.mojekarty.R

/**
 * Obrazovka so zoznamom uložených kariet.
 *
 * Ak je zoznam prázdny, zobrazí sa informácia pre používateľa.
 * Ak sú karty dostupném zobrazia sa v mriežke (1 stĺpec v portrait,
 * 2 stĺpce v landscape móde).
 *
 * @param cards Zoznam kariet na zobrazenie
 * @param onCardClick Callback pri kliknutí na kartu
 * @param onCardLongClick Callback pri dlhom stlačení karty
 */
@Composable
fun CardListScreen(
    cards: List<Card>,
    onCardClick: (Card) -> Unit = {},
    onCardLongClick: (Card) -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    // Určenie, či je zatiadenie v landscape režime.
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    // Počet stĺpcov v LazyVerticalGrid v závislosti od orientácie.
    val columnCount = if (isLandscape) 2 else 1
    // V landscape móde pridávame horizonálny padding (camera notch).
    val horizontalPadding = if (isLandscape) {
        32.dp
    } else {
        0.dp
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (cards.isEmpty()) {
            // Empty state - žiadne karty nie sú uložené.
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.str_start_first),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.str_start_first_desc),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            // LazyVerticalGrid - dynamický zoznam kariet.
            LazyVerticalGrid(
                columns = GridCells.Fixed(columnCount),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = horizontalPadding)
            ) {
                items(cards) { card ->
                    // Obalenie karty pointerInput modifierom,
                    // aby sme mohli reagovať na kliknutie a podržanie.
                    LoyaltyCardItem(
                        card = card,
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = { onCardClick(card) },
                                    onLongPress = { onCardLongClick(card) }
                                )
                            }
                    )
                }
            }
        }
    }
}