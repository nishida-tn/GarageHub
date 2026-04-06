package com.hsgaragepecas.garagehub.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FipeBrandDto(
    @SerialName("codigo") val code: String,
    @SerialName("nome") val name: String
)

@Serializable
data class FipeModelResponse(
    @SerialName("modelos") val models: List<FipeModelDto>
)

@Serializable
data class FipeModelDto(
    @SerialName("codigo") val code: String,
    @SerialName("nome") val name: String
)

@Serializable
data class FipeYearDto(
    @SerialName("codigo") val code: String,
    @SerialName("nome") val name: String
)
