package com.example.mojekarty.util

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.example.mojekarty.model.Card
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.OutputStreamWriter

@Composable
fun rememberExportImportHandlers(
    context: Context,
    cards: List<Card>,
    onImport: (List<Card>) -> Unit
): Pair<() -> Unit, () -> Unit> {
    val gson = Gson()

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json"),
        onResult = { uri ->
            uri?.let {
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    val writer = OutputStreamWriter(outputStream)
                    writer.write(gson.toJson(cards))
                    writer.flush()
                }
            }
        }
    )

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                context.contentResolver.openInputStream(it)?.use { inputStream ->
                    val json = inputStream.bufferedReader().readText()
                    val type = object : TypeToken<List<Card>>() {}.type
                    val importedCards: List<Card> = gson.fromJson(json, type)
                    onImport(importedCards)
                }
            }
        }
    )

    return Pair(
        { exportLauncher.launch("karty_export.json") },
        { importLauncher.launch(arrayOf("application/json")) }
    )
}
