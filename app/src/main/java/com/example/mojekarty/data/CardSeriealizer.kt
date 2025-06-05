package com.example.mojekarty.data

import com.example.mojekarty.model.Card
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

object CardSerializer {

    private val gson = Gson()
    private val gsonPretty = GsonBuilder().setPrettyPrinting().create()

    fun serialize(cards: List<Card>): String = gsonPretty.toJson(cards)

    fun deserialize(json: String): MutableList<Card> {
        val type = object : TypeToken<MutableList<Card>>() {}.type
        return gson.fromJson(json, type)
    }
}