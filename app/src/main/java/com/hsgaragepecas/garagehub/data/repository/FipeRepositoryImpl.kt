package com.hsgaragepecas.garagehub.data.repository

import com.hsgaragepecas.garagehub.data.model.FipeBrandDto
import com.hsgaragepecas.garagehub.data.model.FipeModelDto
import com.hsgaragepecas.garagehub.data.model.FipeYearDto
import com.hsgaragepecas.garagehub.data.remote.FipeService
import com.hsgaragepecas.garagehub.domain.repository.FipeRepository
import javax.inject.Inject

/**
 * Implementation of [FipeRepository].
 */
class FipeRepositoryImpl @Inject constructor(
    private val fipeService: FipeService
) : FipeRepository {

    override suspend fun getBrands(): List<FipeBrandDto> {
        return try {
            fipeService.getBrands()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getModels(brand: String): List<FipeModelDto> {
        return try {
            fipeService.getModels(brand).models
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getYears(brand: String, model: String): List<FipeYearDto> {
        return try {
            fipeService.getYears(brand, model)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
