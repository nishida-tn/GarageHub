package com.hsgaragepecas.garagehub.core.extensions

import androidx.navigation.NavController
import com.hsgaragepecas.garagehub.navigation.AppNavigation

/**
 * Navigates to the given route.
 *
 * @param route The route to navigate to.
 */
fun NavController.navigateTo(route: AppNavigation) {
    val currentRoute = currentBackStackEntry?.destination?.route
    navigate(route) {
        currentRoute?.let {
            popUpTo(it) {
                inclusive = false
            }
        }
    }
}
