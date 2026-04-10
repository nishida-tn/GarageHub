package com.hsgaragepecas.garagehub.ui.estimate.edit

import android.net.Uri
import com.hsgaragepecas.garagehub.data.model.EstimateFullDto
import com.hsgaragepecas.garagehub.data.model.EstimateItemDto
import com.hsgaragepecas.garagehub.data.model.FipeBrandDto
import com.hsgaragepecas.garagehub.data.model.FipeModelDto
import com.hsgaragepecas.garagehub.data.model.FipeYearDto
import com.hsgaragepecas.garagehub.data.model.ProposalDto

/**
 * The contract for the edit estimate screen.
 */
interface EditEstimateContract {

    /**
     * The state of the edit estimate screen.
     */
    data class EditEstimateUiState(
        val isLoading: Boolean = false,
        val estimate: EstimateFullDto? = null,
        val items: List<EstimateItemDto> = emptyList(),
        val photos: List<String> = emptyList(), // Photos from server
        val vehiclePhotos: List<Uri> = emptyList(), // New photos taken locally
        val proposals: List<ProposalDto> = emptyList(),
        val error: String? = null,
        val isSaving: Boolean = false,
        
        // Form Data
        val moHourValue: String = "80,00",
        val paintingHourValue: String = "100,00",
        val clientName: String = "",
        val clientTel: String = "",
        val clientWhats: String = "",
        val clientCep: String = "",
        val clientAddress: String = "",
        val clientNumber: String = "",
        val clientNeighborhood: String = "",
        val clientCity: String = "",
        val clientUf: String = "",
        val clientComplement: String = "",
        val vehiclePlate: String = "",
        val vehicleBrand: String = "",
        val vehicleModel: String = "",
        val vehicleYearFab: String = "",
        val vehicleYearMod: String = "",
        val vehicleChassis: String = "",
        val vehicleFuel: String = "",
        val vehicleAir: String = "",
        val vehicleSteering: String = "",
        val vehicleTransmission: String = "",
        
        // Fipe Data
        val brands: List<FipeBrandDto> = emptyList(),
        val selectedBrand: FipeBrandDto? = null,
        val models: List<FipeModelDto> = emptyList(),
        val selectedModel: FipeModelDto? = null,
        val years: List<FipeYearDto> = emptyList(),
        val selectedYear: FipeYearDto? = null,
        
        // New Item Data
        val itemGenuineCode: String = "",
        val itemPartName: String = "",
        val itemTH: Boolean = false,
        val itemTHValue: String = "",
        val itemRiH: Boolean = false,
        val itemRiHValue: String = "",
        val itemRH: Boolean = false,
        val itemRHValue: String = "",
        val itemPH: Boolean = false,
        val itemPHValue: String = "",
        val itemPartPrice: String = "0,00",
        val itemTotal: String = "0,00"
    )

    /**
     * The events for the edit estimate screen.
     */
    sealed interface EditEstimateUiEvent {
        /**
         * Event to show a toast message.
         *
         * @param message The message to show.
         */
        data class ShowToast(val message: String) : EditEstimateUiEvent

        /**
         * Event to navigate back.
         */
        data object NavigateBack : EditEstimateUiEvent

        /**
         * Event to open a file URI.
         *
         * @param uri The URI to open.
         */
        data class OpenUri(val uri: Uri) : EditEstimateUiEvent
    }

    /**
     * The intents for the edit estimate screen.
     */
    sealed interface EditEstimateUiIntent {
        /**
         * Intent to load the estimate details.
         *
         * @param estimateId The ID of the estimate.
         */
        data class LoadEstimate(val estimateId: Int) : EditEstimateUiIntent

        /**
         * Intent to save the estimate.
         */
        data object SaveEstimate : EditEstimateUiIntent

        /**
         * Intent to delete an item from the estimate.
         *
         * @param itemId The ID of the item.
         */
        data class DeleteItem(val itemId: Int) : EditEstimateUiIntent

        /**
         * Intent to add an item to the estimate.
         */
        data object AddItem : EditEstimateUiIntent

        /**
         * Intent to generate a PDF for the estimate.
         */
        data object GeneratePdf : EditEstimateUiIntent

        /**
         * Intent to send the estimate via WhatsApp.
         */
        data object SendWhatsApp : EditEstimateUiIntent

        /**
         * Intent to make an order from the estimate.
         */
        data object MakeOrder : EditEstimateUiIntent

        /**
         * Intent to create a demand from the estimate.
         */
        data object CreateDemand : EditEstimateUiIntent
        
        // Field update intents
        data class OnMoHourValueChange(val value: String) : EditEstimateUiIntent
        data class OnPaintingHourValueChange(val value: String) : EditEstimateUiIntent
        data class OnClientNameChange(val value: String) : EditEstimateUiIntent
        data class OnClientTelChange(val value: String) : EditEstimateUiIntent
        data class OnClientWhatsChange(val value: String) : EditEstimateUiIntent
        data class OnClientCepChange(val value: String) : EditEstimateUiIntent
        data class OnClientAddressChange(val value: String) : EditEstimateUiIntent
        data class OnClientNumberChange(val value: String) : EditEstimateUiIntent
        data class OnClientNeighborhoodChange(val value: String) : EditEstimateUiIntent
        data class OnClientCityChange(val value: String) : EditEstimateUiIntent
        data class OnClientUfChange(val value: String) : EditEstimateUiIntent
        data class OnClientComplementChange(val value: String) : EditEstimateUiIntent
        data class OnVehiclePlateChange(val value: String) : EditEstimateUiIntent
        data class OnVehicleBrandChange(val value: String) : EditEstimateUiIntent
        data class OnBrandSelected(val brand: FipeBrandDto) : EditEstimateUiIntent
        data class OnVehicleModelChange(val value: String) : EditEstimateUiIntent
        data class OnModelSelected(val model: FipeModelDto) : EditEstimateUiIntent
        data class OnYearSelected(val year: FipeYearDto) : EditEstimateUiIntent
        data class OnVehicleYearFabChange(val value: String) : EditEstimateUiIntent
        data class OnVehicleYearModChange(val value: String) : EditEstimateUiIntent
        data class OnVehicleChassisChange(val value: String) : EditEstimateUiIntent
        data class OnVehicleFuelChange(val value: String) : EditEstimateUiIntent
        data class OnVehicleAirChange(val value: String) : EditEstimateUiIntent
        data class OnVehicleSteeringChange(val value: String) : EditEstimateUiIntent
        data class OnVehicleTransmissionChange(val value: String) : EditEstimateUiIntent
        data class OnAddVehiclePhotos(val uris: List<Uri>) : EditEstimateUiIntent
        
        // Item intents
        data class OnItemGenuineCodeChange(val value: String) : EditEstimateUiIntent
        data class OnItemPartNameChange(val value: String) : EditEstimateUiIntent
        data class OnItemTHChange(val value: Boolean) : EditEstimateUiIntent
        data class OnItemTHValueChange(val value: String) : EditEstimateUiIntent
        data class OnItemRiHChange(val value: Boolean) : EditEstimateUiIntent
        data class OnItemRiHValueChange(val value: String) : EditEstimateUiIntent
        data class OnItemRHChange(val value: Boolean) : EditEstimateUiIntent
        data class OnItemRHValueChange(val value: String) : EditEstimateUiIntent
        data class OnItemPHChange(val value: Boolean) : EditEstimateUiIntent
        data class OnItemPHValueChange(val value: String) : EditEstimateUiIntent
        data class OnItemPartPriceChange(val value: String) : EditEstimateUiIntent
    }
}
