package com.hsgaragepecas.garagehub.ui.estimate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hsgaragepecas.garagehub.data.model.EstimateFullDto
import com.hsgaragepecas.garagehub.data.model.EstimateItemDto
import com.hsgaragepecas.garagehub.data.model.EstimateUpdateRequest
import com.hsgaragepecas.garagehub.data.model.FipeBrandDto
import com.hsgaragepecas.garagehub.data.model.FipeModelDto
import com.hsgaragepecas.garagehub.data.model.FipeYearDto
import com.hsgaragepecas.garagehub.data.remote.ViaCepService
import com.hsgaragepecas.garagehub.domain.repository.EstimateRepository
import com.hsgaragepecas.garagehub.domain.repository.FipeRepository
import com.hsgaragepecas.garagehub.domain.usecases.CreateEstimateUseCase
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
 */
@HiltViewModel
class CreateEstimateViewModel @Inject constructor(
    private val estimateRepository: EstimateRepository,
    private val fipeRepository: FipeRepository,
    private val viaCepService: ViaCepService,
    private val generateEstimatePdfUseCase: GenerateEstimatePdfUseCase,
    private val createEstimateUseCase: CreateEstimateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEstimateUiState())
    val uiState: StateFlow<CreateEstimateUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<CreateEstimateUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    private var fetchAddressJob: Job? = null
    private var suggestionJob: Job? = null

    init {
        loadBrands()
    }

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
            is CreateEstimateUiIntent.OnBrandSelected -> selectBrand(intent.brand)
            is CreateEstimateUiIntent.OnVehicleModelChange -> _uiState.update { it.copy(vehicleModel = intent.value) }
            is CreateEstimateUiIntent.OnModelSelected -> selectModel(intent.model)
            is CreateEstimateUiIntent.OnYearSelected -> selectYear(intent.year)
            is CreateEstimateUiIntent.OnVehicleYearFabChange -> _uiState.update { it.copy(vehicleYearFab = intent.value) }
            is CreateEstimateUiIntent.OnVehicleYearModChange -> _uiState.update { it.copy(vehicleYearMod = intent.value) }
            is CreateEstimateUiIntent.OnVehicleChassisChange -> _uiState.update { it.copy(vehicleChassis = intent.value) }
            is CreateEstimateUiIntent.OnVehicleFuelChange -> _uiState.update { it.copy(vehicleFuel = intent.value) }
            is CreateEstimateUiIntent.OnVehicleAirChange -> _uiState.update { it.copy(vehicleAir = intent.value) }
            is CreateEstimateUiIntent.OnVehicleSteeringChange -> _uiState.update { it.copy(vehicleSteering = intent.value) }
            is CreateEstimateUiIntent.OnVehicleTransmissionChange -> _uiState.update { it.copy(vehicleTransmission = intent.value) }
            is CreateEstimateUiIntent.OnAddVehiclePhotos -> _uiState.update { it.copy(vehiclePhotos = it.vehiclePhotos + intent.uris) }
            is CreateEstimateUiIntent.OnItemGenuineCodeChange -> _uiState.update { it.copy(itemGenuineCode = intent.value) }
            is CreateEstimateUiIntent.OnItemPartNameChange -> onPartNameChange(intent.value)
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

    private fun loadBrands() {
        viewModelScope.launch {
            val brands = fipeRepository.getBrands()
            _uiState.update { it.copy(brands = brands) }
        }
    }

    private fun selectBrand(brand: FipeBrandDto) {
        _uiState.update { 
            it.copy(
                selectedBrand = brand, 
                vehicleBrand = brand.name,
                selectedModel = null, 
                selectedYear = null, 
                models = emptyList(), 
                years = emptyList()
            ) 
        }
        viewModelScope.launch {
            val models = fipeRepository.getModels(brand.code)
            _uiState.update { it.copy(models = models) }
        }
    }

    private fun selectModel(model: FipeModelDto) {
        val brand = _uiState.value.selectedBrand ?: return
        _uiState.update { 
            it.copy(
                selectedModel = model, 
                vehicleModel = model.name,
                selectedYear = null, 
                years = emptyList()
            ) 
        }
        viewModelScope.launch {
            val years = fipeRepository.getYears(brand.code, model.code)
            _uiState.update { it.copy(years = years) }
        }
    }

    private fun selectYear(year: FipeYearDto) {
        val parts = year.name.split("/")
        val fab = parts.firstOrNull() ?: ""
        val mod = parts.lastOrNull() ?: ""
        _uiState.update { 
            it.copy(
                selectedYear = year,
                vehicleYearFab = fab,
                vehicleYearMod = mod
            ) 
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

    private fun onPartNameChange(name: String) {
        _uiState.update { it.copy(itemPartName = name) }
        if (name.length >= 3) {
            suggestionJob?.cancel()
            suggestionJob = viewModelScope.launch {
                try {
                    val suggestion = estimateRepository.getTimeSuggestion(name)
                    if (suggestion.ok) {
                        _uiState.update {
                            it.copy(
                                itemRiHValue = suggestion.ri.toString(),
                                itemPHValue = suggestion.p.toString(),
                                itemTHValue = suggestion.t.toString(),
                                itemRHValue = suggestion.r.toString()
                            )
                        }
                    }
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }

    private fun addItem() {
        val state = _uiState.value
        val mo = state.moHourValue.replace(",", ".").toDoubleOrNull() ?: 80.0
        val pi = state.paintingHourValue.replace(",", ".").toDoubleOrNull() ?: 100.0

        val hoursT = state.itemTHValue.replace(",", ".").toDoubleOrNull() ?: 0.0
        val hoursRi = state.itemRiHValue.replace(",", ".").toDoubleOrNull() ?: 0.0
        val hoursR = state.itemRHValue.replace(",", ".").toDoubleOrNull() ?: 0.0
        val hoursP = state.itemPHValue.replace(",", ".").toDoubleOrNull() ?: 0.0

        val valT = if (state.itemTH) hoursT * mo else 0.0
        val valRi = if (state.itemRiH) hoursRi * mo else 0.0
        val valR = if (state.itemRH) hoursR * mo else 0.0
        val valP = if (state.itemPH) hoursP * pi else 0.0
        val unitPrice = state.itemPartPrice.replace(",", ".").toDoubleOrNull() ?: 0.0

        val total = valT + valRi + valR + valP + unitPrice

        val newItem = EstimateItemDto(
            genuineCode = state.itemGenuineCode,
            partName = state.itemPartName,
            hoursT = hoursT,
            hoursRi = hoursRi,
            hoursR = hoursR,
            hoursP = hoursP,
            selT = if (state.itemTH) 1 else 0,
            selRi = if (state.itemRiH) 1 else 0,
            selR = if (state.itemRH) 1 else 0,
            selP = if (state.itemPH) 1 else 0,
            unitPrice = unitPrice,
            totalValue = total
        )

        _uiState.update { 
            it.copy(
                items = it.items + newItem,
                itemGenuineCode = "",
                itemPartName = "",
                itemTH = false,
                itemTHValue = "",
                itemRiH = false,
                itemRiHValue = "",
                itemRH = false,
                itemRHValue = "",
                itemPH = false,
                itemPHValue = "",
                itemPartPrice = ""
            ) 
        }
    }

    private fun saveEstimate() {
        val state = _uiState.value
        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            try {
                val request = EstimateUpdateRequest(
                    title = "Orçamento - ${state.clientName}",
                    moHourValue = state.moHourValue.replace(",", ".").toDoubleOrNull(),
                    paintingHourValue = state.paintingHourValue.replace(",", ".").toDoubleOrNull(),
                    clientName = state.clientName,
                    clientTel = state.clientTel,
                    clientWhats = state.clientWhats,
                    clientCep = state.clientCep,
                    clientAddress = state.clientAddress,
                    clientNumber = state.clientNumber,
                    clientNeighborhood = state.clientNeighborhood,
                    clientCity = state.clientCity,
                    clientUf = state.clientUf,
                    clientComplement = state.clientComplement,
                    vehiclePlate = state.vehiclePlate,
                    vehicleBrand = state.vehicleBrand,
                    vehicleModel = state.vehicleModel,
                    vehicleYear = "",
                    vehicleFipe = state.selectedYear?.code ?: "",
                    vehicleYearFab = state.vehicleYearFab.toIntOrNull(),
                    vehicleYearMod = state.vehicleYearMod.toIntOrNull(),
                    vehicleChassis = state.vehicleChassis,
                    vehicleFuel = state.vehicleFuel,
                    vehicleAir = state.vehicleAir,
                    vehicleSteering = state.vehicleSteering,
                    vehicleTransmission = state.vehicleTransmission,
                    items = state.items
                )
                val response = createEstimateUseCase(request)
                if (response.ok) {
                    _uiEvent.send(CreateEstimateUiEvent.ShowToast("Orçamento criado com sucesso"))
                    _uiEvent.send(CreateEstimateUiEvent.NavigateBack)
                } else {
                    _uiEvent.send(CreateEstimateUiEvent.ShowToast("Erro ao criar orçamento"))
                }
            } catch (e: Exception) {
                _uiEvent.send(CreateEstimateUiEvent.ShowToast("Erro: ${e.message}"))
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
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
