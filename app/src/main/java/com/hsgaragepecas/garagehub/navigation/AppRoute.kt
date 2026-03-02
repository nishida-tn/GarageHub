package com.hsgaragepecas.garagehub.navigation

import androidx.navigation.NavController
import com.hsgaragepecas.garagehub.ui.account.create.navigation.navigateToCreateAccountScreen
import com.hsgaragepecas.garagehub.ui.account.login.navigation.navigateToLoginScreen
import kotlinx.serialization.Serializable

@Serializable
abstract class Route {
    fun navigate(navController: NavController) {
        when (this) {
            is Login -> navController.navigateToLoginScreen()
            is CreateAccount -> navController.navigateToCreateAccountScreen()
        }
    }
}

@Serializable
data object Login : Route()

@Serializable
data object CreateAccount : Route()

@Serializable
data object ForgotPassword : Route()