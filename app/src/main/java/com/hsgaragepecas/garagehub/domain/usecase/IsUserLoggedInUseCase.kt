package com.hsgaragepecas.garagehub.domain.usecase

import com.hsgaragepecas.garagehub.data.local.user.UserPreferencesDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * A use case that checks if the user is logged in.
 */
class IsUserLoggedInUseCase @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource
) {
    /**
     * Invokes the use case.
     *
     * @return True if the user is logged in, false otherwise.
     */
    suspend operator fun invoke(): Boolean {
        return userPreferencesDataSource.userPreferences.map { it.token != null }.first()
    }
}
