package com.hsgaragepecas.garagehub.domain.usecases

import android.net.Uri
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
     * @param photoUris The URIs of the vehicle photos.
     * @return The response from the API.
     */
    suspend operator fun invoke(
        request: EstimateUpdateRequest,
        photoUris: List<Uri> = emptyList()
    ): CreateEstimateResponse {
        return estimateRepository.createEstimate(request, photoUris)
    }
}
