package com.hsgaragepecas.garagehub.data.model

import kotlinx.serialization.Serializable

/**
 * Data model for the ViaCEP API response.
 */
@Serializable
data class ViaCepResponse(
    val cep: String? = null,
    val logradouro: String? = null,
    val complemento: String? = null,
    val bairro: String? = null,
    val localidade: String? = null,
    val uf: String? = null,
    val erro: Boolean? = null
)
