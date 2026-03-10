package com.hsgaragepecas.garagehub.ui.settings.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hsgaragepecas.garagehub.navigation.Settings
import com.hsgaragepecas.garagehub.ui.settings.SettingsScreen
import com.hsgaragepecas.garagehub.ui.settings.SettingsViewModelImpl

/**
 * The route for the settings screen.
 */


/**
 * Navigates to the settings screen.
 *
 * @param navOptions The navigation options.
 */
fun NavController.navigateToSettingsScreen(navOptions: NavOptions? = null) {
    this.navigate(Settings, navOptions)
}

/**
 * Defines the settings screen in the navigation graph.
 */
fun NavGraphBuilder.settingsScreen(navController: NavHostController) {
    composable<Settings> {
        SettingsScreen(
            viewModel = hiltViewModel<SettingsViewModelImpl>(),
            navController = navController
        )
    }
}
