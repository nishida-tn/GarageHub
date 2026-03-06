package com.hsgaragepecas.garagehub.domain.usecases

import com.hsgaragepecas.garagehub.domain.usecases.CheckPasswordUseCase.PasswordResult
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CheckPasswordUseCaseTest {

    private lateinit var checkPasswordUseCase: CheckPasswordUseCase

    @Before
    fun setUp() {
        checkPasswordUseCase = CheckPasswordUseCase()
    }

    private val storedPassword = "password123"

    @Test
    fun `invoke with valid passwords should return success`() {
        val result = checkPasswordUseCase(
            currentPassword = "password123",
            newPassword = "newPassword456",
            confirmPassword = "newPassword456",
            storedPassword = storedPassword
        )
        assertEquals(PasswordResult.Success, result)
    }

    @Test
    fun `invoke with incorrect current password should return invalid current password`() {
        val result = checkPasswordUseCase(
            currentPassword = "wrongPassword",
            newPassword = "newPassword456",
            confirmPassword = "newPassword456",
            storedPassword = storedPassword
        )
        assertEquals(PasswordResult.InvalidCurrentPassword, result)
    }

    @Test
    fun `invoke with empty new password should return empty new password`() {
        val result = checkPasswordUseCase(
            currentPassword = "password123",
            newPassword = "",
            confirmPassword = "",
            storedPassword = storedPassword
        )
        assertEquals(PasswordResult.EmptyNewPassword, result)
    }
    
    @Test
    fun `invoke with new password shorter than 6 characters should return new password too short`() {
        val result = checkPasswordUseCase(
            currentPassword = "password123",
            newPassword = "12345",
            confirmPassword = "12345",
            storedPassword = storedPassword
        )
        assertEquals(PasswordResult.NewPasswordTooShort, result)
    }

    @Test
    fun `invoke with new password same as current should return password unchanged`() {
        val result = checkPasswordUseCase(
            currentPassword = "password123",
            newPassword = "password123",
            confirmPassword = "password123",
            storedPassword = storedPassword
        )
        assertEquals(PasswordResult.PasswordUnchanged, result)
    }
    
    @Test
    fun `invoke with mismatched new and confirm passwords should return passwords do not match`() {
        val result = checkPasswordUseCase(
            currentPassword = "password123",
            newPassword = "newPassword456",
            confirmPassword = "newPassword789",
            storedPassword = storedPassword
        )
        assertEquals(PasswordResult.PasswordsDoNotMatch, result)
    }
}
