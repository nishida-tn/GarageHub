package com.hsgaragepecas.garagehub.domain.usecases

import com.hsgaragepecas.garagehub.data.model.LoginResponse
import com.hsgaragepecas.garagehub.domain.Result
import com.hsgaragepecas.garagehub.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Use case for signing up a new user.
 *
 * @param authRepository The authentication repository.
 */
class SignupUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Executes the signup use case.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @param name The user's name.
     * @param whatsapp The user's WhatsApp number.
     * @return A result containing the login response if successful.
     */
    suspend operator fun invoke(
        email: String,
        password: String,
        name: String?,
        whatsapp: String
    ): Result<LoginResponse> {
        return authRepository.signup(email, password, name, whatsapp)
    }
}
