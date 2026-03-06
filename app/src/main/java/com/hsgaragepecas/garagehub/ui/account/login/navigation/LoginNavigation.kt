package com.hsgaragepecas.garagehub.ui.account.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hsgaragepecas.garagehub.core.extensions.navigateTo
import com.hsgaragepecas.garagehub.navigation.Login
import com.hsgaragepecas.garagehub.ui.account.login.LoginScreen
import com.hsgaragepecas.garagehub.ui.settings.navigation.navigateToSettingsScreen

fun NavGraphBuilder.loginScreen(
    navController: NavController,
) {
    composable<Login> {
        LoginScreen(
            onCreateAccountClick = { navController.navigateToSettingsScreen() }
        )
    }
}

fun NavController.navigateToLoginScreen() {
    navigateTo(Login)
}