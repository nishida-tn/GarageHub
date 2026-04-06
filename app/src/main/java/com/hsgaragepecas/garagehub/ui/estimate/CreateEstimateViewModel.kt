package com.hsgaragepecas.garagehub.ui.estimate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hsgaragepecas.garagehub.data.model.EstimateFullDto
import com.hsgaragepecas.garagehub.data.model.EstimateItemDto
import com.hsgaragepecas.garagehub.data.remote.ViaCepService
import com.hsgaragepecas.garagehub.domain.repository.EstimateRepository
import com.hsgaragepecas.garagehub.domain.usecases.GenerateEstimatePdfUseCase
import com.hsgaragepecas.garagehub.ui.estimate.CreateEstimateContract.CreateEstimateUiEvent
import com.hsgaragepecas.garagehub.ui.estimate.CreateEstimateContract.CreateEstimateUiIntent
import com.hsgaragepecas.garagehub.ui.estimate.CreateEstimateContract.CreateEstimateUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel for the Create Estimate screen.
 * Handles the business logic and state management for creating a new estimate.
 *
 * @property estimateRepository The repository for estimate data.
 * @property viaCepService The service for fetching address from CEP.
 * @property generateEstimatePdfUseCase The use case for generating estimate PDF.
 */
@HiltViewModel
class CreateEstimateViewModel @Inject constructor(
    private val estimateRepository: EstimateRepository,
    private val viaCepService: ViaCepService,
    private val generateEstimatePdfUseCase: GenerateEstimatePdfUseCase
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
            is CreateEstimateUiIntent.OnItemTHValueChange -> _uiState.update { it.copy(itemTHValue = intent.value) }
            is CreateEstimateUiIntent.OnItemRiHChange -> _uiState.update { it.copy(itemRiH = intent.value) }
            is CreateEstimateUiIntent.OnItemRiHValueChange -> _uiState.update { it.copy(itemRiHValue = intent.value) }
            is CreateEstimateUiIntent.OnItemRHChange -> _uiState.update { it.copy(itemRH = intent.value) }
            is CreateEstimateUiIntent.OnItemRHValueChange -> _uiState.update { it.copy(itemRHValue = intent.value) }
            is CreateEstimateUiIntent.OnItemPHChange -> _uiState.update { it.copy(itemPH = intent.value) }
            is CreateEstimateUiIntent.OnItemPHValueChange -> _uiState.update { it.copy(itemPHValue = intent.value) }
            is CreateEstimateUiIntent.OnItemPartPriceChange -> _uiState.update { it.copy(itemPartPrice = intent.value) }
            CreateEstimateUiIntent.AddItem -> addItem()
            CreateEstimateUiIntent.SaveEstimate -> saveEstimate()
            CreateEstimateUiIntent.GeneratePdf -> generatePdf()
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
                // Handle error
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

    private fun addItem() {
        val state = _uiState.value
        val moHourValue = state.moHourValue.replace(",", ".").toDoubleOrNull() ?: 0.0
        val paintingHourValue = state.paintingHourValue.replace(",", ".").toDoubleOrNull() ?: 0.0

        val itemTHValue = state.itemTHValue.replace(",", ".").toDoubleOrNull() ?: 0.0
        val itemRiHValue = state.itemRiHValue.replace(",", ".").toDoubleOrNull() ?: 0.0
        val itemRHValue = state.itemRHValue.replace(",", ".").toDoubleOrNull() ?: 0.0
        val itemPHValue = state.itemPHValue.replace(",", ".").toDoubleOrNull() ?: 0.0

        val newItem = EstimateItemDto(
            partName = state.itemPartName,
            genuineCode = state.itemGenuineCode,
            unitPrice = state.itemPartPrice.replace(",", ".").toDoubleOrNull() ?: 0.0,
            quantity = 1,
            valueT = if (state.itemTH) itemTHValue * moHourValue else 0.0,
            valueRi = if (state.itemRiH) itemRiHValue * moHourValue else 0.0,
            valueR = if (state.itemRH) itemRHValue * moHourValue else 0.0,
            valueP = if (state.itemPH) itemPHValue * paintingHourValue else 0.0
        )
        val total = (newItem.unitPrice ?: 0.0) + (newItem.valueT ?: 0.0) + (newItem.valueRi ?: 0.0) + (newItem.valueR ?: 0.0) + (newItem.valueP ?: 0.0)
        val newItemWithTotal = newItem.copy(totalValue = total)

        _uiState.update { 
            it.copy(
                items = it.items + newItemWithTotal,
                itemPartName = "",
                itemGenuineCode = "",
                itemTH = false,
                itemTHValue = "",
                itemRiH = false,
                itemRiHValue = "",
                itemRH = false,
                itemRHValue = "",
                itemPH = false,
                itemPHValue = "",
                itemPartPrice = "0,00"
            )
        }
        viewModelScope.launch {
            _uiEvent.send(CreateEstimateUiEvent.ShowToast("Item adicionado"))
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

    private fun generatePdf() {
        val state = _uiState.value
        val estimate = EstimateFullDto(
            id = 0,
            clientName = state.clientName,
            clientTel = state.clientTel,
            clientAddress = state.clientAddress,
            clientNumber = state.clientNumber,
            clientCity = state.clientCity,
            clientUf = state.clientUf,
            vehiclePlate = state.vehiclePlate,
            vehicleBrand = state.vehicleBrand,
            vehicleModel = state.vehicleModel,
            vehicleYearFab = state.vehicleYearFab.toIntOrNull(),
            vehicleYearMod = state.vehicleYearMod.toIntOrNull()
        )

        viewModelScope.launch {
            _uiEvent.send(CreateEstimateUiEvent.ShowToast("Gerando PDF..."))
            try {
                val uri = withContext(Dispatchers.IO) {
                    generateEstimatePdfUseCase(estimate, state.items)
                }
                _uiEvent.send(CreateEstimateUiEvent.OpenUri(uri))
            } catch (e: Exception) {
                _uiEvent.send(CreateEstimateUiEvent.ShowToast("Erro ao gerar PDF: ${e.message}"))
            }
        }
    }
}
