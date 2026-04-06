package com.hsgaragepecas.garagehub.domain.usecases

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
     * @return A map with the result from the API.
     */
    suspend operator fun invoke(request: EstimateUpdateRequest): Map<String, Any> {
        return estimateRepository.createEstimate(request)
    }
}
