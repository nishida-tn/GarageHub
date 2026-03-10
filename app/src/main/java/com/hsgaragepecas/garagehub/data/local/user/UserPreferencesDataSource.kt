package com.hsgaragepecas.garagehub.data.local.user

import com.hsgaragepecas.garagehub.data.model.UserPreferences
import kotlinx.coroutines.flow.Flow

/**
 * A data source for user preferences.
 */
interface UserPreferencesDataSource {
    /**
     * The user preferences.
     */
    val userPreferences: Flow<UserPreferences>

    /**
     * Saves the authentication token.
     *
     * @param token The token to save.
     */
    suspend fun saveToken(token: String)

    /**
     * Saves the user preferences.
     *
     * @param userPreferences The user preferences to save.
     */
    suspend fun saveUserPreferences(userPreferences: UserPreferences)

    /**
     * Clears the user preferences.
     */
    suspend fun clear()
}
