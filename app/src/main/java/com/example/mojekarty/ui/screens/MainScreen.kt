package com.example.mojekarty.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mojekarty.model.Card

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onAddClick: () -> Unit = {}
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val cards = remember { mutableStateListOf(
        Card(1, "Tesco", "1234567890", color = "#1565C0"),
        Card(2, "Lidl", "9876543210", color = "#388E3C")
    ) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("mojeKarty") },
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
        NavHost(
            navController = navController,
            startDestination = "cards",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("cards") {
                CardListScreen(cards = cards)
            }
            composable("settings") {
                Text("Nastavenia") // zatiaľ placeholder
            }
            composable("add") {
                AddCardScreen(
                    onSave = { newCard ->
                        cards.add(newCard)
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
