package com.hsgaragepecas.garagehub.ui.account.login

/**
 * Defines the contract for the login screen.
 */
interface LoginContract {
    /**
     * Represents the state of the login screen.
     *
     * @param isLoading Whether the screen is loading.
     * @param error The error message, if any.
     */
    data class LoginUiState(
        val isLoading: Boolean = false,
        val error: String? = null
    )

    /**
     * Represents an event that can be sent from the login screen.
     */
    sealed class LoginUiEvent {
        /**
         * Shows a toast message.
         *
         * @param message The message to be shown.
         */
        data class ShowToast(val message: String) : LoginUiEvent()
    }
}
