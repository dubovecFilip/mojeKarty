package com.example.mojekarty.data

import android.content.Context
import com.example.mojekarty.model.Card
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit

object StorageManager {

    private const val PREF_NAME = "moje_karty_prefs"
    private const val KEY_CARDS = "cards_json"
    private const val KEY_AUTO_SAVE = "auto_save_enabled"

    private val gson = Gson()

    fun saveCards(context: Context, cards: List<Card>) {
        val json = gson.toJson(cards)
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit() { putString(KEY_CARDS, json) }
    }

    fun loadCards(context: Context): MutableList<Card> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_CARDS, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Card>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveAutoSaveEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit() { putBoolean(KEY_AUTO_SAVE, enabled) }
    }

    fun loadAutoSaveEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_AUTO_SAVE, true)
    }
}