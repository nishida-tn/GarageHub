package com.hsgaragepecas.garagehub.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val uid: Int,
    val name: String? = "",
    val portalAccess: String? = "",
    val subscription: String? = ""
)
