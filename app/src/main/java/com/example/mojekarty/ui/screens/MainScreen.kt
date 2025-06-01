package com.example.mojekarty.ui.screens

import android.content.Context
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
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mojekarty.data.StorageManager
import com.example.mojekarty.model.Card

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    context: Context,
    onAddClick: () -> Unit = {}
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    var selectedCard by remember { mutableStateOf<Card?>(null) }
    var autoSaveEnabled by remember {
        mutableStateOf(StorageManager.loadAutoSaveEnabled(context))
    }
    val snackbarHostState = remember { SnackbarHostState() }

    val cards = remember {
        mutableStateListOf<Card>().apply {
            addAll(StorageManager.loadCards(context))
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (currentDestination) {
                            "settings" -> "Nastavenia"
                            else -> "mojeKarty"
                        }
                    )
                },
                actions = {
                    if (currentDestination == "cards") {
                        IconButton(onClick = { navController.navigate("add") }) {
                            Icon(Icons.Default.Add, contentDescription = "Pridať kartu")
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentDestination == "cards",
                    onClick = { navController.navigate("cards") },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Karty") },
                    label = { Text("Karty") }
                )
                NavigationBarItem(
                    selected = currentDestination == "settings",
                    onClick = { navController.navigate("settings") },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Nastavenia") },
                    label = { Text("Nastavenia") }
                )
            }
        }
    ) { innerPadding ->
        selectedCard?.let { card ->
            AlertDialog(
                onDismissRequest = { selectedCard = null },
                title = { Text("Akcie") },
                text = { Text("Čo chceš spraviť s kartou ${card.companyName}?") },
                confirmButton = {
                    TextButton(onClick = {
                        // TODO: pridať upravovanie
                        selectedCard = null
                    }) {
                        Text("Upraviť")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        cards.remove(card)
                        if (autoSaveEnabled) {
                            StorageManager.saveCards(context, cards)
                        }
                        selectedCard = null
                    }) {
                        Text("Vymazať")
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
                    onCardClick = { /* otvor detail */ },
                    onCardLongClick = { selectedCard = it }
                )
            }
            composable("settings") {
                SettingsScreen(
                    context = context,
                    cards = cards,
                    autoSave = autoSaveEnabled,
                    onAutoSaveChange = {
                        autoSaveEnabled = it
                        StorageManager.saveAutoSaveEnabled(context, it)
                        if (it) {
                            StorageManager.saveCards(context, cards)
                        }
                    },
                    onClearAll = { cards.clear() },
                    navController = navController,
                    snackbarHostState = snackbarHostState
                )
            }
            composable("add") {
                AddCardScreen(
                    onSave = { newCard ->
                        cards.add(newCard)
                        if (autoSaveEnabled) {
                            StorageManager.saveCards(context, cards)
                        }
                        navController.popBackStack()
                    },
                    onCancel = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
