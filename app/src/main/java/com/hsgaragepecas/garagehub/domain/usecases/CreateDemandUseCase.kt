package com.hsgaragepecas.garagehub.domain.usecases

import com.hsgaragepecas.garagehub.data.model.CreateDemandRequest
import com.hsgaragepecas.garagehub.domain.repository.EstimateRepository
import javax.inject.Inject

/**
 * A use case that creates a demand for an estimate.
 *
 * @param estimateRepository The repository for estimates.
 */
class CreateDemandUseCase @Inject constructor(
    private val estimateRepository: EstimateRepository
) {
    /**
     * Invokes the use case.
     *
     * @param request The demand request.
     * @return A map with the result.
     */
    suspend operator fun invoke(request: CreateDemandRequest): Map<String, Any> {
        return estimateRepository.createDemand(request)
    }
}
