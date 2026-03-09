package com.hsgaragepecas.garagehub.data.remote

import com.hsgaragepecas.garagehub.data.model.LoginRequest
import com.hsgaragepecas.garagehub.data.model.LoginResponse
import retrofit2.http.Body
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
}
