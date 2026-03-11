package com.hsgaragepecas.garagehub.domain.repository

import com.hsgaragepecas.garagehub.data.model.LoginResponse
import com.hsgaragepecas.garagehub.data.model.MeResponse
import com.hsgaragepecas.garagehub.domain.Result

/**
 * Repository for handling authentication.
 */
interface AuthRepository {
    /**
     * Logs in a user.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @return A result indicating whether the login was successful.
     */
    suspend fun login(email: String, password: String): Result<LoginResponse>

    /**
     * Gets the current user's data.
     *
     * @return A result containing the user's data.
     */
    suspend fun getMe(): Result<MeResponse>

    /**
     * Logs out the current user.
     */
    suspend fun logout()
}
