package com.hsgaragepecas.garagehub.domain.usecases

import com.hsgaragepecas.garagehub.data.model.EstimateListResponse
import com.hsgaragepecas.garagehub.domain.repository.EstimateRepository
import javax.inject.Inject

/**
 * A use case that lists estimates.
 *
 * @param estimateRepository The repository for estimates.
 */
class ListEstimatesUseCase @Inject constructor(
    private val estimateRepository: EstimateRepository
) {
    /**
     * Invokes the use case.
     *
     * @param status The status filter.
     * @param query The search query.
     * @param page The page number.
     * @param pageSize The number of items per page.
     * @return The estimate list response.
     */
    suspend operator fun invoke(
        status: String? = null,
        query: String? = null,
        page: Int = 1,
        pageSize: Int = 20
    ): EstimateListResponse {
        return estimateRepository.getEstimates(status, query, page, pageSize)
    }
}
