package com.example.mojekarty.data

import android.content.Context
import com.example.mojekarty.model.Card
import androidx.core.content.edit
import com.example.mojekarty.R
import java.io.File

/**
 * Trieda na správu ukladania a načítavania dát aplikácie.
 *
 * Ukladá nastavenie automatického ukladania do SharedPreferences
 * a zoznam kariet do interného súboru vo formáte JSON.
 *
 * Naučené a inšpirované z: ______
 */
object StorageManager {

    /**
     * Uloží informáciu, či je povolené automatické ukladanie.
     *
     * @param context Kontext aplikácie
     * @param enabled Hodnota, či je povolené automatické ukladanie
     */
    fun saveAutoSaveEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(context.getString(R.string.prefs_name), Context.MODE_PRIVATE)
        prefs.edit { putBoolean(context.getString(R.string.auto_save_enabled), enabled) }
    }

    /**
     * Načíta informáciu, či je povolené automatické ukladanie.
     *
     * @param context Kontext aplikácie
     * @return Hodnota, či je povolené automatické ukladanie
     */
    fun loadAutoSaveEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(context.getString(R.string.prefs_name), Context.MODE_PRIVATE)
        return prefs.getBoolean(context.getString(R.string.auto_save_enabled), true)
    }

    /**
     * Uloží zoznam kariet do interného súboru vo formáte JSON.
     *
     * @param context Kontext aplikácie
     * @param cards Zoznam kariet na uloženie
     */
    fun saveCardsToFile(context: Context, cards: List<Card>) {
        val json = CardSerializer.serialize(cards)
        context.openFileOutput(context.getString(R.string.file_name_sp), Context.MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
    }

    /**
     * Načíta zoznam kariet z interného súboru.
     *
     * @param context Kontext aplikácie
     * @return Zoznam kariet, alebo prázdny zoznam ak sa nepodarilo načítať súbor
     */
    fun loadCardsFromFile(context: Context): MutableList<Card> {
        val file = File(context.filesDir, context.getString(R.string.file_name_sp))
        return if (file.exists()) {
            val json = file.bufferedReader().use { it.readText() }
            CardSerializer.deserialize(json)
        } else {
            mutableListOf()
        }
    }
}