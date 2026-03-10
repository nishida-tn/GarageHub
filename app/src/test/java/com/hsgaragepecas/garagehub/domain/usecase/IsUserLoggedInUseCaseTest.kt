package com.hsgaragepecas.garagehub.domain.usecase

import com.hsgaragepecas.garagehub.data.local.user.UserPreferencesDataSource
import com.hsgaragepecas.garagehub.data.model.UserPreferences
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class IsUserLoggedInUseCaseTest {

    private lateinit var userPreferencesDataSource: UserPreferencesDataSource
    private lateinit var isUserLoggedInUseCase: IsUserLoggedInUseCase

    @Before
    fun setUp() {
        userPreferencesDataSource = mockk()
        isUserLoggedInUseCase = IsUserLoggedInUseCase(userPreferencesDataSource)
    }

    @Test
    fun `invoke with token returns true`() = runTest {
        // Arrange
        val userPreferences = UserPreferences(
            token = "test_token",
            uid = 1,
            name = "Test User",
            portalAccess = "access",
            subscription = "sub"
        )
        coEvery { userPreferencesDataSource.userPreferences } returns flowOf(userPreferences)

        // Act
        val result = isUserLoggedInUseCase()

        // Assert
        assertTrue(result)
    }

    @Test
    fun `invoke without token returns false`() = runTest {
        // Arrange
        val userPreferences = UserPreferences(
            token = null,
            uid = null,
            name = null,
            portalAccess = null,
            subscription = null
        )
        coEvery { userPreferencesDataSource.userPreferences } returns flowOf(userPreferences)

        // Act
        val result = isUserLoggedInUseCase()

        // Assert
        assertFalse(result)
    }
}
