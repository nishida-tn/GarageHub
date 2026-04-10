package com.hsgaragepecas.garagehub.data.repository

import com.hsgaragepecas.garagehub.data.local.user.UserPreferencesDataSource
import com.hsgaragepecas.garagehub.data.model.ForgotPasswordRequest
import com.hsgaragepecas.garagehub.data.model.LoginRequest
import com.hsgaragepecas.garagehub.data.model.LoginResponse
import com.hsgaragepecas.garagehub.data.model.MeResponse
import com.hsgaragepecas.garagehub.data.model.ResetPasswordRequest
import com.hsgaragepecas.garagehub.data.model.SignupRequest
import com.hsgaragepecas.garagehub.data.model.UserPreferences
import com.hsgaragepecas.garagehub.data.remote.AuthService
import com.hsgaragepecas.garagehub.domain.Result
import com.hsgaragepecas.garagehub.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Implementation of the [AuthRepository] interface.
 *
 * @param authService The authentication Retrofit service.
 * @param userPreferencesDataSource The user preferences data source.
 */
class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val userPreferencesDataSource: UserPreferencesDataSource
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = authService.login(LoginRequest(email, password, "hs"))
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
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun signup(
        email: String,
        password: String,
        name: String?,
        whatsapp: String
    ): Result<LoginResponse> {
        return try {
            val cleanWhatsapp = whatsapp.filter { it.isDigit() }
            val signupRequest = SignupRequest(
                email = email,
                password = password,
                name = name,
                whatsapp = cleanWhatsapp,
                portal = "hs"
            )
            val signupResponse = authService.signup(signupRequest)
            if (signupResponse["ok"] == true) {
                // Auto-login after successful signup
                login(email, password)
            } else {
                Result.Error(Exception("Signup failed"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun forgotPassword(email: String): Result<Unit> {
        return try {
            val response = authService.forgotPassword(ForgotPasswordRequest(email))
            if (response["ok"] == true) {
                Result.Success(Unit)
            } else {
                Result.Error(Exception("Forgot password request failed"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun resetPassword(token: String, newPassword: String): Result<Unit> {
        return try {
            val response = authService.resetPassword(ResetPasswordRequest(token, newPassword))
            if (response["ok"] == true) {
                Result.Success(Unit)
            } else {
                Result.Error(Exception("Reset password failed"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getMe(): Result<MeResponse> {
        return try {
            val response = authService.getMe()
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun logout() {
        userPreferencesDataSource.clear()
    }
}
