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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.systemBars
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import com.example.mojekarty.R


@Composable
fun CardListScreen(
    cards: List<Card>,
    onCardClick: (Card) -> Unit = {},
    onCardLongClick: (Card) -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val columnCount = if (isLandscape) 2 else 1
    val horizontalPadding = if (isLandscape) {
        WindowInsets.systemBars.asPaddingValues().calculateLeftPadding(LayoutDirection.Ltr) + 32.dp
    } else {
        0.dp
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (cards.isEmpty()) {
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