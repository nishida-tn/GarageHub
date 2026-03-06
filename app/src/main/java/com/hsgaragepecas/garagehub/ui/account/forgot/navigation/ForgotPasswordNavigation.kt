package com.hsgaragepecas.garagehub.ui.account.forgot.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hsgaragepecas.garagehub.core.extensions.navigateTo
import com.hsgaragepecas.garagehub.navigation.ForgotPassword
import com.hsgaragepecas.garagehub.ui.account.forgot.ForgotPasswordScreen

/**
 * Defines the forgot password screen in the navigation graph.
 *
 * @param navController The [NavController] that manages the navigation.
 */
fun NavGraphBuilder.forgotPasswordScreen(
    navController: NavController,
) {
    composable<ForgotPassword> {
        ForgotPasswordScreen()
    }
}

/**
 * Navigates to the forgot password screen.
 */
fun NavController.navigateToForgotPasswordScreen() {
    navigateTo(ForgotPassword)
}
