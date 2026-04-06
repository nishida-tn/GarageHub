package com.hsgaragepecas.garagehub.ui.estimate.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hsgaragepecas.garagehub.data.model.CreateDemandRequest
import com.hsgaragepecas.garagehub.data.model.DemandItemDto
import com.hsgaragepecas.garagehub.data.model.EstimateItemDto
import com.hsgaragepecas.garagehub.data.model.EstimateUpdateRequest
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
    private val generateEstimatePdfUseCase: GenerateEstimatePdfUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditEstimateUiState())
    val uiState: StateFlow<EditEstimateUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<EditEstimateUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

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
            is EditEstimateUiIntent.AddItem -> addItem(intent.item)
            EditEstimateUiIntent.GeneratePdf -> generatePdf()
            EditEstimateUiIntent.SendWhatsApp -> sendWhatsApp()
            EditEstimateUiIntent.MakeOrder -> makeOrder()
            EditEstimateUiIntent.CreateDemand -> createDemand()
        }
    }

    private fun loadEstimate(estimateId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = getEstimateDetailUseCase(estimateId)
                if (response.ok) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            estimate = response.orcamento,
                            items = response.items,
                            photos = response.photos,
                            proposals = response.proposals,
                            error = null
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

    private fun saveEstimate() {
        val estimate = _uiState.value.estimate ?: return
        val items = _uiState.value.items

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                val request = EstimateUpdateRequest(
                    title = estimate.title ?: "",
                    description = estimate.description,
                    moHourValue = estimate.moHourValue,
                    paintingHourValue = estimate.paintingHourValue,
                    clientName = estimate.clientName,
                    clientTel = estimate.clientTel,
                    clientWhats = estimate.clientWhats,
                    clientCep = estimate.clientCep,
                    clientAddress = estimate.clientAddress,
                    clientNumber = estimate.clientNumber,
                    clientNeighborhood = estimate.clientNeighborhood,
                    clientCity = estimate.clientCity,
                    clientUf = estimate.clientUf,
                    clientComplement = estimate.clientComplement,
                    vehiclePlate = estimate.vehiclePlate,
                    vehicleBrand = estimate.vehicleBrand,
                    vehicleModel = estimate.vehicleModel,
                    vehicleYear = estimate.vehicleYear,
                    vehicleFipe = estimate.vehicleFipe,
                    vehicleYearFab = estimate.vehicleYearFab,
                    vehicleYearMod = estimate.vehicleYearMod,
                    vehicleChassis = estimate.vehicleChassis,
                    vehicleFuel = estimate.vehicleFuel,
                    vehicleAir = estimate.vehicleAir,
                    vehicleSteering = estimate.vehicleSteering,
                    vehicleTransmission = estimate.vehicleTransmission,
                    items = items
                )
                val response = updateEstimateUseCase(estimate.id, request)
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
                if (check["has_order"] == true) {
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

    private fun addItem(item: EstimateItemDto) {
        _uiState.update { it.copy(items = it.items + item) }
        saveEstimate()
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
                if (response["ok"] == true) {
                    _uiEvent.send(EditEstimateUiEvent.ShowToast("Pedido gerado com sucesso"))
                    loadEstimate(estimateId)
                } else if (response["detail"] != null) {
                    _uiEvent.send(EditEstimateUiEvent.ShowToast("${response["detail"]}"))
                } else {
                    _uiEvent.send(EditEstimateUiEvent.ShowToast("Nenhuma alteração necessária ou erro ao gerar pedido"))
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
                if (response["ok"] == true) {
                    _uiEvent.send(EditEstimateUiEvent.ShowToast("Demanda criada com sucesso: ${response["num_dem"]}"))
                    loadEstimate(estimate.id)
                } else {
                    _uiEvent.send(EditEstimateUiEvent.ShowToast("Erro ao criar demanda: ${response["error"]}"))
                }
            } catch (e: Exception) {
                _uiEvent.send(EditEstimateUiEvent.ShowToast("Erro ao criar demanda: ${e.message}"))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * Gets time suggestions for a part name.
     *
     * @param partName The name of the part.
     */
    fun onPartNameChanged(partName: String) {
        if (partName.length < 3) return
        viewModelScope.launch {
            try {
                val response = getTimeSuggestionUseCase(partName)
                if (response.ok) {
                    // Update current item input with suggestions if needed
                    // This would require more state in UI for the "current item being added"
                }
            } catch (e: Exception) {
                // Silent error for suggestions
            }
        }
    }
}
