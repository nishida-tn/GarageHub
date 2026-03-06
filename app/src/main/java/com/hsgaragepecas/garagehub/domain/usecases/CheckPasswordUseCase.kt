package com.hsgaragepecas.garagehub.domain.usecases

import javax.inject.Inject

/**
 * A use case that checks if a password is valid.
 */
class CheckPasswordUseCase @Inject constructor() {

    /**
     * The result of a password check.
     */
    sealed class PasswordResult {
        /**
         * The password is valid.
         */
        object Success : PasswordResult()
        /**
         * The current password is invalid.
         */
        object InvalidCurrentPassword : PasswordResult()
        /**
         * The new password is empty.
         */
        object EmptyNewPassword : PasswordResult()
        /**
         * The new password is too short.
         */
        object NewPasswordTooShort : PasswordResult()
        /**
         * The new password is the same as the old password.
         */
        object PasswordUnchanged : PasswordResult()
        /**
         * The new password and the confirmation password do not match.
         */
        object PasswordsDoNotMatch : PasswordResult()
    }

    /**
     * Invokes the use case.
     *
     * @param currentPassword The current password.
     * @param newPassword The new password.
     * @param confirmPassword The confirmation of the new password.
     * @param storedPassword The stored password.
     * @return The result of the password check.
     */
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
