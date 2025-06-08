package com.example.mojekarty.model

/**
 * Dátová trieda reprezentujúca jednu kartu.
 *
 * @property id Identifikátor karty
 * @property companyName Názov spolocnosti, ktorá vystavila kartu
 * @property cardNumber Číslo karty
 * @property holderName Názov vlastníka karty
 * @property color Farba karty
 * @property usedCount Počet užitkov
 * @property createdAt Čas vytvorenia karty
 */
data class Card(
    val id: Int,
    val companyName: String,
    val cardNumber: String,
    val holderName: String? = null,
    val color: String,
    val usedCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)