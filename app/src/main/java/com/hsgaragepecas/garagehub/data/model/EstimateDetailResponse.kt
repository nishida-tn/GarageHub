package com.hsgaragepecas.garagehub.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EstimateDetailResponse(
    @SerialName("ok") val ok: Boolean,
    @SerialName("orcamento") val orcamento: EstimateFullDto? = null,
    @SerialName("itens") val items: List<EstimateItemDto> = emptyList(),
    @SerialName("fotos") val photos: List<String> = emptyList(),
    @SerialName("propostas") val proposals: List<ProposalDto> = emptyList()
)

@Serializable
data class EstimateFullDto(
    @SerialName("id") val id: Int,
    @SerialName("titulo") val title: String? = null,
    @SerialName("descricao") val description: String? = null,
    @SerialName("mo_valor_hora") val moHourValue: Double? = null,
    @SerialName("pintura_valor_hora") val paintingHourValue: Double? = null,
    @SerialName("cliente_nome") val clientName: String? = null,
    @SerialName("cliente_tel") val clientTel: String? = null,
    @SerialName("cliente_whats") val clientWhats: String? = null,
    @SerialName("cliente_cep") val clientCep: String? = null,
    @SerialName("cliente_endereco") val clientAddress: String? = null,
    @SerialName("cliente_numero") val clientNumber: String? = null,
    @SerialName("cliente_bairro") val clientNeighborhood: String? = null,
    @SerialName("cliente_cidade") val clientCity: String? = null,
    @SerialName("cliente_uf") val clientUf: String? = null,
    @SerialName("cliente_complemento") val clientComplement: String? = null,
    @SerialName("veiculo_placa") val vehiclePlate: String? = null,
    @SerialName("veiculo_marca") val vehicleBrand: String? = null,
    @SerialName("veiculo_modelo") val vehicleModel: String? = null,
    @SerialName("veiculo_ano") val vehicleYear: String? = null,
    @SerialName("veiculo_fipe") val vehicleFipe: String? = null,
    @SerialName("veiculo_ano_fab") val vehicleYearFab: Int? = null,
    @SerialName("veiculo_ano_mod") val vehicleYearMod: Int? = null,
    @SerialName("veiculo_chassi") val vehicleChassis: String? = null,
    @SerialName("veiculo_combustivel") val vehicleFuel: String? = null,
    @SerialName("veiculo_ar") val vehicleAir: String? = null,
    @SerialName("veiculo_direcao") val vehicleSteering: String? = null,
    @SerialName("veiculo_cambio") val vehicleTransmission: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)

@Serializable
data class EstimateItemDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("query") val query: String? = null,
    @SerialName("pf_id") val pfId: Int? = null,
    @SerialName("pe_id") val peId: Int? = null,
    @SerialName("marca") val brand: String? = null,
    @SerialName("quantidade") val quantity: Int = 1,
    @SerialName("preco_unit") val unitPrice: Double? = null,
    @SerialName("cod_genuino") val genuineCode: String? = null,
    @SerialName("nome_peca") val partName: String? = null,
    @SerialName("horas_t") val hoursT: Double? = 0.0,
    @SerialName("horas_ri") val hoursRi: Double? = 0.0,
    @SerialName("horas_r") val hoursR: Double? = 0.0,
    @SerialName("horas_p") val hoursP: Double? = 0.0,
    @SerialName("sel_t") val selT: Int? = 0,
    @SerialName("sel_ri") val selRi: Int? = 0,
    @SerialName("sel_r") val selR: Int? = 0,
    @SerialName("sel_p") val selP: Int? = 0,
    @SerialName("valor_t") val valueT: Double? = null,
    @SerialName("valor_ri") val valueRi: Double? = null,
    @SerialName("valor_r") val valueR: Double? = null,
    @SerialName("valor_p") val valueP: Double? = null,
    @SerialName("valor_total") val totalValue: Double? = null
)

@Serializable
data class ProposalDto(
    @SerialName("peca_id") val partId: Int? = null,
    @SerialName("nome_peca") val partName: String? = null,
    @SerialName("marca") val brand: String? = null,
    @SerialName("preco_unit") val unitPrice: Double? = null,
    @SerialName("quantidade") val quantity: Int? = null,
    @SerialName("loja_nome") val shopName: String? = null,
    @SerialName("loja_whats") val shopWhats: String? = null,
    @SerialName("proposta_id") val proposalId: Int? = null,
    @SerialName("is_proposal") val isProposal: Boolean = true
)
