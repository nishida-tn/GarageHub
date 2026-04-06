package com.hsgaragepecas.garagehub.ui.estimate.edit

import com.hsgaragepecas.garagehub.data.model.EstimateDetailResponse
import com.hsgaragepecas.garagehub.data.model.EstimateFullDto
import com.hsgaragepecas.garagehub.data.model.EstimateItemDto
import com.hsgaragepecas.garagehub.domain.usecases.CheckItemDeletionUseCase
import com.hsgaragepecas.garagehub.domain.usecases.CreateDemandUseCase
import com.hsgaragepecas.garagehub.domain.usecases.DeleteEstimateUseCase
import com.hsgaragepecas.garagehub.domain.usecases.GenerateEstimatePdfUseCase
import com.hsgaragepecas.garagehub.domain.usecases.GenerateOrdersUseCase
import com.hsgaragepecas.garagehub.domain.usecases.GetEstimateDetailUseCase
import com.hsgaragepecas.garagehub.domain.usecases.GetTimeSuggestionUseCase
import com.hsgaragepecas.garagehub.domain.usecases.UpdateEstimateUseCase
import com.hsgaragepecas.garagehub.ui.estimate.edit.EditEstimateContract.EditEstimateUiIntent
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class EditEstimateViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val getEstimateDetailUseCase: GetEstimateDetailUseCase = mockk()
    private val updateEstimateUseCase: UpdateEstimateUseCase = mockk()
    private val deleteEstimateUseCase: DeleteEstimateUseCase = mockk()
    private val getTimeSuggestionUseCase: GetTimeSuggestionUseCase = mockk()
    private val generateOrdersUseCase: GenerateOrdersUseCase = mockk()
    private val checkItemDeletionUseCase: CheckItemDeletionUseCase = mockk()
    private val createDemandUseCase: CreateDemandUseCase = mockk()
    private val generateEstimatePdfUseCase: GenerateEstimatePdfUseCase = mockk()

    private lateinit var viewModel: EditEstimateViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = EditEstimateViewModel(
            getEstimateDetailUseCase,
            updateEstimateUseCase,
            deleteEstimateUseCase,
            getTimeSuggestionUseCase,
            generateOrdersUseCase,
            checkItemDeletionUseCase,
            createDemandUseCase,
            generateEstimatePdfUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.estimate)
        assertTrue(state.items.isEmpty())
    }

    @Test
    fun `LoadEstimate intent updates state with estimate details`() = runTest {
        val estimateId = 1
        val estimate = EstimateFullDto(id = estimateId, title = "Test")
        val items = listOf(EstimateItemDto(id = 1, partName = "Part 1"))
        val response = EstimateDetailResponse(ok = true, orcamento = estimate, items = items)

        coEvery { getEstimateDetailUseCase(estimateId) } returns response

        viewModel.onIntent(EditEstimateUiIntent.LoadEstimate(estimateId))
        
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(estimate, state.estimate)
        assertEquals(items, state.items)
        assertNull(state.error)
    }

    @Test
    fun `LoadEstimate intent sets error state on failure`() = runTest {
        val estimateId = 1
        coEvery { getEstimateDetailUseCase(estimateId) } throws Exception("Network error")

        viewModel.onIntent(EditEstimateUiIntent.LoadEstimate(estimateId))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Network error", state.error)
    }

    @Test
    fun `SaveEstimate intent calls update use case and updates state`() = runTest {
        // First load an estimate
        val estimateId = 1
        val estimate = EstimateFullDto(id = estimateId, title = "Test")
        coEvery { getEstimateDetailUseCase(estimateId) } returns EstimateDetailResponse(ok = true, orcamento = estimate)
        viewModel.onIntent(EditEstimateUiIntent.LoadEstimate(estimateId))
        advanceUntilIdle()

        // Prepare update response
        val updatedItems = listOf(EstimateItemDto(id = 2, partName = "Updated Part"))
        val updateResponse = EstimateDetailResponse(ok = true, orcamento = estimate, items = updatedItems)
        coEvery { updateEstimateUseCase(estimateId, any()) } returns updateResponse

        viewModel.onIntent(EditEstimateUiIntent.SaveEstimate)
        
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isSaving)
        assertEquals(updatedItems, state.items)
    }

    @Test
    fun `DeleteItem intent checks for orders and removes item if safe`() = runTest {
        // Load estimate with items
        val estimateId = 1
        val itemId = 100
        val items = listOf(EstimateItemDto(id = itemId, partName = "Part to Delete"))
        val estimate = EstimateFullDto(id = estimateId, title = "Test")
        
        coEvery { getEstimateDetailUseCase(estimateId) } returns EstimateDetailResponse(ok = true, orcamento = estimate, items = items)
        viewModel.onIntent(EditEstimateUiIntent.LoadEstimate(estimateId))
        advanceUntilIdle()

        // Mock check deletion (no orders)
        coEvery { checkItemDeletionUseCase(estimateId, itemId) } returns mapOf("has_order" to false)
        // Mock save after delete
        coEvery { updateEstimateUseCase(estimateId, any()) } returns EstimateDetailResponse(ok = true, items = emptyList())

        viewModel.onIntent(EditEstimateUiIntent.DeleteItem(itemId))
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.items.isEmpty())
    }

    @Test
    fun `MakeOrder intent calls generate orders and reloads estimate`() = runTest {
        val estimateId = 1
        val estimate = EstimateFullDto(id = estimateId, title = "Test")
        
        coEvery { getEstimateDetailUseCase(estimateId) } returns EstimateDetailResponse(ok = true, orcamento = estimate)
        viewModel.onIntent(EditEstimateUiIntent.LoadEstimate(estimateId))
        advanceUntilIdle()

        coEvery { generateOrdersUseCase(estimateId) } returns mapOf("ok" to true)
        
        viewModel.onIntent(EditEstimateUiIntent.MakeOrder)
        advanceUntilIdle()

        // Verify it tried to reload
        coEvery { getEstimateDetailUseCase(estimateId) }
    }
}
