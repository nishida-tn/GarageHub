package com.hsgaragepecas.garagehub.data.repository

import com.hsgaragepecas.garagehub.data.model.LoginRequest
import com.hsgaragepecas.garagehub.data.model.LoginResponse
import com.hsgaragepecas.garagehub.data.remote.AuthService
import com.hsgaragepecas.garagehub.domain.Result
import com.hsgaragepecas.garagehub.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AuthRepositoryTest {

    private lateinit var authService: AuthService
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        authService = mockk()
        authRepository = AuthRepositoryImpl(authService)
    }

    @Test
    fun `login with valid credentials returns success`() = runTest {
        // Arrange
        val loginResponse = LoginResponse("test_token")
        coEvery { authService.login(any()) } returns loginResponse

        // Act
        val result = authRepository.login("test@example.com", "password")

        // Assert
        assert(result is Result.Success<*>)
        assertEquals(loginResponse, (result as Result.Success).data)
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
        assertEquals(exception, (result as Result.Error).exception)
    }
}
