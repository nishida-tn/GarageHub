package com.hsgaragepecas.garagehub.ui.account.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hsgaragepecas.garagehub.domain.Result
import com.hsgaragepecas.garagehub.domain.usecases.SignupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel implementation for the create account screen.
 */
@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val signupUseCase: SignupUseCase
) : ViewModel(), CreateAccountContract {

    private val _uiState = MutableStateFlow(CreateAccountContract.CreateAccountUiState())
    override val uiState = _uiState.asStateFlow()

    private val _sideEffect = Channel<CreateAccountContract.CreateAccountSideEffect>()
    override val sideEffect = _sideEffect.receiveAsFlow()

    override fun setEvent(event: CreateAccountContract.CreateAccountUiEvent) {
        when (event) {
            is CreateAccountContract.CreateAccountUiEvent.OnCreateAccountClick -> {
                signup(event.name, event.email, event.whatsapp, event.password)
            }
        }
    }

    private fun signup(name: String, email: String, whatsapp: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = signupUseCase(email, password, name, whatsapp)) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _sideEffect.send(CreateAccountContract.CreateAccountSideEffect.ShowToast("Conta criada com sucesso!"))
                    _sideEffect.send(CreateAccountContract.CreateAccountSideEffect.NavigateToMain)
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message
                        )
                    }
                    _sideEffect.send(
                        CreateAccountContract.CreateAccountSideEffect.ShowToast(
                            result.exception.message ?: "Erro ao criar conta"
                        )
                    )
                }
            }
        }
    }
}
