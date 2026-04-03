package com.hsgaragepecas.garagehub.data.model

import kotlinx.serialization.Serializable

/**
 * Request body for the forgot password endpoint.
 */
@Serializable
data class ForgotPasswordRequest(
    val email: String
)
