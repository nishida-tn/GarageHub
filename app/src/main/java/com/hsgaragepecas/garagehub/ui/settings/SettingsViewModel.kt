package com.hsgaragepecas.garagehub.ui.settings

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow

/**
 * An interface for the settings view model.
 */
interface SettingsViewModel {
    /**
     * The state of the settings screen.
     */
    val uiState: StateFlow<SettingsUiState>
    /**
     * The events that can be sent from the settings screen.
     */
    val uiEvent: Channel<SettingsUiEvent>

    /**
     * Called when the fantasy name changes.
     *
     * @param name The new fantasy name.
     */
    fun onFantasyNameChange(name: String)
    /**
     * Called when the CNPJ changes.
     *
     * @param cnpj The new CNPJ.
     */
    fun onCnpjChange(cnpj: String)
    /**
     * Called when the landline changes.
     *
     * @param landline The new landline.
     */
    fun onLandlineChange(landline: String)
    /**
     * Called when the WhatsApp number changes.
     *
     * @param whatsapp The new WhatsApp number.
     */
    fun onWhatsappChange(whatsapp: String)
    /**
     * Called when the CEP changes.
     *
     * @param cep The new CEP.
     */
    fun onCepChange(cep: String)
    /**
     * Called when the address changes.
     *
     * @param address The new address.
     */
    fun onAddressChange(address: String)
    /**
     * Called when the number changes.
     *
     * @param number The new number.
     */
    fun onNumberChange(number: String)
    /**
     * Called when the neighborhood changes.
     *
     * @param neighborhood The new neighborhood.
     */
    fun onNeighborhoodChange(neighborhood: String)
    /**
     * Called when the city changes.
     *
     * @param city The new city.
     */
    fun onCityChange(city: String)
    /**
     * Called when the UF changes.
     *
     * @param uf The new UF.
     */
    fun onUfChange(uf: String)
    /**
     * Called when the complement changes.
     *
     * @param complement The new complement.
     */
    fun onComplementChange(complement: String)
    /**
     * Called when the logo path changes.
     *
     * @param path The new logo path.
     */
    fun onLogoPathChange(path: String)
    /**
     * Called when the current password changes.
     *
     * @param password The new current password.
     */
    fun onCurrentPasswordChange(password: String)
    /**
     * Called when the new password changes.
     *
     * @param password The new new password.
     */
    fun onNewPasswordChange(password: String)
    /**
     * Called when the confirm password changes.
     *
     * @param password The new confirm password.
     */
    fun onConfirmPasswordChange(password: String)
    /**
     * Called when the mechanics rate changes.
     *
     * @param rate The new mechanics rate.
     */
    fun onMechanicsRateChange(rate: String)
    /**
     * Called when the painting rate changes.
     *
     * @param rate The new painting rate.
     */
    fun onPaintingRateChange(rate: String)

    /**
     * Saves the workshop data.
     */
    fun saveWorkshopData()
    /**
     * Changes the password.
     */
    fun changePassword()
    /**
     * Saves the hourly rates.
     */
    fun saveHourlyRates()
    /**
     * Logs the user out.
     */
    fun logout()
}
