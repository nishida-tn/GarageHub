package com.hsgaragepecas.garagehub.ui.estimate.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hsgaragepecas.garagehub.navigation.EditEstimate
import com.hsgaragepecas.garagehub.ui.estimate.edit.EditEstimateScreen

/**
 * Extension function to navigate to the edit estimate screen.
 *
 * @param estimateId The ID of the estimate.
 * @param navOptions The navigation options.
 */
fun NavController.navigateToEditEstimate(estimateId: Int, navOptions: NavOptions? = null) {
    this.navigate(EditEstimate(estimateId), navOptions)
}

/**
 * Extension function to add the edit estimate screen to the navigation graph.
 *
 * @param navController The navigation controller.
 */
fun NavGraphBuilder.editEstimateScreen(
    navController: NavController
) {
    composable<EditEstimate> { backStackEntry ->
        val editEstimate: EditEstimate = backStackEntry.toRoute()
        EditEstimateScreen(
            estimateId = editEstimate.estimateId,
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }
}
