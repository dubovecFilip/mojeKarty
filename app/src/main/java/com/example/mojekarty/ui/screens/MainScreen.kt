package com.example.mojekarty.ui.screens

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    context: Context,
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    var selectedCard by remember { mutableStateOf<Card?>(null) }
    var autoSaveEnabled by remember { mutableStateOf(StorageManager.loadAutoSaveEnabled(context)) }
    val snackbarHostState = remember { SnackbarHostState() }
    var editingCard by remember { mutableStateOf<Card?>(null) }

    val cards = remember {
        mutableStateListOf<Card>().apply {
            addAll(StorageManager.loadCardsFromFile(context))
        }
    }

    fun saveIfAutoEnabled() {
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

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),

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
                        cards.remove(card)
                        saveIfAutoEnabled()
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
                    cards = cards,
                    onCardClick = {
                        navController.navigate("preview/${it.id}")
                    },
                    onCardLongClick = { selectedCard = it }
                )
            }

            composable("settings") {
                SettingsScreen(
                    context = context,
                    cards = cards,
                    onAutoSaveChange = {
                        autoSaveEnabled = it
                        StorageManager.saveAutoSaveEnabled(context, it)
                        saveIfAutoEnabled()
                    },
                    onClearAll = { cards.clear() },
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    onImport = { importedCards ->
                        cards.clear()
                        cards.addAll(importedCards)
                        saveIfAutoEnabled()
                    }
                )
            }

            composable("add") {
                AddCardScreen(
                    onSave = { newCard ->
                        cards.add(newCard)
                        saveIfAutoEnabled()
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
                        val index = cards.indexOfFirst { it.id == updatedCard.id }
                        if (index != -1) cards[index] = updatedCard
                        saveIfAutoEnabled()
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
                val index = cards.indexOfFirst { it.id == cardId }
                if (index != -1) {
                    val updatedCard = cards[index]
                    LaunchedEffect(key1 = updatedCard.id) {
                        val incremented = updatedCard.copy(usedCount = updatedCard.usedCount + 1)
                        cards[index] = incremented
                        saveIfAutoEnabled()
                    }
                    PreviewCardScreen(card = cards[index], onBack = { navController.popBackStack() })
                }
            }

            composable("stats") {
                StatsScreen(cards = cards, clickCounts = cards.associate { it.id to it.usedCount })
            }

        }
    }
}
