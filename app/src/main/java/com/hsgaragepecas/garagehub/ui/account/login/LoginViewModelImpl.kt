package com.hsgaragepecas.garagehub.ui.account.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * The implementation of the login view model.
 */
@HiltViewModel
class LoginViewModelImpl @Inject constructor() : ViewModel(), LoginViewModel {

    private val _uiState = MutableStateFlow(LoginContract.LoginUiState())
    override val uiState = _uiState.asStateFlow()

    override val uiEvent = Channel<LoginContract.LoginUiEvent>(Channel.BUFFERED)

    override fun onLoginClick(email: String, password: String) {
        // TODO: Implement login logic
    }
}
