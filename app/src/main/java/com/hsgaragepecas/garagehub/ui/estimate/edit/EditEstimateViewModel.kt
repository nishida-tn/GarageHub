package com.hsgaragepecas.garagehub.ui.estimate.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hsgaragepecas.garagehub.data.model.CreateDemandRequest
import com.hsgaragepecas.garagehub.data.model.DemandItemDto
import com.hsgaragepecas.garagehub.data.model.EstimateItemDto
import com.hsgaragepecas.garagehub.data.model.EstimateUpdateRequest
import com.hsgaragepecas.garagehub.data.model.FipeBrandDto
import com.hsgaragepecas.garagehub.data.model.FipeModelDto
import com.hsgaragepecas.garagehub.data.model.FipeYearDto
import com.hsgaragepecas.garagehub.domain.repository.FipeRepository
import com.hsgaragepecas.garagehub.domain.usecases.CheckItemDeletionUseCase
import com.hsgaragepecas.garagehub.domain.usecases.CreateDemandUseCase
import com.hsgaragepecas.garagehub.domain.usecases.DeleteEstimateUseCase
import com.hsgaragepecas.garagehub.domain.usecases.GenerateEstimatePdfUseCase
import com.hsgaragepecas.garagehub.domain.usecases.GenerateOrdersUseCase
import com.hsgaragepecas.garagehub.domain.usecases.GetEstimateDetailUseCase
import com.hsgaragepecas.garagehub.domain.usecases.GetTimeSuggestionUseCase
import com.hsgaragepecas.garagehub.domain.usecases.UpdateEstimateUseCase
import com.hsgaragepecas.garagehub.ui.estimate.edit.EditEstimateContract.EditEstimateUiEvent
import com.hsgaragepecas.garagehub.ui.estimate.edit.EditEstimateContract.EditEstimateUiIntent
import com.hsgaragepecas.garagehub.ui.estimate.edit.EditEstimateContract.EditEstimateUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonPrimitive
import java.util.Locale
import javax.inject.Inject

/**
 * The view model for the edit estimate screen.
 *
 * @param getEstimateDetailUseCase The use case for getting the estimate details.
 * @param updateEstimateUseCase The use case for updating the estimate.
 * @param deleteEstimateUseCase The use case for deleting the estimate.
 * @param getTimeSuggestionUseCase The use case for getting time suggestions.
 * @param generateOrdersUseCase The use case for generating orders.
 * @param checkItemDeletionUseCase The use case for checking item deletion.
 * @param createDemandUseCase The use case for creating a demand.
 * @param generateEstimatePdfUseCase The use case for generating an estimate PDF.
 */
