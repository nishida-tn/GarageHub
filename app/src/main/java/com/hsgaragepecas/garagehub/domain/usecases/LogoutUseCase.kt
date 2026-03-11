package com.hsgaragepecas.garagehub.domain.usecases

import com.hsgaragepecas.garagehub.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Use case for logging out a user.
 *
 * @param authRepository The authentication repository.
 */
class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Executes the logout use case.
     */
    suspend operator fun invoke() {
        authRepository.logout()
    }
}
