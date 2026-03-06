package com.hsgaragepecas.garagehub.ui.account.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hsgaragepecas.garagehub.core.extensions.navigateTo
import com.hsgaragepecas.garagehub.navigation.Login
import com.hsgaragepecas.garagehub.ui.account.login.LoginScreen
import com.hsgaragepecas.garagehub.ui.settings.navigation.navigateToSettingsScreen

/**
 * Defines the login screen in the navigation graph.
 *
 * @param navController The [NavController] that manages the navigation.
 */
fun NavGraphBuilder.loginScreen(
    navController: NavController,
) {
    composable<Login> {
        LoginScreen(
            onCreateAccountClick = { navController.navigateToSettingsScreen() }
        )
    }
}

/**
 * Navigates to the login screen.
 */
fun NavController.navigateToLoginScreen() {
    navigateTo(Login)
}
