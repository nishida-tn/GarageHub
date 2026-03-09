package com.hsgaragepecas.garagehub.domain.usecases

import com.hsgaragepecas.garagehub.data.model.LoginResponse
import com.hsgaragepecas.garagehub.domain.Result
import com.hsgaragepecas.garagehub.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Use case for logging in a user.
 *
 * @param authRepository The authentication repository.
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Executes the login use case.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @return A result indicating whether the login was successful.
     */
    suspend operator fun invoke(email: String, password: String): Result<LoginResponse> {
        return authRepository.login(email, password)
    }
}
