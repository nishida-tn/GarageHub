package com.hsgaragepecas.garagehub.domain.repository

import android.net.Uri
import com.hsgaragepecas.garagehub.data.model.CreateDemandRequest
import com.hsgaragepecas.garagehub.data.model.CreateEstimateResponse
import com.hsgaragepecas.garagehub.data.model.EstimateDetailResponse
import com.hsgaragepecas.garagehub.data.model.EstimateListResponse
import com.hsgaragepecas.garagehub.data.model.EstimateUpdateRequest
import com.hsgaragepecas.garagehub.data.model.TimeSuggestionResponse

/**
 * The repository for estimates.
 */
interface EstimateRepository {

    /**
     * Gets a list of estimates.
     *
     * @param status The status filter.
     * @param query The search query.
     * @param page The page number.
     * @param pageSize The number of items per page.
     * @return The estimate list response.
     */
    suspend fun getEstimates(
        status: String? = null,
        query: String? = null,
        page: Int = 1,
        pageSize: Int = 20
    ): EstimateListResponse

    /**
     * Gets the details of an estimate.
     *
     * @param estimateId The ID of the estimate.
     * @return The estimate detail response.
     */
    suspend fun getEstimateDetail(estimateId: Int): EstimateDetailResponse

    /**
     * Creates a new estimate.
     *
     * @param request The create request.
     * @param photoUris The URIs of the vehicle photos.
     * @return The create estimate response.
     */
    suspend fun createEstimate(
        request: EstimateUpdateRequest,
        photoUris: List<Uri> = emptyList()
    ): CreateEstimateResponse

    /**
     * Updates an estimate.
     *
     * @param estimateId The ID of the estimate.
     * @param request The update request.
     * @return The estimate detail response.
     */
    suspend fun updateEstimate(estimateId: Int, request: EstimateUpdateRequest): EstimateDetailResponse

    /**
     * Deletes an estimate.
     *
     * @param estimateId The ID of the estimate.
     * @return Whether the deletion was successful.
     */
    suspend fun deleteEstimate(estimateId: Int): Boolean

    /**
     * Gets time suggestions for a part.
     *
     * @param partName The name of the part.
     * @return The time suggestion response.
     */
    suspend fun getTimeSuggestion(partName: String): TimeSuggestionResponse

    /**
     * Generates orders from an estimate.
     *
     * @param estimateId The ID of the estimate.
     * @return A map with the result.
     */
    suspend fun generateOrders(estimateId: Int): Map<String, Any>

    /**
     * Checks if an item can be deleted from an estimate.
     *
     * @param estimateId The ID of the estimate.
     * @param itemId The ID of the item.
     * @return A map with the result.
     */
    suspend fun checkItemDeletion(estimateId: Int, itemId: Int): Map<String, Any>

    /**
     * Creates a demand for an estimate.
     *
     * @param request The demand request.
     * @return A map with the result.
     */
    suspend fun createDemand(request: CreateDemandRequest): Map<String, Any>
}
