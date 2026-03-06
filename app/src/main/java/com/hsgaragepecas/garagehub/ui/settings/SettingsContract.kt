package com.hsgaragepecas.garagehub.ui.settings

data class SettingsUiState(
    // Workshop Data
    val fantasyName: String = "",
    val email: String = "funilariaepinturahs@gmail.com", // Assuming it's fixed for now
    val cnpj: String = "",
    val landline: String = "",
    val whatsapp: String = "",
    val cep: String = "",
    val address: String = "",
    val number: String = "",
    val neighborhood: String = "",
    val city: String = "",
    val uf: String = "",
    val complement: String = "",
    val logoPath: String? = null,

    // Security
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",

    // Labor
    val mechanicsRate: String = "",
    val paintingRate: String = "",

    // Feedback
    val isWorkshopDataSaved: Boolean = false,
    val workshopDataError: String? = null,
    val isPasswordChanged: Boolean = false,
    val passwordError: String? = null,
    val isRatesSaved: Boolean = false,
    val ratesError: String? = null
)

sealed class SettingsUiEvent {
    data class ShowToast(val message: String) : SettingsUiEvent()
}
