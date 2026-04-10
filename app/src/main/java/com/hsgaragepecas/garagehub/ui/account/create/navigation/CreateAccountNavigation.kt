package com.hsgaragepecas.garagehub.ui.account.create.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hsgaragepecas.garagehub.core.extensions.navigateTo
import com.hsgaragepecas.garagehub.navigation.CreateAccount
import com.hsgaragepecas.garagehub.ui.account.create.CreateAccountScreen
import com.hsgaragepecas.garagehub.ui.account.create.CreateAccountViewModel
import com.hsgaragepecas.garagehub.ui.main.navigation.navigateToMainScreenFromCreateAccount

/**
 * Defines the create account screen in the navigation graph.
 *
 * @param navController The [NavController] that manages the navigation.
 */
fun NavGraphBuilder.createAccountScreen(
    navController: NavController,
) {
    composable<CreateAccount> {
        CreateAccountScreen(
            viewModel = hiltViewModel<CreateAccountViewModel>(),
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToMain = {
                navController.navigateToMainScreenFromCreateAccount()
            }
        )
    }
}

/**
 * Navigates to the create account screen.
 */
fun NavController.navigateToCreateAccountScreen() {
    navigateTo(CreateAccount)
}