@HiltViewModel
class EditEstimateViewModel @Inject constructor(
    private val getEstimateDetailUseCase: GetEstimateDetailUseCase,
    private val updateEstimateUseCase: UpdateEstimateUseCase,
    private val deleteEstimateUseCase: DeleteEstimateUseCase,
    private val getTimeSuggestionUseCase: GetTimeSuggestionUseCase,
    private val generateOrdersUseCase: GenerateOrdersUseCase,
    private val checkItemDeletionUseCase: CheckItemDeletionUseCase,
    private val createDemandUseCase: CreateDemandUseCase,
    private val generateEstimatePdfUseCase: GenerateEstimatePdfUseCase,
    private val fipeRepository: FipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditEstimateUiState())
    val uiState: StateFlow<EditEstimateUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<EditEstimateUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadBrands()
    }

    private fun loadBrands() {
        viewModelScope.launch {
            val brands = fipeRepository.getBrands()
            _uiState.update { it.copy(brands = brands) }
        }
    }

    /**
     * Handles the intents for the edit estimate screen.
     *
     * @param intent The intent to handle.
     */
    fun onIntent(intent: EditEstimateUiIntent) {
        when (intent) {
            is EditEstimateUiIntent.LoadEstimate -> loadEstimate(intent.estimateId)
            EditEstimateUiIntent.SaveEstimate -> saveEstimate()
            is EditEstimateUiIntent.DeleteItem -> deleteItem(intent.itemId)
            EditEstimateUiIntent.AddItem -> addItem()
            EditEstimateUiIntent.GeneratePdf -> generatePdf()
            EditEstimateUiIntent.SendWhatsApp -> sendWhatsApp()
            EditEstimateUiIntent.MakeOrder -> makeOrder()
            EditEstimateUiIntent.CreateDemand -> createDemand()
            
            // Field updates
            is EditEstimateUiIntent.OnMoHourValueChange -> {
                _uiState.update { it.copy(moHourValue = intent.value) }
                updateCurrentItemTotal()
            }
            is EditEstimateUiIntent.OnPaintingHourValueChange -> {
                _uiState.update { it.copy(paintingHourValue = intent.value) }
                updateCurrentItemTotal()
            }
            is EditEstimateUiIntent.OnClientNameChange -> _uiState.update { it.copy(clientName = intent.value) }
            is EditEstimateUiIntent.OnClientTelChange -> _uiState.update { it.copy(clientTel = intent.value) }
            is EditEstimateUiIntent.OnClientWhatsChange -> _uiState.update { it.copy(clientWhats = intent.value) }
            is EditEstimateUiIntent.OnClientCepChange -> _uiState.update { it.copy(clientCep = intent.value) }
            is EditEstimateUiIntent.OnClientAddressChange -> _uiState.update { it.copy(clientAddress = intent.value) }
            is EditEstimateUiIntent.OnClientNumberChange -> _uiState.update { it.copy(clientNumber = intent.value) }
            is EditEstimateUiIntent.OnClientNeighborhoodChange -> _uiState.update { it.copy(clientNeighborhood = intent.value) }
            is EditEstimateUiIntent.OnClientCityChange -> _uiState.update { it.copy(clientCity = intent.value) }
            is EditEstimateUiIntent.OnClientUfChange -> _uiState.update { it.copy(clientUf = intent.value) }
            is EditEstimateUiIntent.OnClientComplementChange -> _uiState.update { it.copy(clientComplement = intent.value) }
            is EditEstimateUiIntent.OnVehiclePlateChange -> _uiState.update { it.copy(vehiclePlate = intent.value) }
            is EditEstimateUiIntent.OnVehicleBrandChange -> _uiState.update { it.copy(vehicleBrand = intent.value) }
            is EditEstimateUiIntent.OnBrandSelected -> selectBrand(intent.brand)
            is EditEstimateUiIntent.OnVehicleModelChange -> _uiState.update { it.copy(vehicleModel = intent.value) }
            is EditEstimateUiIntent.OnModelSelected -> selectModel(intent.model)
            is EditEstimateUiIntent.OnYearSelected -> selectYear(intent.year)
            is EditEstimateUiIntent.OnVehicleYearFabChange -> _uiState.update { it.copy(vehicleYearFab = intent.value) }
            is EditEstimateUiIntent.OnVehicleYearModChange -> _uiState.update { it.copy(vehicleYearMod = intent.value) }
            is EditEstimateUiIntent.OnVehicleChassisChange -> _uiState.update { it.copy(vehicleChassis = intent.value) }
            is EditEstimateUiIntent.OnVehicleFuelChange -> _uiState.update { it.copy(vehicleFuel = intent.value) }
            is EditEstimateUiIntent.OnVehicleAirChange -> _uiState.update { it.copy(vehicleAir = intent.value) }
            is EditEstimateUiIntent.OnVehicleSteeringChange -> _uiState.update { it.copy(vehicleSteering = intent.value) }
            is EditEstimateUiIntent.OnVehicleTransmissionChange -> _uiState.update { it.copy(vehicleTransmission = intent.value) }
            is EditEstimateUiIntent.OnAddVehiclePhotos -> _uiState.update { it.copy(vehiclePhotos = it.vehiclePhotos + intent.uris) }
            
            // Item updates
            is EditEstimateUiIntent.OnItemGenuineCodeChange -> _uiState.update { it.copy(itemGenuineCode = intent.value) }
            is EditEstimateUiIntent.OnItemPartNameChange -> onPartNameChange(intent.value)
            is EditEstimateUiIntent.OnItemTHChange -> {
                _uiState.update { it.copy(itemTH = intent.value) }
                updateCurrentItemTotal()
            }
            is EditEstimateUiIntent.OnItemTHValueChange -> {
                _uiState.update { it.copy(itemTHValue = intent.value) }
                updateCurrentItemTotal()
            }
            is EditEstimateUiIntent.OnItemRiHChange -> {
                _uiState.update { it.copy(itemRiH = intent.value) }
                updateCurrentItemTotal()
            }
            is EditEstimateUiIntent.OnItemRiHValueChange -> {
                _uiState.update { it.copy(itemRiHValue = intent.value) }
                updateCurrentItemTotal()
            }
            is EditEstimateUiIntent.OnItemRHChange -> {
                _uiState.update { it.copy(itemRH = intent.value) }
                updateCurrentItemTotal()
            }
            is EditEstimateUiIntent.OnItemRHValueChange -> {
                _uiState.update { it.copy(itemRHValue = intent.value) }
                updateCurrentItemTotal()
            }
            is EditEstimateUiIntent.OnItemPHChange -> {
                _uiState.update { it.copy(itemPH = intent.value) }
                updateCurrentItemTotal()
            }
            is EditEstimateUiIntent.OnItemPHValueChange -> {
                _uiState.update { it.copy(itemPHValue = intent.value) }
                updateCurrentItemTotal()
            }
            is EditEstimateUiIntent.OnItemPartPriceChange -> {
                _uiState.update { it.copy(itemPartPrice = intent.value) }
                updateCurrentItemTotal()
            }
        }
    }

    private fun loadEstimate(estimateId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = getEstimateDetailUseCase(estimateId)
                if (response.ok) {
                    val est = response.orcamento
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            estimate = est,
                            items = response.items,
                            photos = response.photos,
                            proposals = response.proposals,
                            error = null,
                            // Map to form
                            moHourValue = String.format(Locale("pt", "BR"), "%.2f", est?.moHourValue ?: 80.0),
                            paintingHourValue = String.format(Locale("pt", "BR"), "%.2f", est?.paintingHourValue ?: 100.0),
                            clientName = est?.clientName ?: "",
                            clientTel = est?.clientTel ?: "",
                            clientWhats = est?.clientWhats ?: "",
                            clientCep = est?.clientCep ?: "",
                            clientAddress = est?.clientAddress ?: "",
                            clientNumber = est?.clientNumber ?: "",
                            clientNeighborhood = est?.clientNeighborhood ?: "",
                            clientCity = est?.clientCity ?: "",
                            clientUf = est?.clientUf ?: "",
                            clientComplement = est?.clientComplement ?: "",
                            vehiclePlate = est?.vehiclePlate ?: "",
                            vehicleBrand = est?.vehicleBrand ?: "",
                            vehicleModel = est?.vehicleModel ?: "",
                            vehicleYearFab = est?.vehicleYearFab?.toString() ?: "",
                            vehicleYearMod = est?.vehicleYearMod?.toString() ?: "",
                            vehicleChassis = est?.vehicleChassis ?: "",
                            vehicleFuel = est?.vehicleFuel ?: "",
                            vehicleAir = est?.vehicleAir ?: "",
                            vehicleSteering = est?.vehicleSteering ?: "",
                            vehicleTransmission = est?.vehicleTransmission ?: ""
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Erro ao carregar orçamento") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
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

    private fun onPartNameChange(name: String) {
        _uiState.update { it.copy(itemPartName = name) }
        if (name.length >= 3) {
            viewModelScope.launch {
                try {
                    val suggestion = getTimeSuggestionUseCase(name)
                    if (suggestion.ok) {
                        _uiState.update {
                            it.copy(
                                itemRiHValue = suggestion.ri.toString(),
                                itemPHValue = suggestion.p.toString(),
                                itemTHValue = suggestion.t.toString(),
                                itemRHValue = suggestion.r.toString()
                            )
                        }
                        updateCurrentItemTotal()
                    }
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }

    private fun updateCurrentItemTotal() {
        val state = _uiState.value
        val mo = state.moHourValue.replace(",", ".").toDoubleOrNull() ?: 0.0
        val pi = state.paintingHourValue.replace(",", ".").toDoubleOrNull() ?: 0.0

        val hoursT = if (state.itemTH) state.itemTHValue.replace(",", ".").toDoubleOrNull() ?: 0.0 else 0.0
        val hoursRi = if (state.itemRiH) state.itemRiHValue.replace(",", ".").toDoubleOrNull() ?: 0.0 else 0.0
        val hoursR = if (state.itemRH) state.itemRHValue.replace(",", ".").toDoubleOrNull() ?: 0.0 else 0.0
        val hoursP = if (state.itemPH) state.itemPHValue.replace(",", ".").toDoubleOrNull() ?: 0.0 else 0.0

        val partPrice = state.itemPartPrice.replace(",", ".").toDoubleOrNull() ?: 0.0
        
        val total = (hoursT * mo) + (hoursRi * mo) + (hoursR * mo) + (hoursP * pi) + partPrice

        _uiState.update { 
            it.copy(itemTotal = String.format(Locale("pt", "BR"), "%.2f", total))
        }
    }

    private fun addItem() {
        val state = _uiState.value
        val mo = state.moHourValue.replace(",", ".").toDoubleOrNull() ?: 80.0
        val pi = state.paintingHourValue.replace(",", ".").toDoubleOrNull() ?: 100.0

        val hoursT = if (state.itemTH) state.itemTHValue.replace(",", ".").toDoubleOrNull() ?: 0.0 else 0.0
        val hoursRi = if (state.itemRiH) state.itemRiHValue.replace(",", ".").toDoubleOrNull() ?: 0.0 else 0.0
        val hoursR = if (state.itemRH) state.itemRHValue.replace(",", ".").toDoubleOrNull() ?: 0.0 else 0.0
        val hoursP = if (state.itemPH) state.itemPHValue.replace(",", ".").toDoubleOrNull() ?: 0.0 else 0.0

        val valT = hoursT * mo
        val valRi = hoursRi * mo
        val valR = hoursR * mo
        val valP = hoursP * pi
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
            valueT = valT,
            valueRi = valRi,
            valueR = valR,
            valueP = valP,
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
                itemPartPrice = "0,00",
                itemTotal = "0,00"
            ) 
        }
        saveEstimate()
    }

    private fun saveEstimate() {
        val state = _uiState.value
        val estimateId = state.estimate?.id ?: return
        val items = state.items

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                val request = EstimateUpdateRequest(
                    title = state.estimate.title ?: "Orçamento - ${state.clientName}",
                    description = state.estimate.description,
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
                    vehicleYear = state.estimate.vehicleYear,
                    vehicleFipe = state.selectedYear?.code ?: state.estimate.vehicleFipe ?: "",
                    vehicleYearFab = state.vehicleYearFab.toIntOrNull(),
                    vehicleYearMod = state.vehicleYearMod.toIntOrNull(),
                    vehicleChassis = state.vehicleChassis,
                    vehicleFuel = state.vehicleFuel,
                    vehicleAir = state.vehicleAir,
                    vehicleSteering = state.vehicleSteering,
                    vehicleTransmission = state.vehicleTransmission,
                    items = items
                )
                val response = updateEstimateUseCase(estimateId, request)
                if (response.ok) {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            items = response.items,
                            estimate = response.orcamento
                        )
                    }
                    _uiEvent.send(EditEstimateUiEvent.ShowToast("Orçamento salvo com sucesso"))
                } else {
                    _uiState.update { it.copy(isSaving = false) }
                    _uiEvent.send(EditEstimateUiEvent.ShowToast("Erro ao salvar orçamento"))
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false) }
                _uiEvent.send(EditEstimateUiEvent.ShowToast("Erro: ${e.message}"))
            }
        }
    }

    private fun deleteItem(itemId: Int) {
        val estimateId = _uiState.value.estimate?.id ?: return
        viewModelScope.launch {
            try {
                val check = checkItemDeletionUseCase(estimateId, itemId)
                val hasOrder = check["has_order"]?.let { 
                    if (it is JsonPrimitive) it.booleanOrNull ?: false else false 
                } ?: false
                if (hasOrder) {
                    _uiEvent.send(EditEstimateUiEvent.ShowToast("Não é possível excluir item vinculado a pedido ativo"))
                } else {
                    _uiState.update { state ->
                        state.copy(items = state.items.filter { it.id != itemId })
                    }
                    saveEstimate()
                }
            } catch (e: Exception) {
                _uiEvent.send(EditEstimateUiEvent.ShowToast("Erro ao verificar item: ${e.message}"))
            }
        }
    }

    private fun generatePdf() {
        val estimate = _uiState.value.estimate ?: return
        val items = _uiState.value.items

        viewModelScope.launch {
            _uiEvent.send(EditEstimateUiEvent.ShowToast("Gerando PDF..."))
            try {
                val uri = generateEstimatePdfUseCase(estimate, items)
                _uiEvent.send(EditEstimateUiEvent.OpenUri(uri))
            } catch (e: Exception) {
                _uiEvent.send(EditEstimateUiEvent.ShowToast("Erro ao gerar PDF: ${e.message}"))
            }
        }
    }

    private fun sendWhatsApp() {
        viewModelScope.launch {
            _uiEvent.send(EditEstimateUiEvent.ShowToast("Abrindo WhatsApp..."))
        }
    }

    private fun makeOrder() {
        val estimateId = _uiState.value.estimate?.id ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = generateOrdersUseCase(estimateId)
                val ok = response["ok"]?.let { 
                    if (it is JsonPrimitive) it.booleanOrNull ?: false else false 
                } ?: false
                if (ok) {
                    _uiEvent.send(EditEstimateUiEvent.ShowToast("Pedido gerado com sucesso"))
                    loadEstimate(estimateId)
                } else {
                    val detail = response["detail"]?.let { 
                        if (it is JsonPrimitive) it.content else null 
                    }
                    if (detail != null) {
                        _uiEvent.send(EditEstimateUiEvent.ShowToast(detail))
                    } else {
                        _uiEvent.send(EditEstimateUiEvent.ShowToast("Nenhuma alteração necessária ou erro ao gerar pedido"))
                    }
                }
            } catch (e: Exception) {
                _uiEvent.send(EditEstimateUiEvent.ShowToast("Erro ao gerar pedido: ${e.message}"))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun createDemand() {
        val estimate = _uiState.value.estimate ?: return
        val items = _uiState.value.items

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val demandRequest = CreateDemandRequest(
                    clientName = estimate.clientName ?: "PORTAL OFICINA",
                    clientTel = estimate.clientTel ?: "",
                    vehicleBrand = estimate.vehicleBrand ?: "",
                    vehicleModel = estimate.vehicleModel ?: "",
                    yearFab = estimate.vehicleYearFab ?: 0,
                    yearMod = estimate.vehicleYearMod ?: 0,
                    chassis = estimate.vehicleChassis,
                    fuelType = estimate.vehicleFuel,
                    airConditioning = estimate.vehicleAir,
                    steeringType = estimate.vehicleSteering,
                    transmissionType = estimate.vehicleTransmission,
                    photoUrls = _uiState.value.photos,
                    items = items.mapIndexed { index, item ->
                        DemandItemDto(
                            description = item.partName ?: "",
                            quantity = item.quantity,
                            dealershipCode = item.genuineCode,
                            estimateItemId = item.id,
                            estimateItemIdx = index + 1
                        )
                    },
                    estimateId = estimate.id
                )
                val response = createDemandUseCase(demandRequest)
                val ok = response["ok"]?.let { 
                    if (it is JsonPrimitive) it.booleanOrNull ?: false else false 
                } ?: false
                if (ok) {
                    val numDem = response["num_dem"]?.let { 
                        if (it is JsonPrimitive) it.content else null 
                    }
                    _uiEvent.send(EditEstimateUiEvent.ShowToast("Demanda criada com sucesso: $numDem"))
                    loadEstimate(estimate.id)
                } else {
                    val error = response["error"]?.let { 
                        if (it is JsonPrimitive) it.content else null 
                    }
                    _uiEvent.send(EditEstimateUiEvent.ShowToast("Erro ao criar demanda: $error"))
                }
            } catch (e: Exception) {
                _uiEvent.send(EditEstimateUiEvent.ShowToast("Erro ao criar demanda: ${e.message}"))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
