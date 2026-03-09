package com.hsgaragepecas.garagehub.ui.account.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hsgaragepecas.garagehub.domain.Result
import com.hsgaragepecas.garagehub.domain.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImpl @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel(), LoginViewModel {

    private val _uiState = MutableStateFlow(LoginContract.LoginUiState())
    override val uiState = _uiState.asStateFlow()

    private val _sideEffect = Channel<LoginContract.LoginSideEffect>()
    override val sideEffect = _sideEffect.receiveAsFlow()

    override fun setEvent(event: LoginContract.LoginUiEvent) {
        when (event) {
            is LoginContract.LoginUiEvent.OnLoginClick -> {
                login(event.email, event.password)
            }
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = loginUseCase(email, password)) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _sideEffect.send(LoginContract.LoginSideEffect.NavigateToCreateEstimate)
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message
                        )
                    }
                    _sideEffect.send(
                        LoginContract.LoginSideEffect.ShowToast(
                            result.exception.message ?: "An unknown error occurred"
                        )
                    )
                }
            }
        }
    }
}
