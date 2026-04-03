package com.hsgaragepecas.garagehub.domain.usecases

import com.hsgaragepecas.garagehub.domain.repository.EstimateRepository
import javax.inject.Inject

/**
 * A use case that generates orders from an estimate.
 *
 * @param estimateRepository The repository for estimates.
 */
class GenerateOrdersUseCase @Inject constructor(
    private val estimateRepository: EstimateRepository
) {
    /**
     * Invokes the use case.
     *
     * @param estimateId The ID of the estimate.
     * @return A map with the result.
     */
    suspend operator fun invoke(estimateId: Int): Map<String, Any> {
        return estimateRepository.generateOrders(estimateId)
    }
}
