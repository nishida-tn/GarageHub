package com.hsgaragepecas.garagehub.navigation

import androidx.navigation.NavController
import com.hsgaragepecas.garagehub.ui.account.create.navigation.navigateToCreateAccountScreen
import com.hsgaragepecas.garagehub.ui.account.login.navigation.navigateToLoginScreen
import com.hsgaragepecas.garagehub.ui.estimate.navigation.navigateToCreateEstimateScreen
import com.hsgaragepecas.garagehub.ui.listorder.navigation.navigateToListOrder
import com.hsgaragepecas.garagehub.ui.settings.navigation.navigateToSettingsScreen
import kotlinx.serialization.Serializable

/**
 * A sealed class that represents a route in the application.
 */
@Serializable
sealed class Route {
    /**
     * Navigates to the route.
     *
     * @param navController The [NavController] that manages the navigation.
     */
    fun navigate(navController: NavController) {
        when (this) {
            is Login -> navController.navigateToLoginScreen()
            is CreateAccount -> navController.navigateToCreateAccountScreen()
            is Settings -> navController.navigateToSettingsScreen()
            is ListOrder -> navController.navigateToListOrder()
            is CreateEstimate -> navController.navigateToCreateEstimateScreen()
            is ForgotPassword -> TODO()
        }
    }
}

/**
 * Represents the login screen route.
 */
@Serializable
data object Login : Route()

/**
 * Represents the create account screen route.
 */
@Serializable
data object CreateAccount : Route()

/**
 * Represents the forgot password screen route.
 */
@Serializable
data object ForgotPassword : Route()

/**
 * Represents the settings screen route.
 */
@Serializable
data object Settings : Route()

/**
 * Represents the list order screen route.
 */
@Serializable
data object ListOrder : Route()

/**
 * Represents the create estimate screen route.
 */
@Serializable
data object CreateEstimate : Route()
