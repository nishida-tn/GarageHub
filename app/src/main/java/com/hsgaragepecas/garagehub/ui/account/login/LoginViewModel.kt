package com.hsgaragepecas.garagehub.ui.account.login

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow

/**
 * Defines the contract for the login view model.
 */
interface LoginViewModel {
    /**
     * The UI state of the login screen.
     */
    val uiState: StateFlow<LoginContract.LoginUiState>

    /**
     * The side effects of the login screen.
     */
    val sideEffect: Flow<LoginContract.LoginSideEffect>

    /**
     * Sends an event to the view model.
     *
     * @param event The event to be sent.
     */
    fun setEvent(event: LoginContract.LoginUiEvent)
}
