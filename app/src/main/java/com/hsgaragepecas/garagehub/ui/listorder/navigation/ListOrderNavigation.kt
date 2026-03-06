package com.hsgaragepecas.garagehub.ui.listorder.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hsgaragepecas.garagehub.core.extensions.navigateTo
import com.hsgaragepecas.garagehub.navigation.ListOrder
import com.hsgaragepecas.garagehub.ui.listorder.ListOrderScreen

/**
 * Defines the list order screen in the navigation graph.
 *
 * @param navController The [NavController] that manages the navigation.
 */
fun NavGraphBuilder.listOrderScreen(navController: NavController) {
    composable<ListOrder> {
        ListOrderScreen(onNavigateToOrderDetails = {})
    }
}

/**
 * Navigates to the list order screen.
 */
fun NavController.navigateToListOrder() {
    navigateTo(ListOrder)
}
