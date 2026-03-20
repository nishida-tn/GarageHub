package com.hsgaragepecas.garagehub.domain.usecases

import com.hsgaragepecas.garagehub.data.model.EstimateDetailResponse
import com.hsgaragepecas.garagehub.domain.repository.EstimateRepository
import javax.inject.Inject

/**
 * A use case that gets the details of an estimate.
 *
 * @param estimateRepository The repository for estimates.
 */
class GetEstimateDetailUseCase @Inject constructor(
    private val estimateRepository: EstimateRepository
) {
    /**
     * Invokes the use case.
     *
     * @param estimateId The ID of the estimate.
     * @return The estimate detail response.
     */
    suspend operator fun invoke(estimateId: Int): EstimateDetailResponse {
        return estimateRepository.getEstimateDetail(estimateId)
    }
}
