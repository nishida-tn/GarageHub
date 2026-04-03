package com.hsgaragepecas.garagehub.domain.usecases

import com.hsgaragepecas.garagehub.data.model.EstimateDetailResponse
import com.hsgaragepecas.garagehub.data.model.EstimateUpdateRequest
import com.hsgaragepecas.garagehub.domain.repository.EstimateRepository
import javax.inject.Inject

/**
 * A use case that updates an estimate.
 *
 * @param estimateRepository The repository for estimates.
 */
class UpdateEstimateUseCase @Inject constructor(
    private val estimateRepository: EstimateRepository
) {
    /**
     * Invokes the use case.
     *
     * @param estimateId The ID of the estimate.
     * @param request The update request.
     * @return The estimate detail response.
     */
    suspend operator fun invoke(estimateId: Int, request: EstimateUpdateRequest): EstimateDetailResponse {
        return estimateRepository.updateEstimate(estimateId, request)
    }
}
