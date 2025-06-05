package com.example.mojekarty.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mojekarty.R
import com.example.mojekarty.data.StorageManager
import com.example.mojekarty.model.Card
import kotlinx.coroutines.launch
import com.example.mojekarty.util.rememberExportImportHandlers

@Composable
fun SettingsScreen(
    context: Context,
    cards: List<Card>,
    onAutoSaveChange: (Boolean) -> Unit,
    onClearAll: () -> Unit,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onImport: (List<Card>) -> Unit,
) {
    val (export, import) = rememberExportImportHandlers(context, cards, onImport)
    val coroutineScope = rememberCoroutineScope()

    var localAutoSave by remember { mutableStateOf(StorageManager.loadAutoSaveEnabled(context)) }

    var showConfirm by remember { mutableStateOf(false) }

    val changesSaved = stringResource(R.string.str_changes_saved)
    val deleteConfirm = stringResource(R.string.str_delete_confirm)

    fun saveCards(cards: List<Card>) {
        StorageManager.saveCardsToFile(context, cards)
    }

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
                text = stringResource(R.string.str_auto_save),
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
                    saveCards(cards)
                    navController.navigate("cards") {
                        popUpTo("settings") { inclusive = true }
                    }
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(changesSaved)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.str_save_changes))
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                thickness = 1.dp
            )

        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(onClick = export, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.str_export))
            }
            OutlinedButton(onClick = import, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.str_import))
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
            Text(stringResource(R.string.str_show_stats))
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = { showConfirm = true },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.str_delete_cards))
        }

        if (showConfirm) {
            AlertDialog(
                onDismissRequest = { showConfirm = false },
                title = { Text(stringResource(R.string.str_confirm_delete)) },
                text = { Text(stringResource(R.string.str_confirm_delete_desc)) },
                confirmButton = {
                    TextButton(onClick = {
                        onClearAll()
                        saveCards(emptyList())
                        showConfirm = false

                        navController.navigate("cards") {
                            popUpTo("settings") { inclusive = true }
                        }

                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(deleteConfirm)
                        }
                    }) {
                        Text(stringResource(R.string.str_delete))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirm = false }) {
                        Text(stringResource(R.string.str_cancel))
                    }
                }
            )
        }
    }
}
