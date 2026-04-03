package com.hsgaragepecas.garagehub.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request body for the reset password endpoint.
 */
@Serializable
data class ResetPasswordRequest(
    val token: String,
    @SerialName("new_password")
    val newPassword: String
)
