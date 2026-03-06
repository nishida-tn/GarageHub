package com.hsgaragepecas.garagehub.ui.listorder

import com.hsgaragepecas.garagehub.data.local.order.OrderEntity
import com.hsgaragepecas.garagehub.data.local.order.OrderStatus
import com.hsgaragepecas.garagehub.domain.usecases.ListOrdersUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ListOrderViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var listOrdersUseCase: ListOrdersUseCase
    private lateinit var viewModel: ListOrderViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        listOrdersUseCase = mockk()
        viewModel = ListOrderViewModel(listOrdersUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadOrders updates uiState with orders when use case returns data`() = runTest {
        val orders = listOf(
            OrderEntity(1, 1678886400000, "Ford", OrderStatus.ABERTO, 2, 150.0),
            OrderEntity(2, 1678799999000, "Chevrolet", OrderStatus.CONCLUIDO, 1, 200.0)
        )
        every { listOrdersUseCase() } returns flowOf(orders)

        viewModel.loadOrders()
        advanceUntilIdle()

        assertEquals(
            ListOrderContract.ListOrderUiState(
                orders = orders,
                isLoading = false,
                error = null
            ),
            viewModel.uiState.value
        )
    }

    @Test
    fun `loadOrders updates uiState with error and emits toast when use case throws exception`() = runTest {
        // Arrange
        val errorMessage = "Test error"
        every { listOrdersUseCase() } returns flow {
            throw RuntimeException(errorMessage)
        }

        val uiEvents = mutableListOf<ListOrderContract.ListOrderUiEvent>()
        val eventJob = launch {
            viewModel.uiEvent.collect { uiEvents.add(it) }
        }

        // Act
        viewModel.loadOrders()
        advanceUntilIdle()

        // Assert
        assertEquals(
            ListOrderContract.ListOrderUiState(
                isLoading = false,
                error = errorMessage
            ),
            viewModel.uiState.value
        )

        assertEquals(1, uiEvents.size)
        assertEquals(
            ListOrderContract.ListOrderUiEvent.ShowToast("Error loading orders: $errorMessage"),
            uiEvents[0]
        )

        eventJob.cancel()
    }
}
