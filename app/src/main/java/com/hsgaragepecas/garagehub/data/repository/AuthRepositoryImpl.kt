package com.hsgaragepecas.garagehub.data.repository

import com.hsgaragepecas.garagehub.data.local.user.UserPreferencesDataSource
import com.hsgaragepecas.garagehub.data.model.LoginRequest
import com.hsgaragepecas.garagehub.data.model.LoginResponse
import com.hsgaragepecas.garagehub.data.model.MeResponse
import com.hsgaragepecas.garagehub.data.model.UserPreferences
import com.hsgaragepecas.garagehub.data.remote.AuthService
import com.hsgaragepecas.garagehub.domain.Result
import com.hsgaragepecas.garagehub.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Implementation of the [AuthRepository].
 */
class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val userPreferencesDataSource: UserPreferencesDataSource
) : AuthRepository {
    /**
     * Logs in a user.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @return A result indicating whether the login was successful.
     */
    override suspend fun login(email: String, password: String): Result<LoginResponse> = withContext(
        Dispatchers.IO){
        return@withContext try {
            val loginRequest = LoginRequest(email, password, "oficina")
            val response = authService.login(loginRequest)
            userPreferencesDataSource.saveUserPreferences(
                UserPreferences(
                    token = response.token,
                    uid = response.uid,
                    name = response.name,
                    portalAccess = response.portalAccess,
                    subscription = response.subscription
                )
            )
            Result.Success(response)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                Result.Error(Exception("Invalid credentials", e))
            } else {
                Result.Error(e)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getMe(): Result<MeResponse> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = authService.getMe()
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
