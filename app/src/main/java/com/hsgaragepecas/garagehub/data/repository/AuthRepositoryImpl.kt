package com.hsgaragepecas.garagehub.data.repository

import com.hsgaragepecas.garagehub.data.model.LoginRequest
import com.hsgaragepecas.garagehub.data.model.LoginResponse
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
    private val authService: AuthService
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
}
