package com.hsgaragepecas.garagehub.ui.estimate.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hsgaragepecas.garagehub.core.extensions.navigateTo
import com.hsgaragepecas.garagehub.navigation.CreateEstimate
import com.hsgaragepecas.garagehub.ui.estimate.CreateEstimateScreen

fun NavGraphBuilder.createEstimateScreen(navController: NavController) {
    composable<CreateEstimate> {
        CreateEstimateScreen()
    }
}

/**
 * Navigates to the list order screen.
 */
fun NavController.navigateToCreateEstimateScreen() {
    navigateTo(CreateEstimate)
}
