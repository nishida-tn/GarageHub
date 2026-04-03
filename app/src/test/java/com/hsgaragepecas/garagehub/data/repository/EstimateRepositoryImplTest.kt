package com.hsgaragepecas.garagehub.data.repository

import com.hsgaragepecas.garagehub.data.model.EstimateDetailResponse
import com.hsgaragepecas.garagehub.data.model.EstimateListResponse
import com.hsgaragepecas.garagehub.data.model.EstimateUpdateRequest
import com.hsgaragepecas.garagehub.data.model.PaginationDto
import com.hsgaragepecas.garagehub.data.model.TimeSuggestionResponse
import com.hsgaragepecas.garagehub.data.remote.EstimateService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class EstimateRepositoryImplTest {

    private val estimateService: EstimateService = mockk()
    private lateinit var repository: EstimateRepositoryImpl

    @Before
    fun setUp() {
        repository = EstimateRepositoryImpl(estimateService)
    }

    @Test
    fun `getEstimates calls service and returns response`() = runTest {
        val expectedResponse = EstimateListResponse(
            ok = true,
            items = emptyList(),
            pagination = PaginationDto(0, 1, 20, 0)
        )
        coEvery { estimateService.getEstimates(any(), any(), any(), any()) } returns expectedResponse

        val result = repository.getEstimates()

        assertEquals(expectedResponse, result)
    }

    @Test
    fun `getEstimateDetail calls service and returns response`() = runTest {
        val estimateId = 1
        val expectedResponse = EstimateDetailResponse(ok = true)
        coEvery { estimateService.getEstimateDetail(estimateId) } returns expectedResponse

        val result = repository.getEstimateDetail(estimateId)

        assertEquals(expectedResponse, result)
    }

    @Test
    fun `updateEstimate calls service and returns response`() = runTest {
        val estimateId = 1
        val request = EstimateUpdateRequest(title = "New Title")
        val expectedResponse = EstimateDetailResponse(ok = true)
        coEvery { estimateService.updateEstimate(estimateId, request) } returns expectedResponse

        val result = repository.updateEstimate(estimateId, request)

        assertEquals(expectedResponse, result)
    }

    @Test
    fun `deleteEstimate returns true on success`() = runTest {
        val estimateId = 1
        coEvery { estimateService.deleteEstimate(estimateId) } returns mapOf("ok" to true)

        val result = repository.deleteEstimate(estimateId)

        assertTrue(result)
    }

    @Test
    fun `getTimeSuggestion calls service and returns response`() = runTest {
        val partName = "Part"
        val expectedResponse = TimeSuggestionResponse(ok = true, t = 1.0)
        coEvery { estimateService.getTimeSuggestion(partName) } returns expectedResponse

        val result = repository.getTimeSuggestion(partName)

        assertEquals(expectedResponse, result)
    }

    @Test
    fun `generateOrders calls service and returns response map`() = runTest {
        val estimateId = 1
        val expectedResponse = mapOf("ok" to true, "pedido_id" to 123)
        coEvery { estimateService.generateOrders(estimateId) } returns expectedResponse

        val result = repository.generateOrders(estimateId)

        assertEquals(expectedResponse, result)
    }
}
