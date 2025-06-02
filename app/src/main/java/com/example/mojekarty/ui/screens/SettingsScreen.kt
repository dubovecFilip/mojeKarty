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
import com.example.mojekarty.util.rememberExportImportHandlers

@Composable
fun SettingsScreen(
    context: Context,
    cards: List<Card>,
    autoSave: Boolean,
    onAutoSaveChange: (Boolean) -> Unit,
    onClearAll: () -> Unit,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onImport: (List<Card>) -> Unit,
) {
    val (export, import) = rememberExportImportHandlers(context, cards, onImport)
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

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(onClick = export, modifier = Modifier.weight(1f)) {
                Text("Export")
            }
            OutlinedButton(onClick = import, modifier = Modifier.weight(1f)) {
                Text("Import")
            }
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            thickness = 1.dp
        )

        Button(
            onClick = { navController.navigate("stats") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Zobraziť štatistiky")
        }

        Spacer(modifier = Modifier.weight(1f))

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
