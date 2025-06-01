package com.example.mojekarty.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mojekarty.model.Card

@Composable
fun AddCardScreen(
    onSave: (Card) -> Unit,
    onCancel: () -> Unit
) {
    var company by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var holder by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("#2196F3") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = company,
            onValueChange = { company = it },
            label = { Text("Spoločnosť") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = number,
            onValueChange = { number = it },
            label = { Text("Číslo karty") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {
                    // TODO: skenovanie kódu
                }) {
                    Icon(Icons.Default.Search, contentDescription = "Skenovať")
                }
            }
        )
        OutlinedTextField(
            value = holder,
            onValueChange = { holder = it },
            label = { Text("Meno držiteľa") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = color,
            onValueChange = { color = it },
            label = { Text("Farba (hex)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Zrušiť")
            }
            Button(
                onClick = {
                    val newCard = Card(
                        id = (0..999999).random(),
                        companyName = company,
                        cardNumber = number,
                        holderName = holder.ifBlank { null },
                        color = color
                    )
                    onSave(newCard)
                },
                modifier = Modifier.weight(1f),
                enabled = company.isNotBlank() && number.isNotBlank()
            ) {
                Text("Pridať")
            }
        }
    }
}