package com.hsgaragepecas.garagehub.data.repository

import com.hsgaragepecas.garagehub.data.model.EstimateListResponse
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
}
