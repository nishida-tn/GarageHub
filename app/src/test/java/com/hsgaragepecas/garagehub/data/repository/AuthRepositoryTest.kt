package com.hsgaragepecas.garagehub.data.repository

import com.hsgaragepecas.garagehub.data.local.user.UserPreferencesDataSource
import com.hsgaragepecas.garagehub.data.model.LoginResponse
import com.hsgaragepecas.garagehub.data.model.UserPreferences
import com.hsgaragepecas.garagehub.data.remote.AuthService
import com.hsgaragepecas.garagehub.domain.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AuthRepositoryTest {

    private lateinit var authService: AuthService
    private lateinit var userPreferencesDataSource: UserPreferencesDataSource
    private lateinit var authRepository: AuthRepositoryImpl

    @Before
    fun setUp() {
        authService = mockk()
        userPreferencesDataSource = mockk(relaxUnitFun = true)
        authRepository = AuthRepositoryImpl(authService, userPreferencesDataSource)
    }

    @Test
    fun `login with valid credentials returns success and saves user preferences`() = runTest {
        // Arrange
        val loginResponse = LoginResponse(
            token = "test_token",
            uid = 1,
            name = "Test User",
            portalAccess = "access",
            subscription = "sub"
        )
        val userPreferences = UserPreferences(
            token = loginResponse.token,
            uid = loginResponse.uid,
            name = loginResponse.name,
            portalAccess = loginResponse.portalAccess,
            subscription = loginResponse.subscription
        )
        coEvery { authService.login(any()) } returns loginResponse

        // Act
        val result = authRepository.login("test@example.com", "password")

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(loginResponse, (result as Result.Success).data)
        coVerify { userPreferencesDataSource.saveUserPreferences(userPreferences) }
    }

    @Test
    fun `login with invalid credentials returns error`() = runTest {
        // Arrange
        val exception = Exception("Invalid credentials")
        coEvery { authService.login(any()) } throws exception

        // Act
        val result = authRepository.login("test@example.com", "wrong_password")

        // Assert
        assertTrue(result is Result.Error)
    }

    @Test
    fun `signup with valid data returns success`() = runTest {
        // Arrange
        coEvery { authService.signup(any()) } returns mapOf("ok" to true)

        // Act
        val result = authRepository.signup("test@example.com", "password", "Name", "123456789")

        // Assert
        assertTrue(result is Result.Success)
    }

    @Test
    fun `forgotPassword with valid email returns success`() = runTest {
        // Arrange
        coEvery { authService.forgotPassword(any()) } returns mapOf("ok" to true)

        // Act
        val result = authRepository.forgotPassword("test@example.com")

        // Assert
        assertTrue(result is Result.Success)
    }

    @Test
    fun `resetPassword with valid token and password returns success`() = runTest {
        // Arrange
        coEvery { authService.resetPassword(any()) } returns mapOf("ok" to true)

        // Act
        val result = authRepository.resetPassword("token", "new_password")

        // Assert
        assertTrue(result is Result.Success)
    }
}
