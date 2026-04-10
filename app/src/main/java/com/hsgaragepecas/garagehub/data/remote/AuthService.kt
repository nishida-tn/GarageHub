package com.hsgaragepecas.garagehub.data.remote

import com.hsgaragepecas.garagehub.data.model.CommonResponse
import com.hsgaragepecas.garagehub.data.model.ForgotPasswordRequest
import com.hsgaragepecas.garagehub.data.model.LoginRequest
import com.hsgaragepecas.garagehub.data.model.LoginResponse
import com.hsgaragepecas.garagehub.data.model.MeResponse
import com.hsgaragepecas.garagehub.data.model.ResetPasswordRequest
import com.hsgaragepecas.garagehub.data.model.SignupRequest
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
     * Signs up a new user.
     *
     * @param request The signup request.
     * @return A map indicating success.
     */
    @POST("auth/signup")
    suspend fun signup(@Body request: SignupRequest): CommonResponse

    /**
     * Requests a password reset email.
     *
     * @param request The forgot password request.
     * @return A map indicating success.
     */
    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): CommonResponse

    /**
     * Resets the user's password using a token.
     *
     * @param request The reset password request.
     * @return A map indicating success.
     */
    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): CommonResponse

    /**
     * Gets the current user's data.
     *
     * @return The user's data.
     */
    @GET("auth/me")
    suspend fun getMe(): MeResponse
}
