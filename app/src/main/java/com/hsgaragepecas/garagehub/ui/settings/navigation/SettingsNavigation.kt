package com.hsgaragepecas.garagehub.ui.settings.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hsgaragepecas.garagehub.ui.settings.SettingsScreen
import com.hsgaragepecas.garagehub.ui.settings.SettingsViewModelImpl

const val settingsRoute = "settings"

fun NavController.navigateToSettingsScreen(navOptions: NavOptions? = null) {
    this.navigate(settingsRoute, navOptions)
}

fun NavGraphBuilder.settingsScreen() {
    composable(route = settingsRoute) {
        SettingsScreen(
            viewModel = hiltViewModel<SettingsViewModelImpl>()
        )
    }
}
