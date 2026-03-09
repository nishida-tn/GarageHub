package com.hsgaragepecas.garagehub.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    @SerialName("senha")
    val password: String,
    val portal: String
)
