package com.hsgaragepecas.garagehub.data.remote

import com.hsgaragepecas.garagehub.data.model.LoginRequest
import com.hsgaragepecas.garagehub.data.model.LoginResponse
import com.hsgaragepecas.garagehub.data.model.MeResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * The Retrofit service interface for the authentication API.
 */
interface AuthService {

    /**
     * Logs in a user.
     *
     * @param request The login request.
     * @return The login response.
     */
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    /**
     * Gets the current user's data.
     *
     * @return The user's data.
     */
    @GET("auth/me")
    suspend fun getMe(): MeResponse
}
