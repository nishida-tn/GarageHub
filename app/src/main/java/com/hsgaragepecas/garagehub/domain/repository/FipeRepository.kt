package com.hsgaragepecas.garagehub.domain.repository

import com.hsgaragepecas.garagehub.data.model.FipeBrandDto
import com.hsgaragepecas.garagehub.data.model.FipeModelDto
import com.hsgaragepecas.garagehub.data.model.FipeYearDto

/**
 * Repository interface for FIPE data.
 */
interface FipeRepository {
    /**
     * Fetches vehicle brands.
     */
    suspend fun getBrands(): List<FipeBrandDto>

    /**
     * Fetches vehicle models for a brand.
     */
    suspend fun getModels(brand: String): List<FipeModelDto>

    /**
     * Fetches vehicle years for a model.
     */
    suspend fun getYears(brand: String, model: String): List<FipeYearDto>
}
