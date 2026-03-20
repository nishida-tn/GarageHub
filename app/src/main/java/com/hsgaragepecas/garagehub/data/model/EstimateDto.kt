package com.hsgaragepecas.garagehub.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EstimateDto(
    @SerialName("id") val id: Int,
    @SerialName("cliente_nome") val clientName: String? = null,
    @SerialName("veiculo_placa") val vehiclePlate: String? = null,
    @SerialName("veiculo_modelo") val vehicleModel: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("foto_principal") val mainPhoto: String? = null,
    @SerialName("valor_total") val totalValue: Double? = null,
    @SerialName("created_at") val createdAt: String? = null
)

@Serializable
data class EstimateListResponse(
    @SerialName("ok") val ok: Boolean,
    @SerialName("items") val items: List<EstimateDto>,
    @SerialName("pagination") val pagination: PaginationDto
)

@Serializable
data class PaginationDto(
    @SerialName("total") val total: Int,
    @SerialName("page") val page: Int,
    @SerialName("size") val size: Int,
    @SerialName("pages") val pages: Int
)
