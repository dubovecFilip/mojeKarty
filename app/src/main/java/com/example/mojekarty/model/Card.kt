package com.example.mojekarty.model

data class Card(
    val id: Int,
    val companyName: String,
    val cardNumber: String,
    val holderName: String? = null,
    val color: String,
    val usedCount: Int = 0
)