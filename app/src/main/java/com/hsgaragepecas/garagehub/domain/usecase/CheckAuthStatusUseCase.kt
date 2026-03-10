package com.hsgaragepecas.garagehub.domain.usecase

import com.hsgaragepecas.garagehub.domain.Result
import com.hsgaragepecas.garagehub.domain.repository.AuthRepository
import javax.inject.Inject

class CheckAuthStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return when (val result = authRepository.getMe()) {
            is Result.Success -> Result.Success(true)
            is Result.Error -> Result.Success(false)
        }
    }
}
