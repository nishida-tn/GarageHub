package com.hsgaragepecas.garagehub.ui.listorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hsgaragepecas.garagehub.domain.usecases.ListOrdersUseCase
import com.hsgaragepecas.garagehub.ui.listorder.ListOrderContract.ListOrderUiEvent
import com.hsgaragepecas.garagehub.ui.listorder.ListOrderContract.ListOrderUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The view model for the list order screen.
 *
 * @param listOrdersUseCase The use case for listing the orders.
 */
@HiltViewModel
class ListOrderViewModel @Inject constructor(
    private val listOrdersUseCase: ListOrdersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListOrderUiState())
    val uiState: StateFlow<ListOrderUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ListOrderUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    /**
     * Loads the orders.
     */
    fun loadOrders() {
        viewModelScope.launch {
            delay(1)
            listOrdersUseCase()
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { throwable ->
                    _uiState.update { it.copy(isLoading = false, error = throwable.message) }
                    _uiEvent.send(ListOrderUiEvent.ShowToast("Error loading orders: ${throwable.message}"))
                }
                .collect { orders ->
                    _uiState.update { it.copy(isLoading = false, orders = orders, error = null) }
                }
        }
    }
}
