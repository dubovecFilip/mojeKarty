package com.example.mojekarty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import com.example.mojekarty.ui.theme.MojeKartyTheme
import com.example.mojekarty.ui.screens.MainScreen
import android.content.pm.ActivityInfo
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

/**
 * Hlavná aktivita aplikácie mojeKarty.
 *
 * Inicializuje Compose prostredie a nastavuje tému a navigáciu.
 * Dynamicky riadi orientáciu obrazovky porľa aktuálnej obrazovky (route):
 *  - cards, settings -> umožnená automatická orientácia
 *  - ostatné -> zamknuté na portrait.
 */
@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MojeKartyTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val route = navBackStackEntry?.destination?.route

                LaunchedEffect(route) {
                    requestedOrientation = when (route) {
                        "cards", "settings", "preview/{cardId}" -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                        else -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    }
                }

                Surface {
                    MainScreen(context = this@MainActivity, navController = navController)
                }
            }
        }
    }
}
