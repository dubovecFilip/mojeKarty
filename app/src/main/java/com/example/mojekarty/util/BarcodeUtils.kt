package com.example.mojekarty.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

fun generateBarcodeBitmap(data: String, width: Int = 800, height: Int = 200): ImageBitmap? {
    return try {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(data, BarcodeFormat.CODE_128, width, height)
        val bmp = createBitmap(width, height)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp[x, y] =
                    if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
            }
        }
        bmp.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}