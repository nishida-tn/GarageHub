package com.hsgaragepecas.garagehub.ui.estimate.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hsgaragepecas.garagehub.domain.usecases.ListEstimatesUseCase
import com.hsgaragepecas.garagehub.ui.estimate.list.ListEstimateContract.ListEstimateUiEvent
import com.hsgaragepecas.garagehub.ui.estimate.list.ListEstimateContract.ListEstimateUiIntent
import com.hsgaragepecas.garagehub.ui.estimate.list.ListEstimateContract.ListEstimateUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The view model for the list estimate screen.
 *
 * @param listEstimatesUseCase The use case for listing the estimates.
 */
@HiltViewModel
class ListEstimateViewModel @Inject constructor(
    private val listEstimatesUseCase: ListEstimatesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListEstimateUiState())
    val uiState: StateFlow<ListEstimateUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ListEstimateUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    private var searchJob: Job? = null

    /**
     * Handles the intents for the list estimate screen.
     *
     * @param intent The intent to handle.
     */
    fun onIntent(intent: ListEstimateUiIntent) {
        when (intent) {
            is ListEstimateUiIntent.LoadEstimates -> loadEstimates()
            is ListEstimateUiIntent.SearchEstimates -> {
                _uiState.update { it.copy(searchQuery = intent.query) }
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500)
                    loadEstimates()
                }
            }
            is ListEstimateUiIntent.FilterByStatus -> {
                _uiState.update { it.copy(selectedStatus = intent.status) }
                loadEstimates()
            }
            is ListEstimateUiIntent.RefreshEstimates -> loadEstimates()
            is ListEstimateUiIntent.DeleteEstimate -> {
                // TODO: Implement delete use case and call it here
                viewModelScope.launch {
                   _uiEvent.send(ListEstimateUiEvent.ShowToast("Excluir funcionalidade em breve"))
                }
            }
        }
    }

    private fun loadEstimates() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = listEstimatesUseCase(
                    query = _uiState.value.searchQuery,
                    status = _uiState.value.selectedStatus
                )
                if (response.ok) {
                    _uiState.update { it.copy(isLoading = false, estimates = response.items, error = null) }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Failed to load estimates") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
                _uiEvent.send(ListEstimateUiEvent.ShowToast("Error: ${e.message}"))
            }
        }
    }
}
