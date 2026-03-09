package com.hsgaragepecas.garagehub.ui.account.login.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hsgaragepecas.garagehub.core.extensions.navigateTo
import com.hsgaragepecas.garagehub.navigation.Login
import com.hsgaragepecas.garagehub.ui.account.login.LoginScreen
import com.hsgaragepecas.garagehub.ui.account.login.LoginViewModelImpl
import com.hsgaragepecas.garagehub.ui.listorder.navigation.navigateToListOrder
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
            viewModel = hiltViewModel<LoginViewModelImpl>(),
            onLoginSuccess = {
                navController.navigateToListOrder()
            },
            onForgotPasswordClick = {
                // TODO: Navigate to forgot password screen
            },
            onCreateAccountClick = {
                navController.navigateToSettingsScreen()
            }
        )
    }
}

/**
 * Navigates to the login screen.
 */
fun NavController.navigateToLoginScreen() {
    navigateTo(Login)
}
