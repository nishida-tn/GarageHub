package com.hsgaragepecas.garagehub.ui.estimate.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hsgaragepecas.garagehub.navigation.ListEstimate
import com.hsgaragepecas.garagehub.ui.estimate.list.ListEstimateScreen

/**
 * Extension function to navigate to the list estimate screen.
 *
 * @param navOptions The navigation options.
 */
fun NavController.navigateToListEstimate(navOptions: NavOptions? = null) {
    this.navigate(ListEstimate, navOptions)
}

/**
 * Extension function to add the list estimate screen to the navigation graph.
 *
 * @param navController The navigation controller.
 */
fun NavGraphBuilder.listEstimateScreen(
    navController: NavController
) {
    composable<ListEstimate> {
        ListEstimateScreen(
            onNavigateToCreateEstimate = {
                navController.navigate(com.hsgaragepecas.garagehub.navigation.CreateEstimate)
            },
            onNavigateToEstimateDetails = { orcId ->
                // TODO: Navigate to estimate details
            }
        )
    }
}
