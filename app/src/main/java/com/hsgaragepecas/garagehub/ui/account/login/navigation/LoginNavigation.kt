package com.hsgaragepecas.garagehub.ui.account.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hsgaragepecas.garagehub.core.extensions.navigateTo
import com.hsgaragepecas.garagehub.navigation.Login
import com.hsgaragepecas.garagehub.ui.account.login.LoginScreen

fun NavGraphBuilder.loginScreen(
    navController: NavController,
) {
    composable<Login> {
        LoginScreen()
    }
}

fun NavController.navigateToLoginScreen() {
    navigateTo(Login)
}