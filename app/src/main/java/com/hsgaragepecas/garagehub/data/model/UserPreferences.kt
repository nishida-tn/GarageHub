package com.hsgaragepecas.garagehub.data.model

import kotlinx.serialization.Serializable

/**
 * Represents the user preferences stored in the application.
 *
 * @property token The authentication token for the user.
 * @property uid The unique identifier of the user.
 * @property name The name of the user.
 * @property portalAccess The portal access level of the user.
 * @property subscription The subscription status of the user.
 */
@Serializable
data class UserPreferences(
    val token: String?,
    val uid: Int?,
    val name: String?,
    val portalAccess: String?,
    val subscription: String?
)
