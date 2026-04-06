package com.hsgaragepecas.garagehub.domain.usecases

import com.hsgaragepecas.garagehub.data.model.CreateEstimateResponse
import com.hsgaragepecas.garagehub.data.model.EstimateUpdateRequest
import com.hsgaragepecas.garagehub.domain.repository.EstimateRepository
import javax.inject.Inject

/**
 * Use case to create a new estimate.
 *
 * @param estimateRepository The repository for estimates.
 */
class CreateEstimateUseCase @Inject constructor(
    private val estimateRepository: EstimateRepository
) {
    /**
     * Invokes the use case.
     *
     * @param request The estimate create request.
     * @return The response from the API.
     */
    suspend operator fun invoke(request: EstimateUpdateRequest): CreateEstimateResponse {
        return estimateRepository.createEstimate(request)
    }
}
