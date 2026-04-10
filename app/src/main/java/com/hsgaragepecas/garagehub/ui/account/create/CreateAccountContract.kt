package com.hsgaragepecas.garagehub.ui.account.create

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Contract for the create account screen.
 */
interface CreateAccountContract {

    /**
     * The UI state of the create account screen.
     */
    data class CreateAccountUiState(
        val isLoading: Boolean = false,
        val error: String? = null
    )

    /**
     * The UI events of the create account screen.
     */
    sealed interface CreateAccountUiEvent {
        data class OnCreateAccountClick(
            val name: String,
            val email: String,
            val whatsapp: String,
            val password: String
        ) : CreateAccountUiEvent
    }

    /**
     * The side effects of the create account screen.
     */
    sealed interface CreateAccountSideEffect {
        data class ShowToast(val message: String) : CreateAccountSideEffect
        data object NavigateBack : CreateAccountSideEffect
        data object NavigateToMain : CreateAccountSideEffect
    }

    /**
     * The state flow of the UI state.
     */
    val uiState: StateFlow<CreateAccountUiState>

    /**
     * The flow of side effects.
     */
    val sideEffect: Flow<CreateAccountSideEffect>

    /**
     * Sets a UI event.
     *
     * @param event The UI event.
     */
    fun setEvent(event: CreateAccountUiEvent)
}
