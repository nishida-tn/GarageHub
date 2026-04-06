package com.hsgaragepecas.garagehub.data.repository

import com.hsgaragepecas.garagehub.data.model.CreateDemandRequest
import com.hsgaragepecas.garagehub.data.model.EstimateDetailResponse
import com.hsgaragepecas.garagehub.data.model.EstimateListResponse
import com.hsgaragepecas.garagehub.data.model.EstimateUpdateRequest
import com.hsgaragepecas.garagehub.data.model.TimeSuggestionResponse
import com.hsgaragepecas.garagehub.data.remote.EstimateService
import com.hsgaragepecas.garagehub.domain.repository.EstimateRepository
import javax.inject.Inject

/**
 * The implementation of the [EstimateRepository] interface.
 *
 * @param estimateService The service for the estimate API.
 */
class EstimateRepositoryImpl @Inject constructor(
    private val estimateService: EstimateService
) : EstimateRepository {

    override suspend fun getEstimates(
        status: String?,
        query: String?,
        page: Int,
        pageSize: Int
    ): EstimateListResponse {
        return estimateService.getEstimates(status, query, page, pageSize)
    }

    override suspend fun getEstimateDetail(estimateId: Int): EstimateDetailResponse {
        return estimateService.getEstimateDetail(estimateId)
    }

    override suspend fun createEstimate(request: EstimateUpdateRequest): Map<String, Any> {
        return estimateService.createEstimate(request)
    }

    override suspend fun updateEstimate(
        estimateId: Int,
        request: EstimateUpdateRequest
    ): EstimateDetailResponse {
        return estimateService.updateEstimate(estimateId, request)
    }

    override suspend fun deleteEstimate(estimateId: Int): Boolean {
        return try {
            val response = estimateService.deleteEstimate(estimateId)
            response["ok"] == true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getTimeSuggestion(partName: String): TimeSuggestionResponse {
        return estimateService.getTimeSuggestion(partName)
    }

    override suspend fun generateOrders(estimateId: Int): Map<String, Any> {
        return estimateService.generateOrders(estimateId)
    }

    override suspend fun checkItemDeletion(estimateId: Int, itemId: Int): Map<String, Any> {
        return estimateService.checkItemDeletion(estimateId, itemId)
    }

    override suspend fun createDemand(request: CreateDemandRequest): Map<String, Any> {
        return estimateService.createDemand(request)
    }
}
