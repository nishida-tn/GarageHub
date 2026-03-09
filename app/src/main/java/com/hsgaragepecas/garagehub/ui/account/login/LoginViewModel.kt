package com.hsgaragepecas.garagehub.ui.account.login

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow

/**
 * An interface for the login view model.
 */
interface LoginViewModel {
    /**
     * The state of the login screen.
     */
    val uiState: StateFlow<LoginContract.LoginUiState>
    /**
     * The events that can be sent from the login screen.
     */
    val uiEvent: Channel<LoginContract.LoginUiEvent>

    /**
     * Called when the login button is clicked.
     *
     * @param email The email.
     * @param password The password.
     */
    fun onLoginClick(email: String, password: String)
}
