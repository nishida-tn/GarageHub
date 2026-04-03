package com.hsgaragepecas.garagehub.domain.usecases

import com.hsgaragepecas.garagehub.domain.repository.EstimateRepository
import javax.inject.Inject

/**
 * A use case that deletes an estimate.
 *
 * @param estimateRepository The repository for estimates.
 */
class DeleteEstimateUseCase @Inject constructor(
    private val estimateRepository: EstimateRepository
) {
    /**
     * Invokes the use case.
     *
     * @param estimateId The ID of the estimate.
     * @return Whether the deletion was successful.
     */
    suspend operator fun invoke(estimateId: Int): Boolean {
        return estimateRepository.deleteEstimate(estimateId)
    }
}
