package com.hsgaragepecas.garagehub.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hsgaragepecas.garagehub.ui.estimate.navigation.createEstimateScreen
import com.hsgaragepecas.garagehub.ui.listorder.navigation.listOrderScreen
import com.hsgaragepecas.garagehub.ui.settings.navigation.settingsScreen

/**
 * Defines the navigation graph for the main part of the application,
 * which is accessible after login and includes the bottom navigation bar.
 *
 * @param navController The [NavHostController] that manages the navigation.
 * @param modifier The modifier to be applied to the NavHost.
 */
@Composable
fun MainAppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = ListOrder,
        modifier = modifier
    ) {
        listOrderScreen(navController)
        createEstimateScreen(navController)
        settingsScreen(navController)
    }
}
