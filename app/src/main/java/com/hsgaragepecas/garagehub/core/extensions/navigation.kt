package com.hsgaragepecas.garagehub.core.extensions

import androidx.navigation.NavController
import com.hsgaragepecas.garagehub.navigation.Route

/**
 * Navigates to the given route.
 *
 * @param route The route to navigate to.
 */
fun NavController.navigateTo(route: Route) {
    val currentRoute = currentBackStackEntry?.destination?.route
    navigate(route) {
        currentRoute?.let {
            popUpTo(it) {
                inclusive = false
            }
        }
    }
}
