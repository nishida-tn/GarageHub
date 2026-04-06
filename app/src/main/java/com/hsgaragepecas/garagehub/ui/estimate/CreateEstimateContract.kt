package com.hsgaragepecas.garagehub.ui.estimate

import android.net.Uri
import com.hsgaragepecas.garagehub.data.model.EstimateItemDto
import com.hsgaragepecas.garagehub.data.model.FipeBrandDto
import com.hsgaragepecas.garagehub.data.model.FipeModelDto
import com.hsgaragepecas.garagehub.data.model.FipeYearDto

/**
 * The contract for the create estimate screen.
 */
interface CreateEstimateContract {

    /**
     * The state of the create estimate screen.
     */
    data class CreateEstimateUiState(
        val isLoading: Boolean = false,
        val moHourValue: String = "80,00",
        val paintingHourValue: String = "100,00",
        // Customer Data
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
        // Vehicle Data
        val vehiclePlate: String = "",
        val vehicleBrand: String = "",
        val brands: List<FipeBrandDto> = emptyList(),
        val selectedBrand: FipeBrandDto? = null,
        val vehicleModel: String = "",
        val models: List<FipeModelDto> = emptyList(),
        val selectedModel: FipeModelDto? = null,
        val years: List<FipeYearDto> = emptyList(),
        val selectedYear: FipeYearDto? = null,
        val vehicleYearFab: String = "",
        val vehicleYearMod: String = "",
        val vehicleChassis: String = "",
        val vehicleFuel: String = "",
        val vehicleAir: String = "",
        val vehicleSteering: String = "",
        val vehicleTransmission: String = "",
        val vehiclePhotos: List<Uri> = emptyList(),
        // Items
        val items: List<EstimateItemDto> = emptyList(),
        // Item Data (Current item being added)
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
        val itemTotal: String = "0,00",
        val error: String? = null,
        val isSaving: Boolean = false
    )

    /**
     * The events for the create estimate screen.
     */
    sealed interface CreateEstimateUiEvent {
        data class ShowToast(val message: String) : CreateEstimateUiEvent
        data object NavigateBack : CreateEstimateUiEvent
        data class OpenUri(val uri: Uri) : CreateEstimateUiEvent
    }

    /**
     * The intents for the create estimate screen.
     */
    sealed interface CreateEstimateUiIntent {
        data class OnMoHourValueChange(val value: String) : CreateEstimateUiIntent
        data class OnPaintingHourValueChange(val value: String) : CreateEstimateUiIntent
        // Customer Data Intents
        data class OnClientNameChange(val value: String) : CreateEstimateUiIntent
        data class OnClientTelChange(val value: String) : CreateEstimateUiIntent
        data class OnClientWhatsChange(val value: String) : CreateEstimateUiIntent
        data class OnClientCepChange(val value: String) : CreateEstimateUiIntent
        data class OnClientAddressChange(val value: String) : CreateEstimateUiIntent
        data class OnClientNumberChange(val value: String) : CreateEstimateUiIntent
        data class OnClientNeighborhoodChange(val value: String) : CreateEstimateUiIntent
        data class OnClientCityChange(val value: String) : CreateEstimateUiIntent
        data class OnClientUfChange(val value: String) : CreateEstimateUiIntent
        data class OnClientComplementChange(val value: String) : CreateEstimateUiIntent
        // Vehicle Data Intents
        data class OnVehiclePlateChange(val value: String) : CreateEstimateUiIntent
        data class OnVehicleBrandChange(val value: String) : CreateEstimateUiIntent
        data class OnBrandSelected(val brand: FipeBrandDto) : CreateEstimateUiIntent
        data class OnVehicleModelChange(val value: String) : CreateEstimateUiIntent
        data class OnModelSelected(val model: FipeModelDto) : CreateEstimateUiIntent
        data class OnYearSelected(val year: FipeYearDto) : CreateEstimateUiIntent
        data class OnVehicleYearFabChange(val value: String) : CreateEstimateUiIntent
        data class OnVehicleYearModChange(val value: String) : CreateEstimateUiIntent
        data class OnVehicleChassisChange(val value: String) : CreateEstimateUiIntent
        data class OnVehicleFuelChange(val value: String) : CreateEstimateUiIntent
        data class OnVehicleAirChange(val value: String) : CreateEstimateUiIntent
        data class OnVehicleSteeringChange(val value: String) : CreateEstimateUiIntent
        data class OnVehicleTransmissionChange(val value: String) : CreateEstimateUiIntent
        data class OnAddVehiclePhotos(val uris: List<Uri>) : CreateEstimateUiIntent
        // Item Data Intents
        data class OnItemGenuineCodeChange(val value: String) : CreateEstimateUiIntent
        data class OnItemPartNameChange(val value: String) : CreateEstimateUiIntent
        data class OnItemTHChange(val value: Boolean) : CreateEstimateUiIntent
        data class OnItemTHValueChange(val value: String) : CreateEstimateUiIntent
        data class OnItemRiHChange(val value: Boolean) : CreateEstimateUiIntent
        data class OnItemRiHValueChange(val value: String) : CreateEstimateUiIntent
        data class OnItemRHChange(val value: Boolean) : CreateEstimateUiIntent
        data class OnItemRHValueChange(val value: String) : CreateEstimateUiIntent
        data class OnItemPHChange(val value: Boolean) : CreateEstimateUiIntent
        data class OnItemPHValueChange(val value: String) : CreateEstimateUiIntent
        data class OnItemPartPriceChange(val value: String) : CreateEstimateUiIntent
        
        data object AddItem : CreateEstimateUiIntent
        data object SaveEstimate : CreateEstimateUiIntent
        data object GeneratePdf : CreateEstimateUiIntent
    }
}
