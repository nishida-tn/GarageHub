package com.hsgaragepecas.garagehub.ui.account.forgot

/**
 * The contract for the forgot password screen.
 */
interface ForgotPasswordContract {
    /**
     * The state of the forgot password screen.
     *
     * @property email The email entered by the user.
     * @property isLoading Whether the screen is currently loading.
     * @property error The error message to be displayed, if any.
     * @property isSuccess Whether the password reset link was successfully sent.
     */
    data class State(
        val email: String = "",
        val isLoading: Boolean = false,
        val error: String? = null,
        val isSuccess: Boolean = false
    )

    /**
     * The intents for the forgot password screen.
     */
    sealed interface Intent {
        /**
         * Triggered when the email is changed.
         *
         * @property email The new email.
         */
        data class EmailChanged(val email: String) : Intent

        /**
         * Triggered when the send link button is clicked.
         */
        data object SendLinkClicked : Intent

        /**
         * Triggered when the back to login button is clicked.
         */
        data object BackToLoginClicked : Intent

        /**
         * Triggered when the error is dismissed.
         */
        data object ErrorDismissed : Intent
    }

    /**
     * The side effects for the forgot password screen.
     */
    sealed interface Effect {
        /**
         * Navigates back to the login screen.
         */
        data object NavigateBackToLogin : Effect

        /**
         * Shows a success message.
         */
        data object ShowSuccessMessage : Effect
    }
}
