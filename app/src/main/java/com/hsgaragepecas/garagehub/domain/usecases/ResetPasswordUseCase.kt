package com.hsgaragepecas.garagehub.domain.usecases

import com.hsgaragepecas.garagehub.domain.Result
import com.hsgaragepecas.garagehub.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Use case for resetting the user's password using a token.
 *
 * @param authRepository The authentication repository.
 */
class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Executes the reset password use case.
     *
     * @param token The reset token.
     * @param newPassword The new password.
     * @return A result indicating whether the reset was successful.
     */
    suspend operator fun invoke(token: String, newPassword: String): Result<Unit> {
        return authRepository.resetPassword(token, newPassword)
    }
}
