package com.hsgaragepecas.garagehub.data.remote

import com.hsgaragepecas.garagehub.data.model.ViaCepResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit service for the ViaCEP API.
 */
interface ViaCepService {

    /**
     * Gets the address information for a given CEP.
     *
     * @param cep The CEP to look up.
     * @return The address information.
     */
    @GET("{cep}/json/")
    suspend fun getAddress(@Path("cep") cep: String): ViaCepResponse
}
