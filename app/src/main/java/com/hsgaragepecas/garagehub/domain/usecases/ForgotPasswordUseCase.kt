package com.hsgaragepecas.garagehub.domain.usecases

import com.hsgaragepecas.garagehub.domain.Result
import com.hsgaragepecas.garagehub.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Use case for requesting a password reset email.
 *
 * @param authRepository The authentication repository.
 */
class ForgotPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Executes the forgot password use case.
     *
     * @param email The user's email.
     * @return A result indicating whether the request was successful.
     */
    suspend operator fun invoke(email: String): Result<Unit> {
        return authRepository.forgotPassword(email)
    }
}
