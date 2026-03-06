package com.hsgaragepecas.garagehub.domain.usecases

import javax.inject.Inject

class CheckPasswordUseCase @Inject constructor() {

    sealed class PasswordResult {
        object Success : PasswordResult()
        object InvalidCurrentPassword : PasswordResult()
        object EmptyNewPassword : PasswordResult()
        object NewPasswordTooShort : PasswordResult()
        object PasswordUnchanged : PasswordResult()
        object PasswordsDoNotMatch : PasswordResult()
    }

    operator fun invoke(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
        storedPassword: String // In a real app, this would be a hash
    ): PasswordResult {
        // In a real implementation, you would hash the `currentPassword` before comparing.
        // For this use case logic, a direct comparison is sufficient.
        if (currentPassword != storedPassword) {
            return PasswordResult.InvalidCurrentPassword
        }
        if (newPassword.isBlank()) {
            return PasswordResult.EmptyNewPassword
        }
        if (newPassword.length < 6) {
            return PasswordResult.NewPasswordTooShort
        }
        if (newPassword == currentPassword) {
            return PasswordResult.PasswordUnchanged
        }
        if (newPassword != confirmPassword) {
            return PasswordResult.PasswordsDoNotMatch
        }

        return PasswordResult.Success
    }
}
