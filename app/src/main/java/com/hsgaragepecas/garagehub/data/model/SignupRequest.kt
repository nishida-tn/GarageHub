package com.hsgaragepecas.garagehub.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request body for the signup endpoint.
 */
@Serializable
data class SignupRequest(
    val email: String,
    @SerialName("senha")
    val password: String,
    val name: String? = null,
    val whatsapp: String,
    val portal: String = "hs"
)
