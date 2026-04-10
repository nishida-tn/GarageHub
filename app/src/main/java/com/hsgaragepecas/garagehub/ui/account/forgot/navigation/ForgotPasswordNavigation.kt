package com.hsgaragepecas.garagehub.ui.account.forgot.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hsgaragepecas.garagehub.core.extensions.navigateTo
import com.hsgaragepecas.garagehub.navigation.ForgotPassword
import com.hsgaragepecas.garagehub.ui.account.forgot.ForgotPasswordScreen
import com.hsgaragepecas.garagehub.ui.account.forgot.ForgotPasswordViewModel

/**
 * Defines the forgot password screen in the navigation graph.
 *
 * @param navController The [NavController] that manages the navigation.
 */
fun NavGraphBuilder.forgotPasswordScreen(
    navController: NavController,
) {
    composable<ForgotPassword> {
        ForgotPasswordScreen(
            viewModel = hiltViewModel<ForgotPasswordViewModel>(),
            onBackToLoginClick = {
                navController.popBackStack()
            }
        )
    }
}

/**
 * Navigates to the forgot password screen.
 */
fun NavController.navigateToForgotPasswordScreen() {
    navigateTo(ForgotPassword)
}
