package com.hsgaragepecas.garagehub.data.model

import kotlinx.serialization.Serializable

/**
 * A common response from the API.
 *
 * @property ok Whether the request was successful.
 * @property message An optional message from the API.
 */
@Serializable
data class CommonResponse(
    val ok: Boolean,
    val message: String? = null
)
