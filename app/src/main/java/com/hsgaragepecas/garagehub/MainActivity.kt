package com.hsgaragepecas.garagehub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.hsgaragepecas.garagehub.navigation.AppNavHost
import com.hsgaragepecas.garagehub.ui.theme.GarageHubTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main activity of the application.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GarageHubTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}
