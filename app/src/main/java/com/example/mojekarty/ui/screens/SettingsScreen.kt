package com.example.mojekarty.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mojekarty.data.StorageManager
import com.example.mojekarty.model.Card
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    context: Context,
    cards: List<Card>,
    autoSave: Boolean,
    onAutoSaveChange: (Boolean) -> Unit,
    onClearAll: () -> Unit,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var autoSave by remember {
        mutableStateOf(StorageManager.loadAutoSaveEnabled(context))
    }
    var showConfirm by remember { mutableStateOf(false) }
    var localAutoSave by remember { mutableStateOf(autoSave) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Automatické ukladanie",
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = localAutoSave,
                onCheckedChange = {
                    localAutoSave = it
                    onAutoSaveChange(it)
                }
            )
        }

        if (!localAutoSave) {
            Button(
                onClick = {
                    StorageManager.saveCards(context, cards)
                    navController.navigate("cards") {
                        popUpTo("settings") { inclusive = true }
                    }
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Zmeny boli uložené")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Uložiť zmeny")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                // TODO: implementovať backup/export
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Zálohovať karty")
        }

        OutlinedButton(
            onClick = {
                // TODO: implementovať reset
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Obnoviť predvolené")
        }

        OutlinedButton(
            onClick = { showConfirm = true },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Vymazať všetky karty")
        }

        if (showConfirm) {
            AlertDialog(
                onDismissRequest = { showConfirm = false },
                title = { Text("Naozaj vymazať?") },
                text = { Text("Týmto odstrániš všetky uložené karty.") },
                confirmButton = {
                    TextButton(onClick = {
                        onClearAll()
                        StorageManager.saveCards(context, emptyList())
                        showConfirm = false

                        navController.navigate("cards") {
                            popUpTo("settings") { inclusive = true }
                        }

                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Všetky karty boli vymazané")
                        }
                    }) {
                        Text("Vymazať")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirm = false }) {
                        Text("Zrušiť")
                    }
                }
            )
        }
    }
}
