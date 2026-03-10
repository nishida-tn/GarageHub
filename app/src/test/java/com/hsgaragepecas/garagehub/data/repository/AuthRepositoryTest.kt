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
            ok = true,
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
        assert(result is Result.Success<*>)
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
        assert(result is Result.Error)
    }
}
