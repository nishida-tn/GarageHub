package com.hsgaragepecas.garagehub.ui.estimate.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hsgaragepecas.garagehub.domain.usecases.GetEstimateDetailUseCase
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
 */
@HiltViewModel
class EditEstimateViewModel @Inject constructor(
    private val getEstimateDetailUseCase: GetEstimateDetailUseCase
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
        // TODO: Implement save logic
    }

    private fun deleteItem(itemId: Int) {
        // TODO: Implement delete item logic
    }

    private fun addItem(item: com.hsgaragepecas.garagehub.data.model.EstimateItemDto) {
        // TODO: Implement add item logic
    }

    private fun generatePdf() {
        // TODO: Implement PDF generation logic
    }

    private fun sendWhatsApp() {
        // TODO: Implement WhatsApp sending logic
    }

    private fun makeOrder() {
        // TODO: Implement order making logic
    }

    private fun createDemand() {
        // TODO: Implement demand creation logic
    }
}
