package com.example.mojekarty.model

data class Card(
    val id: Int,
    val companyName: String,
    val cardNumber: String,
    val holderName: String? = null,
    val color: String = "#2196F3",
    val saved: Double = 0.0,
    val points: Int = 0
)