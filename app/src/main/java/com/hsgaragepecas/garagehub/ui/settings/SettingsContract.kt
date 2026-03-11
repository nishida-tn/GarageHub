package com.hsgaragepecas.garagehub.ui.settings

/**
 * Represents the state of the settings screen.
 */
data class SettingsUiState(
    /**
     * The fantasy name of the workshop.
     */
    val fantasyName: String = "",
    /**
     * The email of the workshop.
     */
    val email: String = "funilariaepinturahs@gmail.com", // Assuming it's fixed for now
    /**
     * The CNPJ of the workshop.
     */
    val cnpj: String = "",
    /**
     * The landline of the workshop.
     */
    val landline: String = "",
    /**
     * The WhatsApp number of the workshop.
     */
    val whatsapp: String = "",
    /**
     * The CEP of the workshop.
     */
    val cep: String = "",
    /**
     * The address of the workshop.
     */
    val address: String = "",
    /**
     * The number of the workshop's address.
     */
    val number: String = "",
    /**
     * The neighborhood of the workshop.
     */
    val neighborhood: String = "",
    /**
     * The city of the workshop.
     */
    val city: String = "",
    /**
     * The UF of the workshop.
     */
    val uf: String = "",
    /**
     * The complement of the workshop's address.
     */
    val complement: String = "",
    /**
     * The path to the workshop's logo.
     */
    val logoPath: String? = null,

    // Security
    /**
     * The current password.
     */
    val currentPassword: String = "",
    /**
     * The new password.
     */
    val newPassword: String = "",
    /**
     * The confirmation of the new password.
     */
    val confirmPassword: String = "",

    // Labor
    /**
     * The mechanics rate.
     */
    val mechanicsRate: String = "",
    /**
     * The painting rate.
     */
    val paintingRate: String = "",

    // Feedback
    /**
     * Whether the workshop data has been saved.
     */
    val isWorkshopDataSaved: Boolean = false,
    /**
     * The error message for the workshop data.
     */
    val workshopDataError: String? = null,
    /**
     * Whether the password has been changed.
     */
    val isPasswordChanged: Boolean = false,
    /**
     * The error message for the password.
     */
    val passwordError: String? = null,
    /**
     * Whether the rates have been saved.
     */
    val isRatesSaved: Boolean = false,
    /**
     * The error message for the rates.
     */
    val ratesError: String? = null
)

/**
 * Represents an event that can be sent from the settings screen.
 */
sealed class SettingsUiEvent {
    /**
     * Shows a toast message.
     *
     * @param message The message to be shown.
     */
    data class ShowToast(val message: String) : SettingsUiEvent()

    /**
     * Navigates to the login screen.
     */
    data object NavigateToLogin : SettingsUiEvent()
}
