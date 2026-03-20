package com.hsgaragepecas.garagehub.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents the items in the bottom navigation bar.
 *
 * @param route The navigation route.
 * @param title The title of the item.
 * @param icon The icon of the item.
 */
sealed class BottomNavItem(
    val route: AppNavigation,
    val title: String,
    val icon: ImageVector
) {
    /**
     * Represents the List Order screen item in the bottom navigation bar.
     */
    data object ListOrder : BottomNavItem(
        route = com.hsgaragepecas.garagehub.navigation.ListOrder,
        title = "Pedidos",
        icon = Icons.Default.List
    )

    /**
     * Represents the List Estimate screen item in the bottom navigation bar.
     */
    data object ListEstimate : BottomNavItem(
        route = com.hsgaragepecas.garagehub.navigation.ListEstimate,
        title = "Orçamentos",
        icon = Icons.Default.Search
    )

    /**
     * Represents the Create Estimate screen item in the bottom navigation bar.
     */
    data object CreateEstimate : BottomNavItem(
        route = com.hsgaragepecas.garagehub.navigation.CreateEstimate,
        title = "Cria Novo",
        icon = Icons.Default.Build
    )

    /**
     * Represents the Settings screen item in the bottom navigation bar.
     *
     */
    data object Settings : BottomNavItem(
        route = com.hsgaragepecas.garagehub.navigation.Settings,
        title = "Ajustes",
        icon = Icons.Default.Settings
    )
}