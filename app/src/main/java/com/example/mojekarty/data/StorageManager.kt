package com.example.mojekarty.data

import android.content.Context
import com.example.mojekarty.model.Card
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit
import com.google.gson.GsonBuilder

object StorageManager {

    private const val PREF_NAME = "moje_karty_prefs"
    private const val FILE_NAME = "cards.json"
    private const val KEY_AUTO_SAVE = "auto_save_enabled"

    private val gson = Gson()

    fun saveAutoSaveEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit() { putBoolean(KEY_AUTO_SAVE, enabled) }
    }

    fun loadAutoSaveEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_AUTO_SAVE, true)
    }

    fun saveCardsToFile(context: Context, cards: List<Card>) {
        val json = GsonBuilder().setPrettyPrinting().create().toJson(cards)
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
    }

    fun loadCardsFromFile(context: Context): MutableList<Card> {
        return try {
            val file = context.getFileStreamPath(FILE_NAME)
            if (!file.exists()) return mutableListOf()

            val json = file.readText()
            val type = object : TypeToken<MutableList<Card>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            mutableListOf()
        }
    }
}