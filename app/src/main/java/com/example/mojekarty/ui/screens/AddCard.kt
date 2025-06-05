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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mojekarty.model.Card
import com.github.skydoves.colorpicker.compose.*
import androidx.core.graphics.toColorInt
import com.example.mojekarty.R
import com.example.mojekarty.ui.components.LoyaltyCardItem

@OptIn(ExperimentalMaterial3Api::class)
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
            label = { Text(stringResource(R.string.str_company)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = number,
            onValueChange = { number = it },
            label = { Text(stringResource(R.string.str_card_number)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {
                    // TODO: skenovanie kódu
                }) {
                    Icon(Icons.Default.Search, contentDescription = stringResource(R.string.str_scan))
                }
            }
        )
        OutlinedTextField(
            value = holder,
            onValueChange = { holder = it },
            label = { Text(stringResource(R.string.str_name_of_holder)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            controller = colorController,
            initialColor = selectedColor,
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

        Spacer(modifier = Modifier.weight(1f))

        val hexColor = String.format("#%06X", 0xFFFFFF and selectedColor.toArgb())

        val previewCard = Card(
            id = initialCard?.id ?: 0,
            companyName = if (company.isNotBlank()) company else stringResource(R.string.str_company),
            cardNumber = if (number.isNotBlank()) number else stringResource(R.string.str_dummy_number),
            holderName = holder,
            color = hexColor
        )

        LoyaltyCardItem(card = previewCard)

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.str_cancel))
            }
            Button(
                onClick = {
                    val savedCard = previewCard.copy(
                        id = initialCard?.id ?: (0..999_999).random(),
                        usedCount = initialCard?.usedCount ?: 0,
                        createdAt = initialCard?.createdAt ?: System.currentTimeMillis()
                    )
                    onSave(savedCard)
                },
                modifier = Modifier.weight(1f),
                enabled = company.isNotBlank() && number.isNotBlank()
            ) {
                Text(stringResource(R.string.str_add))
            }
        }
    }
}