package com.example.mojekarty.util

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.mojekarty.R
import com.example.mojekarty.model.Card
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.OutputStreamWriter


/**
 * Funkcia na vytvorenie handlerov pre export a import kariet do/z súboru JSON.
 *
 * Inšpirované z: https://blog.jroddev.com/reading-and-writing-files-in-android-10/
 * Autor: jroddev
 * Prispôsobené pre použitie v aplikácii mojeKarty.
 *
 * Táto funkcia vráti dvojicu funkcií (export, import) vo forme Pair<()->Unit, ()->Unit>.
 *
 * Používa 'rememberLauncherForActivityResult' na spustenie systémových dialógov pre výber súboru.
 *
 * @param context Kontext aplikácie (potrebný pre contentResolver)
 * @param cards Zoznam kariet, ktorý sa má exportovať (pri exporte)
 * @param onImport Callback na spracovanie importovaných kariet (pri importe)
 * @return Dvojica obsahujúca dve funkcie
 *  - export: Funkcia na spustenie exportu kariet do JSON súboru
 *  - import: Funkcia na spustenie importu kariet z JSON súboru
 */
@Composable
fun rememberExportImportHandlers(
    context: Context,
    cards: List<Card>,
    onImport: (List<Card>) -> Unit
): Pair<() -> Unit, () -> Unit> {
    val gson = Gson()

    val exportFileName = stringResource(R.string.str_export_file_name)

    //Launcher pre export - CreateDocument spustí systémový dialóg na vytvorenie nového súboru.
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json"),
        onResult = { uri ->
            uri?.let {
                // Po výbere cesty pre uloženie súboru zapíšeme JSON do OutputStream.
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    val writer = OutputStreamWriter(outputStream)
                    writer.write(gson.toJson(cards)) // Serializácia zoznamu kariet na JSON.
                    writer.flush()
                }
            }
        }
    )

    //Launcher pre import - OpenDocument spustí systémový dialóg na výber existujúceho súboru.
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                // Po výbere súboru prečítame jeho obsah a deserializujeme JSON na List<Card>.
                context.contentResolver.openInputStream(it)?.use { inputStream ->
                    val json = inputStream.bufferedReader().readText()
                    val type = object : TypeToken<List<Card>>() {}.type
                    val importedCards: List<Card> = gson.fromJson(json, type)
                    // Callback na uloženie alebo spracovanie importovaných kariet.
                    onImport(importedCards)
                }
            }
        }
    )

    // Funkcia vráti Pair:
    //  - export(): Funkcia na spustenie exportu kariet do JSON súboru
    //  - import(): Funkcia na spustenie importu kariet z JSON súboru
    return Pair(
        { exportLauncher.launch(exportFileName) },
        { importLauncher.launch(arrayOf("application/json")) }
    )
}
