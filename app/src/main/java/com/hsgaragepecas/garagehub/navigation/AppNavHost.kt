package com.hsgaragepecas.garagehub.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.hsgaragepecas.garagehub.ui.account.login.navigation.loginScreen
import com.hsgaragepecas.garagehub.ui.settings.navigation.settingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Login
    ) {
        loginScreen(navController)
        settingsScreen()
    }
}