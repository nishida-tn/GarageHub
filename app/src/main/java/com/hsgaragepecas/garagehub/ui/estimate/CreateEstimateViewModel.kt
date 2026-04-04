package com.hsgaragepecas.garagehub.ui.estimate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hsgaragepecas.garagehub.data.remote.ViaCepService
import com.hsgaragepecas.garagehub.domain.repository.EstimateRepository
import com.hsgaragepecas.garagehub.ui.estimate.CreateEstimateContract.CreateEstimateUiEvent
import com.hsgaragepecas.garagehub.ui.estimate.CreateEstimateContract.CreateEstimateUiIntent
import com.hsgaragepecas.garagehub.ui.estimate.CreateEstimateContract.CreateEstimateUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Create Estimate screen.
 * Handles the business logic and state management for creating a new estimate.
 */
@HiltViewModel
class CreateEstimateViewModel @Inject constructor(
    private val estimateRepository: EstimateRepository,
    private val viaCepService: ViaCepService
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEstimateUiState())
    val uiState: StateFlow<CreateEstimateUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<CreateEstimateUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    private var fetchAddressJob: Job? = null

    /**
     * Handles user intents and updates the UI state accordingly.
     *
     * @param intent The user intent to handle.
     */
    fun onIntent(intent: CreateEstimateUiIntent) {
        when (intent) {
            is CreateEstimateUiIntent.OnMoHourValueChange -> _uiState.update { it.copy(moHourValue = intent.value) }
            is CreateEstimateUiIntent.OnPaintingHourValueChange -> _uiState.update { it.copy(paintingHourValue = intent.value) }
            is CreateEstimateUiIntent.OnClientNameChange -> _uiState.update { it.copy(clientName = intent.value) }
            is CreateEstimateUiIntent.OnClientTelChange -> _uiState.update { it.copy(clientTel = intent.value) }
            is CreateEstimateUiIntent.OnClientWhatsChange -> _uiState.update { it.copy(clientWhats = intent.value) }
            is CreateEstimateUiIntent.OnClientCepChange -> {
                val cep = intent.value.replace(Regex("\\D"), "")
                _uiState.update { it.copy(clientCep = intent.value) }
                if (cep.length == 8) {
                    fetchAddress(cep)
                } else {
                    cancelFetchAndClearAddress()
                }
            }
            is CreateEstimateUiIntent.OnClientAddressChange -> _uiState.update { it.copy(clientAddress = intent.value) }
            is CreateEstimateUiIntent.OnClientNumberChange -> _uiState.update { it.copy(clientNumber = intent.value) }
            is CreateEstimateUiIntent.OnClientNeighborhoodChange -> _uiState.update { it.copy(clientNeighborhood = intent.value) }
            is CreateEstimateUiIntent.OnClientCityChange -> _uiState.update { it.copy(clientCity = intent.value) }
            is CreateEstimateUiIntent.OnClientUfChange -> _uiState.update { it.copy(clientUf = intent.value) }
            is CreateEstimateUiIntent.OnClientComplementChange -> _uiState.update { it.copy(clientComplement = intent.value) }
            is CreateEstimateUiIntent.OnVehiclePlateChange -> _uiState.update { it.copy(vehiclePlate = intent.value) }
            is CreateEstimateUiIntent.OnVehicleBrandChange -> _uiState.update { it.copy(vehicleBrand = intent.value) }
            is CreateEstimateUiIntent.OnVehicleModelChange -> _uiState.update { it.copy(vehicleModel = intent.value) }
            is CreateEstimateUiIntent.OnVehicleYearFabChange -> _uiState.update { it.copy(vehicleYearFab = intent.value) }
            is CreateEstimateUiIntent.OnVehicleYearModChange -> _uiState.update { it.copy(vehicleYearMod = intent.value) }
            is CreateEstimateUiIntent.OnVehicleChassisChange -> _uiState.update { it.copy(vehicleChassis = intent.value) }
            is CreateEstimateUiIntent.OnVehicleFuelChange -> _uiState.update { it.copy(vehicleFuel = intent.value) }
            is CreateEstimateUiIntent.OnVehicleAirChange -> _uiState.update { it.copy(vehicleAir = intent.value) }
            is CreateEstimateUiIntent.OnVehicleSteeringChange -> _uiState.update { it.copy(vehicleSteering = intent.value) }
            is CreateEstimateUiIntent.OnVehicleTransmissionChange -> _uiState.update { it.copy(vehicleTransmission = intent.value) }
            is CreateEstimateUiIntent.OnAddVehiclePhotos -> _uiState.update { it.copy(vehiclePhotos = it.vehiclePhotos + intent.uris) }
            is CreateEstimateUiIntent.OnItemGenuineCodeChange -> _uiState.update { it.copy(itemGenuineCode = intent.value) }
            is CreateEstimateUiIntent.OnItemPartNameChange -> _uiState.update { it.copy(itemPartName = intent.value) }
            is CreateEstimateUiIntent.OnItemTHChange -> _uiState.update { it.copy(itemTH = intent.value) }
            is CreateEstimateUiIntent.OnItemRiHChange -> _uiState.update { it.copy(itemRiH = intent.value) }
            is CreateEstimateUiIntent.OnItemRHChange -> _uiState.update { it.copy(itemRH = intent.value) }
            is CreateEstimateUiIntent.OnItemPHChange -> _uiState.update { it.copy(itemPH = intent.value) }
            is CreateEstimateUiIntent.OnItemPartPriceChange -> _uiState.update { it.copy(itemPartPrice = intent.value) }
            CreateEstimateUiIntent.SaveEstimate -> saveEstimate()
        }
    }

    private fun fetchAddress(cep: String) {
        fetchAddressJob?.cancel()
        fetchAddressJob = viewModelScope.launch {
            try {
                val response = viaCepService.getAddress(cep)
                if (response.erro != true) {
                    _uiState.update {
                        it.copy(
                            clientAddress = response.logradouro ?: "",
                            clientNeighborhood = response.bairro ?: "",
                            clientCity = response.localidade ?: "",
                            clientUf = response.uf ?: ""
                        )
                    }
                }
            } catch (e: Exception) {
                // Handle error or silent catch as in JS snippet
            }
        }
    }

    private fun cancelFetchAndClearAddress() {
        fetchAddressJob?.cancel()
        _uiState.update {
            it.copy(
                clientAddress = "",
                clientNeighborhood = "",
                clientCity = "",
                clientUf = ""
            )
        }
    }

    private fun saveEstimate() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            // Implementation for saving estimate will go here
            _uiState.update { it.copy(isSaving = false) }
            _uiEvent.send(CreateEstimateUiEvent.ShowToast("Estimate created successfully"))
            _uiEvent.send(CreateEstimateUiEvent.NavigateBack)
        }
    }
}
