package com.hsgaragepecas.garagehub.ui.account.forgot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hsgaragepecas.garagehub.domain.Result
import com.hsgaragepecas.garagehub.domain.usecases.ForgotPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The view model for the forgot password screen.
 *
 * @property forgotPasswordUseCase The use case for resetting the password.
 */
@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordContract.State())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<ForgotPasswordContract.Effect>()
    val effect = _effect.receiveAsFlow()

    /**
     * Handles the given intent.
     *
     * @param intent The intent to be handled.
     */
    fun onIntent(intent: ForgotPasswordContract.Intent) {
        when (intent) {
            is ForgotPasswordContract.Intent.EmailChanged -> {
                _uiState.update { it.copy(email = intent.email) }
            }
            is ForgotPasswordContract.Intent.SendLinkClicked -> {
                sendForgotPasswordLink()
            }
            is ForgotPasswordContract.Intent.BackToLoginClicked -> {
                viewModelScope.launch {
                    _effect.send(ForgotPasswordContract.Effect.NavigateBackToLogin)
                }
            }
            is ForgotPasswordContract.Intent.ErrorDismissed -> {
                _uiState.update { it.copy(error = null) }
            }
        }
    }

    private fun sendForgotPasswordLink() {
        val email = _uiState.value.email
        if (email.isBlank()) {
            _uiState.update { it.copy(error = "Email cannot be empty") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = forgotPasswordUseCase(email)) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    _effect.send(ForgotPasswordContract.Effect.ShowSuccessMessage)
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.exception.message) }
                }
            }
        }
    }
}
