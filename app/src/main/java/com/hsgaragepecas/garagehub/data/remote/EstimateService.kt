package com.hsgaragepecas.garagehub.data.remote

import com.hsgaragepecas.garagehub.data.model.CreateDemandRequest
import com.hsgaragepecas.garagehub.data.model.CreateEstimateResponse
import com.hsgaragepecas.garagehub.data.model.EstimateDetailResponse
import com.hsgaragepecas.garagehub.data.model.EstimateListResponse
import com.hsgaragepecas.garagehub.data.model.EstimateUpdateRequest
import com.hsgaragepecas.garagehub.data.model.TimeSuggestionResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    /**
     * Creates a new estimate.
     *
     * @param request The create request.
     * @return The create estimate response.
     */
    @POST("oficina/orcamentos")
    suspend fun createEstimate(
        @Body request: EstimateUpdateRequest
    ): CreateEstimateResponse

    /**
     * Updates an estimate.
     *
     * @param estimateId The ID of the estimate.
     * @param request The update request.
     * @return The estimate detail response.
     */
    @PUT("oficina/orcamentos/{orc_id}")
    suspend fun updateEstimate(
        @Path("orc_id") estimateId: Int,
        @Body request: EstimateUpdateRequest
    ): EstimateDetailResponse

    /**
     * Deletes an estimate.
     *
     * @param estimateId The ID of the estimate.
     * @return A map with the status.
     */
    @DELETE("oficina/orcamentos/{orc_id}")
    suspend fun deleteEstimate(
        @Path("orc_id") estimateId: Int
    ): Map<String, Boolean>

    /**
     * Gets time suggestions for a part.
     *
     * @param partName The name of the part.
     * @return The time suggestion response.
     */
    @GET("oficina/tempo/sugestao")
    suspend fun getTimeSuggestion(
        @Query("peca") partName: String
    ): TimeSuggestionResponse

    /**
     * Generates orders from an estimate.
     *
     * @param estimateId The ID of the estimate.
     * @return A map with the result.
     */
    @POST("oficina/pedidos/from-orcamento/{orc_id}")
    suspend fun generateOrders(
        @Path("orc_id") estimateId: Int
    ): Map<String, kotlinx.serialization.json.JsonElement>

    /**
     * Checks if an item can be deleted from an estimate.
     *
     * @param estimateId The ID of the estimate.
     * @param itemId The ID of the item.
     * @return A map with the result.
     */
    @GET("oficina/orcamentos/{orc_id}/check-item/{item_id}")
    suspend fun checkItemDeletion(
        @Path("orc_id") estimateId: Int,
        @Path("item_id") itemId: Int
    ): Map<String, kotlinx.serialization.json.JsonElement>

    /**
     * Creates a demand for an estimate.
     *
     * @param request The demand request.
     * @return A map with the result.
     */
    @POST("oficina/demandas")
    suspend fun createDemand(
        @Body request: CreateDemandRequest
    ): Map<String, kotlinx.serialization.json.JsonElement>
}
