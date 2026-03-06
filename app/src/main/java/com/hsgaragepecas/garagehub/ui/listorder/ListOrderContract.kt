package com.hsgaragepecas.garagehub.ui.listorder

import com.hsgaragepecas.garagehub.data.local.order.OrderEntity

/**
 * Defines the contract for the list order screen.
 */
interface ListOrderContract {
    /**
     * Represents the state of the list order screen.
     *
     * @param orders The list of orders.
     * @param isLoading Whether the screen is loading.
     * @param error The error message, if any.
     */
    data class ListOrderUiState(
        val orders: List<OrderEntity> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    /**
     * Represents an event that can be sent from the list order screen.
     */
    sealed class ListOrderUiEvent {
        /**
         * Shows a toast message.
         *
         * @param message The message to be shown.
         */
        data class ShowToast(val message: String) : ListOrderUiEvent()
    }
}
