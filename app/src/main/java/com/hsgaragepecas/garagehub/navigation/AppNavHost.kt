package com.hsgaragepecas.garagehub.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.hsgaragepecas.garagehub.ui.account.login.navigation.loginScreen
import com.hsgaragepecas.garagehub.ui.main.navigation.mainScreen
import com.hsgaragepecas.garagehub.ui.splash.navigation.splashScreen

/**
 * Defines the navigation graph for the application.
 *
 * @param navController The [NavHostController] that manages the navigation.
 * @param modifier The modifier to be applied to the NavHost.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Splash,
        modifier = modifier
    ) {
        splashScreen(navController)
        loginScreen(navController)
        mainScreen()
    }
}
