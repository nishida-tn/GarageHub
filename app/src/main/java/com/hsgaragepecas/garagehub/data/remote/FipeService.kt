package com.hsgaragepecas.garagehub.data.remote

import com.hsgaragepecas.garagehub.data.model.FipeBrandDto
import com.hsgaragepecas.garagehub.data.model.FipeModelResponse
import com.hsgaragepecas.garagehub.data.model.FipeYearDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service for FIPE integration.
 */
interface FipeService {

    /**
     * Gets the list of vehicle brands.
     */
    @GET("oficina/fipe/marcas")
    suspend fun getBrands(): List<FipeBrandDto>

    /**
     * Gets the list of models for a specific brand.
     *
     * @param brand The brand code.
     */
    @GET("oficina/fipe/modelos")
    suspend fun getModels(@Query("marca") brand: String): FipeModelResponse

    /**
     * Gets the list of years for a specific brand and model.
     *
     * @param brand The brand code.
     * @param model The model code.
     */
    @GET("oficina/fipe/anos")
    suspend fun getYears(
        @Query("marca") brand: String,
        @Query("modelo") model: String
    ): List<FipeYearDto>
}
