package com.example.mojekarty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import com.example.mojekarty.ui.theme.MojeKartyTheme
import com.example.mojekarty.ui.screens.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MojeKartyTheme {
                Surface {
                    MainScreen(context = this)
                }
            }
        }
    }
}
