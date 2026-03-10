package com.hsgaragepecas.garagehub.domain.usecase

import com.hsgaragepecas.garagehub.data.local.user.UserPreferencesDataSource
import com.hsgaragepecas.garagehub.domain.Result
import com.hsgaragepecas.garagehub.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CheckAuthStatusUseCase @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource
) {
    suspend operator fun invoke(): Result<Boolean> {
        val userPreferences = userPreferencesDataSource.userPreferences.first()
        return Result.Success(!userPreferences.token.isNullOrEmpty())
    }
}
