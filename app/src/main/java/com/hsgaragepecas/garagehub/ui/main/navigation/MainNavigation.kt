package com.hsgaragepecas.garagehub.ui.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hsgaragepecas.garagehub.navigation.Login
import com.hsgaragepecas.garagehub.navigation.Main
import com.hsgaragepecas.garagehub.ui.main.MainScreen

/**
 * Defines the main screen in the navigation graph.
 */
fun NavGraphBuilder.mainScreen() {
    composable<Main> {
        MainScreen()
    }
}

/**
 * Navigates to the main screen.
 */
fun NavController.navigateToMainScreen() {
    navigate(Main)
}

/**
 * Navigates to the main screen, popping the login screen from the back stack.
 */
fun NavController.navigateToMainScreenFromLogin() {
    navigate(Main) {
        popUpTo(Login) {
            inclusive = true
        }
    }
}
