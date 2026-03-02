package com.hsgaragepecas.garagehub.core.extensions

import androidx.navigation.NavController
import com.hsgaragepecas.garagehub.navigation.Route

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