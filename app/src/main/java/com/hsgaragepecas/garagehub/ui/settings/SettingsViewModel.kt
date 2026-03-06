package com.hsgaragepecas.garagehub.ui.settings

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow

interface SettingsViewModel {
    val uiState: StateFlow<SettingsUiState>
    val uiEvent: Channel<SettingsUiEvent>

    fun onFantasyNameChange(name: String)
    fun onCnpjChange(cnpj: String)
    fun onLandlineChange(landline: String)
    fun onWhatsappChange(whatsapp: String)
    fun onCepChange(cep: String)
    fun onAddressChange(address: String)
    fun onNumberChange(number: String)
    fun onNeighborhoodChange(neighborhood: String)
    fun onCityChange(city: String)
    fun onUfChange(uf: String)
    fun onComplementChange(complement: String)
    fun onLogoPathChange(path: String)
    fun onCurrentPasswordChange(password: String)
    fun onNewPasswordChange(password: String)
    fun onConfirmPasswordChange(password: String)
    fun onMechanicsRateChange(rate: String)
    fun onPaintingRateChange(rate: String)

    fun saveWorkshopData()
    fun changePassword()
    fun saveHourlyRates()
}
