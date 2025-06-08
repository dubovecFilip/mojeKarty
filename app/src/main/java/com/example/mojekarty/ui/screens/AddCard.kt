package com.example.mojekarty.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mojekarty.model.Card
import com.github.skydoves.colorpicker.compose.*
import androidx.core.graphics.toColorInt
import com.example.mojekarty.R
import com.example.mojekarty.ui.components.LoyaltyCardItem
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

/**
 * Obrazovka pre pridanie alebo úpravu vernostnej karty.
 *
 * Umožňuje zadať údaje o karte, naskenovať čiarovýkód cez kameru,
 * vybrať farbu a zobraziť náhľad karty.
 *
 * @param onSave Callback, ktorý sa zavolá po potvrdení a uloźení karty
 * @param onCancel Callback, ktorý sa zavolá pri zrušení pridania/úpravy karty
 * @param initialCard Pôvodná karta (ak ide o úpravu), inak null (pridanie novej)
 */
@Composable
fun AddCardScreen(
    onSave: (Card) -> Unit,
    onCancel: () -> Unit,
    initialCard: Card? = null
) {
    var company by remember { mutableStateOf(initialCard?.companyName ?: "") }
    var number by remember { mutableStateOf(initialCard?.cardNumber ?: "") }
    var holder by remember { mutableStateOf(initialCard?.holderName ?: "") }

    // Controller pre ColorPicker - uchováva aktuálne vybranú farbu.
    val colorController = rememberColorPickerController()
    var selectedColor by remember {
        mutableStateOf(
            initialCard?.color?.let { Color(it.toColorInt()) }
                ?: Color(0xFF1E88D9)
        )
    }
    // Posledná platná farba s dostatočným kontrastom.
    var lastValidColor by remember { mutableStateOf(selectedColor) }
    val context = LocalContext.current

    // Launcher pre spustenie skenovania čiarového kódu cez ZXing.
    val launcher = rememberLauncherForActivityResult(contract = ScanContract()) { result ->
        if (result.contents != null) {
            number = result.contents
        }
    }

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
                    val options = ScanOptions().apply {
                        setPrompt(context.getString(R.string.str_scan_prompt))
                    }
                    launcher.launch(options)
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
                .height(150.dp),
            controller = colorController,
            initialColor = selectedColor,
            // Obmedzenie výberu príliž svetlých farieb (čitateľnosť textu).
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