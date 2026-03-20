package com.hsgaragepecas.garagehub.data.remote

import com.hsgaragepecas.garagehub.data.model.EstimateDetailResponse
import com.hsgaragepecas.garagehub.data.model.EstimateListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * The Retrofit service interface for the Estimate API.
 */
interface EstimateService {

    /**
     * Gets a list of estimates.
     *
     * @param status The status filter.
     * @param query The search query.
     * @param page The page number.
     * @param pageSize The number of items per page.
     * @return The estimate list response.
     */
    @GET("oficina/orcamentos")
    suspend fun getEstimates(
        @Query("status") status: String? = null,
        @Query("q") query: String? = null,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): EstimateListResponse

    /**
     * Gets the details of an estimate.
     *
     * @param estimateId The ID of the estimate.
     * @return The estimate detail response.
     */
    @GET("oficina/orcamentos/{orc_id}")
    suspend fun getEstimateDetail(
        @Path("orc_id") estimateId: Int
    ): EstimateDetailResponse
}
