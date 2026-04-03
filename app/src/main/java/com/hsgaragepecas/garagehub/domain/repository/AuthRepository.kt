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
     * Signs up a new user.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @param name The user's name.
     * @param whatsapp The user's WhatsApp number.
     * @return A result indicating whether the signup was successful.
     */
    suspend fun signup(email: String, password: String, name: String?, whatsapp: String): Result<Unit>

    /**
     * Requests a password reset email.
     *
     * @param email The user's email.
     * @return A result indicating whether the request was successful.
     */
    suspend fun forgotPassword(email: String): Result<Unit>

    /**
     * Resets the user's password using a token.
     *
     * @param token The reset token.
     * @param newPassword The new password.
     * @return A result indicating whether the reset was successful.
     */
    suspend fun resetPassword(token: String, newPassword: String): Result<Unit>

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
