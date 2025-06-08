package com.example.mojekarty.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mojekarty.data.StorageManager
import com.example.mojekarty.model.Card
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import com.example.mojekarty.R
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mojekarty.ui.viewmodel.CardListViewModel

/**
 * Hlavná obrazovka aplikácie.
 *
 * Obsahuje navigačný scaffold, top bar, bottom navigation
 * a NavHost pre prepínanie obrazoviek.
 *
 * Riadi zobrazovanie zoznamu kariet, pridávanie, editovanie,
 * náhľad karty, nastavenia a štatistiky.
 *
 * @param context Kontext aplikácie (potrebný na prístup k StorageManager)
 * @param navController Navigačný controller pre prepínanie obrazoviek
 */
@ExperimentalMaterial3Api
@Composable
fun MainScreen(
    context: Context,
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    // Vybraná karta (pri long-press) - pre zobrazenie AlertDialogu.
    var selectedCard by remember { mutableStateOf<Card?>(null) }

    // Nastavenie automatického ukladania - načíta sa pri spustení.
    var autoSaveEnabled by remember { mutableStateOf(StorageManager.loadAutoSaveEnabled(context)) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Karta, ktorú používateľ práve edituje (pri kliknutí na "Upraviť").
    var editingCard by remember { mutableStateOf<Card?>(null) }

    val cardListViewModel: CardListViewModel = viewModel()
    val cardsState = cardListViewModel.cards.collectAsState().value

    /**
     * Uloží karty do súboru ak je povolené automatické ukladanie.
     */
    fun saveIfAutoEnabled(cards: List<Card>) {
        if (autoSaveEnabled) {
            StorageManager.saveCardsToFile(context, cards)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },

        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Icon(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = stringResource(R.string.str_logo),
                            modifier = Modifier
                                .size(50.dp)
                                .padding(end = 8.dp)
                        )

                        Text(
                            when (currentDestination) {
                                "settings" -> stringResource(R.string.str_settings)
                                "add"-> stringResource(R.string.str_add_card)
                                "edit" -> stringResource(R.string.str_edit_card)
                                else -> stringResource(R.string.app_name)
                            }
                        )

                    }
                },

                actions = {
                    if (currentDestination == "cards") {
                        IconButton(onClick = { navController.navigate("add") }) {
                            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.str_add_card))
                        }
                    }
                }

            )
        },
        bottomBar = {
            NavigationBar (
                containerColor = MaterialTheme.colorScheme.surface
            ){

                NavigationBarItem(
                    selected = currentDestination == "cards",
                    onClick = { navController.navigate("cards") },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = stringResource(R.string.str_cards)) },
                    label = { Text(stringResource(R.string.str_cards)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    )
                )

                NavigationBarItem(
                    selected = currentDestination == "settings",
                    onClick = { navController.navigate("settings") },
                    icon = { Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.str_settings)) },
                    label = { Text(stringResource(R.string.str_settings)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    )
                )

            }
        }
    ) { innerPadding ->
        selectedCard?.let { card ->

            // AlertDialog - zobrazuje sa po long-press sna kartu.
            AlertDialog(
                onDismissRequest = { selectedCard = null },
                title = { Text(stringResource(R.string.str_actions)) },
                text = { Text(stringResource(R.string.str_card_menu) + " ${card.companyName}?") },

                confirmButton = {
                    TextButton(onClick = {
                        editingCard = selectedCard
                        selectedCard = null
                        navController.navigate("edit")
                    }) {
                        Text(stringResource(R.string.str_edit))
                    }
                },

                dismissButton = {
                    TextButton(onClick = {
                        cardListViewModel.removeCard(card)
                        saveIfAutoEnabled(cardListViewModel.cards.value)
                        selectedCard = null
                    }) {
                        Text(stringResource(R.string.str_delete))
                    }
                }
            )
        }

        NavHost(
            navController = navController,
            startDestination = "cards",
            modifier = Modifier.padding(innerPadding)
        ) {

            composable("cards") {
                CardListScreen(
                    cards = cardsState,
                    onCardClick = {
                        navController.navigate("preview/${it.id}")
                    },
                    onCardLongClick = { selectedCard = it }
                )
            }

            composable("settings") {
                SettingsScreen(
                    context = context,
                    cards = cardsState,
                    autoSaveEnabled = autoSaveEnabled,
                    onAutoSaveChange = {
                        autoSaveEnabled = it
                        cardListViewModel.autoSaveEnabled = it
                        saveIfAutoEnabled(cardListViewModel.cards.value)
                        StorageManager.saveAutoSaveEnabled(context, it)
                    },
                    onClearAll = {
                        cardsState.forEach { cardListViewModel.removeCard(it) }
                    },
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    onImport = { importedCards ->
                        importedCards.forEach { cardListViewModel.addCard(it) }
                        saveIfAutoEnabled(cardListViewModel.cards.value)
                    },
                    cardListViewModel = cardListViewModel
                )
            }

            composable("add") {
                AddCardScreen(
                    onSave = { newCard ->
                        cardListViewModel.addCard(newCard)
                        saveIfAutoEnabled(cardListViewModel.cards.value)
                        navController.popBackStack()
                    },
                    onCancel = {
                        navController.popBackStack()
                    }
                )
            }

            composable("edit") {
                AddCardScreen(
                    initialCard = editingCard,
                    onSave = { updatedCard ->
                        cardListViewModel.updateCard(updatedCard)
                        saveIfAutoEnabled(cardListViewModel.cards.value)
                        editingCard = null
                        navController.popBackStack()
                    },
                    onCancel = {
                        editingCard = null
                        navController.popBackStack()
                    }
                )
            }

            composable("preview/{cardId}") { backStackEntry ->
                val cardId = backStackEntry.arguments?.getString("cardId")?.toIntOrNull()
                val card = cardsState.find { it.id == cardId }
                if (card != null) {
                    // LaunchedEffect - inkrementuje počet použití karty po jej zobrazení
                    LaunchedEffect(key1 = card.id) {
                        val incremented = card.copy(usedCount = card.usedCount + 1)
                        cardListViewModel.updateCard(incremented)
                        saveIfAutoEnabled(cardListViewModel.cards.value)
                    }
                    PreviewCardScreen(card = card, onBack = { navController.popBackStack() })
                }
            }

            composable("stats") {
                StatsScreen(cards = cardsState, clickCounts = cardsState.associate { it.id to it.usedCount })
            }

        }
    }
}
