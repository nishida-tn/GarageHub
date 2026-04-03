package com.hsgaragepecas.garagehub.domain.usecases

import com.hsgaragepecas.garagehub.domain.repository.EstimateRepository
import javax.inject.Inject

/**
 * A use case that checks if an item can be deleted from an estimate.
 *
 * @param estimateRepository The repository for estimates.
 */
class CheckItemDeletionUseCase @Inject constructor(
    private val estimateRepository: EstimateRepository
) {
    /**
     * Invokes the use case.
     *
     * @param estimateId The ID of the estimate.
     * @param itemId The ID of the item.
     * @return A map with the result.
     */
    suspend operator fun invoke(estimateId: Int, itemId: Int): Map<String, Any> {
        return estimateRepository.checkItemDeletion(estimateId, itemId)
    }
}
