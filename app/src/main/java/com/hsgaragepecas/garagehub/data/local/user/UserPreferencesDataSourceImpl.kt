package com.hsgaragepecas.garagehub.data.local.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hsgaragepecas.garagehub.data.model.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * An implementation of the [UserPreferencesDataSource] that uses [DataStore] to store the user
 * preferences.
 */
class UserPreferencesDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesDataSource {
    private object Keys {
        val TOKEN = stringPreferencesKey("token")
        val UID = intPreferencesKey("uid")
        val NAME = stringPreferencesKey("name")
        val PORTAL_ACCESS = stringPreferencesKey("portal_access")
        val SUBSCRIPTION = stringPreferencesKey("subscription")
    }

    override val userPreferences: Flow<UserPreferences> = dataStore.data.map { preferences ->
        UserPreferences(
            token = preferences[Keys.TOKEN],
            uid = preferences[Keys.UID],
            name = preferences[Keys.NAME],
            portalAccess = preferences[Keys.PORTAL_ACCESS],
            subscription = preferences[Keys.SUBSCRIPTION]
        )
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[Keys.TOKEN] = token
        }
    }

    override suspend fun saveUserPreferences(userPreferences: UserPreferences) {
        dataStore.edit { preferences ->
            userPreferences.token?.let { preferences[Keys.TOKEN] = it }
            userPreferences.uid?.let { preferences[Keys.UID] = it }
            userPreferences.name?.let { preferences[Keys.NAME] = it }
            userPreferences.portalAccess?.let { preferences[Keys.PORTAL_ACCESS] = it }
            userPreferences.subscription?.let { preferences[Keys.SUBSCRIPTION] = it }
        }
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
