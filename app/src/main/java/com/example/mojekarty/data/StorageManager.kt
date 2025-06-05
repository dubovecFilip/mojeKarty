package com.example.mojekarty.data

import android.content.Context
import com.example.mojekarty.model.Card
import androidx.core.content.edit

/**
 * Naučené z: https://developer.android.com/reference/android/content/SharedPreferences
 * Autor: Android Developers
 */
object StorageManager {

    private const val PREF_NAME = "cards_prefs"
    private const val KEY_AUTO_SAVE = "auto_save_enabled"
    private const val FILE_NAME = "cards.json"

    fun saveAutoSaveEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit() { putBoolean(KEY_AUTO_SAVE, enabled) }
    }

    fun loadAutoSaveEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_AUTO_SAVE, true)
    }

    fun saveCardsToFile(context: Context, cards: List<Card>) {
        val json = CardSerializer.serialize(cards)
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
    }

    fun loadCardsFromFile(context: Context): MutableList<Card> {
        return try {
            val json = context.openFileInput(FILE_NAME).bufferedReader().use { it.readText() }
            CardSerializer.deserialize(json)
        } catch (e: Exception) {
            mutableListOf()
        }
    }
}