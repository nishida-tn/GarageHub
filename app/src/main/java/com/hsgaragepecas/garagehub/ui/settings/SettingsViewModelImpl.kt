package com.hsgaragepecas.garagehub.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hsgaragepecas.garagehub.domain.usecases.CheckPasswordUseCase
import com.hsgaragepecas.garagehub.domain.usecases.SaveHourlyRatesUseCase
import com.hsgaragepecas.garagehub.domain.usecases.SaveSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModelImpl @Inject constructor(
    private val saveSettingsUseCase: SaveSettingsUseCase,
    private val checkPasswordUseCase: CheckPasswordUseCase,
    private val saveHourlyRatesUseCase: SaveHourlyRatesUseCase
) : ViewModel(), SettingsViewModel {

    private val _uiState = MutableStateFlow(SettingsUiState())
    override val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    override val uiEvent = Channel<SettingsUiEvent>(Channel.BUFFERED)

    override fun onFantasyNameChange(name: String) {
        _uiState.update { it.copy(fantasyName = name) }
    }

    override fun onCnpjChange(cnpj: String) {
        _uiState.update { it.copy(cnpj = cnpj) }
    }

    override fun onLandlineChange(landline: String) {
        _uiState.update { it.copy(landline = landline) }
    }

    override fun onWhatsappChange(whatsapp: String) {
        _uiState.update { it.copy(whatsapp = whatsapp) }
    }

    override fun onCepChange(cep: String) {
        _uiState.update { it.copy(cep = cep) }
    }

    override fun onAddressChange(address: String) {
        _uiState.update { it.copy(address = address) }
    }

    override fun onNumberChange(number: String) {
        _uiState.update { it.copy(number = number) }
    }

    override fun onNeighborhoodChange(neighborhood: String) {
        _uiState.update { it.copy(neighborhood = neighborhood) }
    }

    override fun onCityChange(city: String) {
        _uiState.update { it.copy(city = city) }
    }

    override fun onUfChange(uf: String) {
        _uiState.update { it.copy(uf = uf) }
    }

    override fun onComplementChange(complement: String) {
        _uiState.update { it.copy(complement = complement) }
    }

    override fun onLogoPathChange(path: String) {
        _uiState.update { it.copy(logoPath = path) }
    }

    override fun onCurrentPasswordChange(password: String) {
        _uiState.update { it.copy(currentPassword = password) }
    }

    override fun onNewPasswordChange(password: String) {
        _uiState.update { it.copy(newPassword = password) }
    }

    override fun onConfirmPasswordChange(password: String) {
        _uiState.update { it.copy(confirmPassword = password) }
    }

    override fun onMechanicsRateChange(rate: String) {
        _uiState.update { it.copy(mechanicsRate = rate) }
    }

    override fun onPaintingRateChange(rate: String) {
        _uiState.update { it.copy(paintingRate = rate) }
    }

    override fun saveWorkshopData() {
        viewModelScope.launch {
            val state = _uiState.value
            val result = saveSettingsUseCase(
                fantasyName = state.fantasyName,
                email = state.email,
                cnpj = state.cnpj,
                landline = state.landline,
                whatsapp = state.whatsapp,
                cep = state.cep,
                number = state.number,
                neighborhood = state.neighborhood,
                city = state.city,
                uf = state.uf,
                complement = state.complement,
                logoPath = state.logoPath
            )
            if (result) {
                _uiState.update { it.copy(isWorkshopDataSaved = true, workshopDataError = null) }
                uiEvent.send(SettingsUiEvent.ShowToast("Data saved successfully"))
            } else {
                _uiState.update { it.copy(isWorkshopDataSaved = false, workshopDataError = "Invalid data") }
            }
        }
    }

    override fun changePassword() {
        viewModelScope.launch {
            val state = _uiState.value
            val storedPassword = "" // Mocked
            val result = checkPasswordUseCase(
                currentPassword = state.currentPassword,
                newPassword = state.newPassword,
                confirmPassword = state.confirmPassword,
                storedPassword = storedPassword
            )
            when (result) {
                CheckPasswordUseCase.PasswordResult.Success -> {
                    _uiState.update { it.copy(isPasswordChanged = true, passwordError = null) }
                    uiEvent.send(SettingsUiEvent.ShowToast("Password changed successfully"))
                }
                else -> {
                    val errorMessage = when (result) {
                        CheckPasswordUseCase.PasswordResult.InvalidCurrentPassword -> "Invalid current password"
                        CheckPasswordUseCase.PasswordResult.EmptyNewPassword -> "New password cannot be empty"
                        CheckPasswordUseCase.PasswordResult.NewPasswordTooShort -> "New password is too short"
                        CheckPasswordUseCase.PasswordResult.PasswordUnchanged -> "New password must be different"
                        CheckPasswordUseCase.PasswordResult.PasswordsDoNotMatch -> "Passwords do not match"
                        else -> "An unknown error occurred"
                    }
                    _uiState.update { it.copy(isPasswordChanged = false, passwordError = errorMessage) }
                }
            }
        }
    }

    override fun saveHourlyRates() {
        viewModelScope.launch {
            val state = _uiState.value
            val result = saveHourlyRatesUseCase(
                mechanicsRate = state.mechanicsRate,
                paintingRate = state.paintingRate
            )
            when (result) {
                SaveHourlyRatesUseCase.Result.Success -> {
                    _uiState.update { it.copy(isRatesSaved = true, ratesError = null) }
                    uiEvent.send(SettingsUiEvent.ShowToast("Rates saved successfully"))
                }
                else -> {
                    val errorMessage = when (result) {
                        SaveHourlyRatesUseCase.Result.EmptyMechanicsRate -> "Mechanics rate is empty"
                        SaveHourlyRatesUseCase.Result.InvalidMechanicsRate -> "Invalid mechanics rate"
                        SaveHourlyRatesUseCase.Result.EmptyPaintingRate -> "Painting rate is empty"
                        SaveHourlyRatesUseCase.Result.InvalidPaintingRate -> "Invalid painting rate"
                        else -> "An unknown error occurred"
                    }
                    _uiState.update { it.copy(isRatesSaved = false, ratesError = errorMessage) }
                }
            }
        }
    }
}
