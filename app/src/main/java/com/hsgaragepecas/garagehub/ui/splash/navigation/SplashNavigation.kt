package com.hsgaragepecas.garagehub.ui.splash.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hsgaragepecas.garagehub.navigation.Splash
import com.hsgaragepecas.garagehub.ui.splash.SplashScreen

fun NavGraphBuilder.splashScreen(navController: NavController) {
    composable<Splash> {
        SplashScreen(navController)
    }
}
