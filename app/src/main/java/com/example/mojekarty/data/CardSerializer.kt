package com.example.mojekarty.data

import com.example.mojekarty.model.Card
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

/**
 * Pomocná trieda na serializáciu a deserializáciu zoznamu kariet
 * do formátu JSON a späť.
 *
 * Naučené a inšpirované z: ______
 */
object CardSerializer {

    // Gson inštancia pre bežné použitie
    private val gson = Gson()

    // Gson inštancia pre "pekne" formátovaný výstup (Pretty Print)
    private val gsonPretty = GsonBuilder().setPrettyPrinting().create()

    /**
     * Serializuje zoznam kariet do pekného formátu JSON.
     */
    fun serialize(cards: List<Card>): String = gsonPretty.toJson(cards)

    /**
     * Deserializuje JSON reťazec na zoznam kariet.
     */
    fun deserialize(json: String): MutableList<Card> {
        val type = object : TypeToken<MutableList<Card>>() {}.type
        return gson.fromJson(json, type)
    }
}