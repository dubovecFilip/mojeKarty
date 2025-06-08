package com.example.mojekarty.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

/**
 * Generuje bitmapu čiarového kódu zo zadaného textu.
 *
 * Používa formát CODE_128 (bežne čitateľný v predajniach, kompatibilný s väčšinou skenerov.
 *
 * Prevzaté z: https://medium.com/@copy2sim/generate-qr-code-using-zxing-library-751ca3d76792
 * Autor: Scott
 * Upravené pre použitie v aplikácii mojeKarty.
 *
 * @param data Text, ktorý sa má zapísať do kódu
 * @param width Šírka výsledného čiarového kódu (default 800 px)
 * @param height Výška výsledného čiarového kódu (default 200 px)
 * @return Bitmap čiarového kódu
 */
fun generateBarcodeBitmap(data: String, width: Int = 800, height: Int = 200): ImageBitmap {
    val bitMatrix: BitMatrix = MultiFormatWriter().encode(data, BarcodeFormat.CODE_128, width, height)
    val bmp = createBitmap(width, height)
    for (x in 0..width-1) {
        for (y in 0..height-1) {
            bmp[x, y] = if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
        }
    }
    return bmp.asImageBitmap()
}