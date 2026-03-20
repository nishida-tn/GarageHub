package com.hsgaragepecas.garagehub.domain.repository

import com.hsgaragepecas.garagehub.data.model.EstimateDetailResponse
import com.hsgaragepecas.garagehub.data.model.EstimateListResponse

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
}
