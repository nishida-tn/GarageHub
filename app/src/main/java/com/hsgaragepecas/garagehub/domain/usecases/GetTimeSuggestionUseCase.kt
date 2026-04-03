package com.hsgaragepecas.garagehub.domain.usecases

import com.hsgaragepecas.garagehub.data.model.TimeSuggestionResponse
import com.hsgaragepecas.garagehub.domain.repository.EstimateRepository
import javax.inject.Inject

/**
 * A use case that gets time suggestions for a part.
 *
 * @param estimateRepository The repository for estimates.
 */
class GetTimeSuggestionUseCase @Inject constructor(
    private val estimateRepository: EstimateRepository
) {
    /**
     * Invokes the use case.
     *
     * @param partName The name of the part.
     * @return The time suggestion response.
     */
    suspend operator fun invoke(partName: String): TimeSuggestionResponse {
        return estimateRepository.getTimeSuggestion(partName)
    }
}
