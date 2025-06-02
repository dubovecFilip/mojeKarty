package com.example.mojekarty.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mojekarty.model.Card
import androidx.core.graphics.toColorInt
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import com.example.mojekarty.util.generateBarcodeBitmap

@Composable
fun LoyaltyCardItem(
    card: Card,
    modifier: Modifier = Modifier,
    barcodeHeight: Dp = 80.dp,
    height: Dp = 120.dp
) {
    val backgroundColor = Color(card.color.toColorInt())

    val barcode = generateBarcodeBitmap(card.cardNumber, width = 600, height = barcodeHeight.value.toInt())

    val cardModifier = modifier
        .fillMaxWidth()
        .height(height)
        .clip(RoundedCornerShape(16.dp))

    Card(
        modifier = cardModifier,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = card.companyName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                /*Text(
                    text = "${card.usedCount}x",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )*/
                Text(
                    text = card.cardNumber,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(barcodeHeight)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(4.dp)
            ) {
                if (barcode != null) {
                    Image(
                        bitmap = barcode,
                        contentDescription = "Čiarový kód",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(
                        text = "Neplatný kód",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}