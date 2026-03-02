package com.hsgaragepecas.garagehub.ui.account.create.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hsgaragepecas.garagehub.core.extensions.navigateTo
import com.hsgaragepecas.garagehub.navigation.CreateAccount
import com.hsgaragepecas.garagehub.ui.account.create.CreateAccountScreen

fun NavGraphBuilder.createAccountScreen(
    navController: NavController,
) {
    composable<CreateAccount> {
        CreateAccountScreen()
    }
}

fun NavController.navigateToCreateAccountScreen() {
    navigateTo(CreateAccount)
}