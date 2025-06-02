package com.example.mojekarty.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.mojekarty.model.Card
import com.github.skydoves.colorpicker.compose.*
import androidx.core.graphics.toColorInt
import com.example.mojekarty.ui.components.LoyaltyCardItem

@Composable
fun AddCardScreen(
    onSave: (Card) -> Unit,
    onCancel: () -> Unit,
    initialCard: Card? = null
) {
    var company by remember { mutableStateOf(initialCard?.companyName ?: "") }
    var number by remember { mutableStateOf(initialCard?.cardNumber ?: "") }
    var holder by remember { mutableStateOf(initialCard?.holderName ?: "") }

    val colorController = rememberColorPickerController()
    var selectedColor by remember {
        mutableStateOf(
            initialCard?.color?.let { Color(it.toColorInt()) }
                ?: Color(0xFF2196F3)
        )
    }
    var lastValidColor by remember { mutableStateOf(selectedColor) }

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

        Text("Vyber farbu", style = MaterialTheme.typography.labelLarge)

        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            controller = colorController,
            onColorChanged = { colorEnvelope ->
                val color = colorEnvelope.color
                if (color.luminance() < 0.80f) {
                    selectedColor = color
                    lastValidColor = color
                } else {
                    selectedColor = lastValidColor
                }
            }
        )

        val hexColor = "#" + Integer.toHexString(selectedColor.toArgb()).takeLast(6)
        LoyaltyCardItem(
            card = Card(
                id = 0,
                companyName = if (company.isNotBlank()) company else "Spoločnosť",
                cardNumber = if (number.isNotBlank()) number else "1234567890",
                holderName = holder,
                color = hexColor
            )
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
                        id = initialCard?.id ?: (0..999999).random(),
                        companyName = company,
                        cardNumber = number,
                        holderName = holder.ifBlank { null },
                        color = hexColor
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