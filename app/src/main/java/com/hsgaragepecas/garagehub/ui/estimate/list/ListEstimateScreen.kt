package com.hsgaragepecas.garagehub.ui.estimate.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.hsgaragepecas.garagehub.R
import com.hsgaragepecas.garagehub.data.model.EstimateDto
import com.hsgaragepecas.garagehub.ui.estimate.list.ListEstimateContract.ListEstimateUiIntent
import com.hsgaragepecas.garagehub.ui.estimate.list.ListEstimateContract.ListEstimateUiState
import com.hsgaragepecas.garagehub.ui.theme.GarageHubTheme
import com.hsgaragepecas.garagehub.ui.theme.GarageYellow

@Composable
fun ListEstimateScreen(
    viewModel: ListEstimateViewModel = hiltViewModel(),
    onNavigateToCreateEstimate: () -> Unit,
    onNavigateToEstimateDetails: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onIntent(ListEstimateUiIntent.LoadEstimates)
    }

    ListEstimateContent(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        onNavigateToCreateEstimate = onNavigateToCreateEstimate,
        onNavigateToEstimateDetails = onNavigateToEstimateDetails
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListEstimateContent(
    uiState: ListEstimateUiState,
    onIntent: (ListEstimateUiIntent) -> Unit,
    onNavigateToCreateEstimate: () -> Unit,
    onNavigateToEstimateDetails: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Orçamentos", fontWeight = FontWeight.Bold) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreateEstimate,
                containerColor = GarageYellow,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Novo Orçamento", tint = Color.Black)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            StatusFilters(
                selectedStatus = uiState.selectedStatus,
                onStatusSelected = { onIntent(ListEstimateUiIntent.FilterByStatus(it)) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = { onIntent(ListEstimateUiIntent.SearchEstimates(it)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading && uiState.estimates.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = GarageYellow
                )
            } else if (uiState.error != null && uiState.estimates.isEmpty()) {
                Text(
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.estimates) { estimate ->
                        EstimateCard(
                            estimate = estimate,
                            onClick = { onNavigateToEstimateDetails(estimate.id) },
                            onDeleteClick = { onIntent(ListEstimateUiIntent.DeleteEstimate(estimate.id)) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusFilters(
    selectedStatus: String?,
    onStatusSelected: (String?) -> Unit
) {
    val statuses = listOf(
        "Todos" to null,
        "Aguardando" to "aguardando",
        "Aprovados" to "aprovado",
        "Concluídos" to "concluido",
        "Demandas" to "demanda",
        "Cancelados" to "cancelado"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        statuses.forEach { (label, status) ->
            val selected = selectedStatus == status
            FilterChip(
                selected = selected,
                onClick = { onStatusSelected(status) },
                label = { Text(label) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = GarageYellow,
                    selectedLabelColor = Color.Black,
                    labelColor = Color.Gray
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selected,
                    borderColor = Color.DarkGray,
                    selectedBorderColor = Color.Transparent,
                    borderWidth = 1.dp
                )
            )
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Buscar por placa, cliente...", color = Color.Gray) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.DarkGray,
            focusedBorderColor = GarageYellow
        )
    )
}

@Composable
fun EstimateCard(
    estimate: EstimateDto,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                if (estimate.mainPhoto != null) {
                    AsyncImage(
                        model = estimate.mainPhoto,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${estimate.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                StatusTag(status = estimate.status)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = estimate.vehicleModel ?: "Modelo não informado",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "${estimate.clientName ?: "Cliente"} - ${estimate.vehiclePlate ?: "Placa"}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.DarkGray),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Text("Abrir")
                }

                Button(
                    onClick = onDeleteClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GarageYellow, contentColor = Color.Black)
                ) {
                    Text("Excluir")
                }
            }
        }
    }
}

@Composable
fun StatusTag(status: String?) {
    val (text, color) = when (status?.lowercase()) {
        "aguardando" -> "AGUARDANDO" to Color(0xFFFFA500)
        "aprovado" -> "APROVADO" to Color.Green
        "concluido" -> "CONCLUÍDO" to Color.Cyan
        "demanda" -> "DEMANDA" to Color(0xFF00BFFF)
        "cancelado" -> "CANCELADO" to Color.Red
        else -> (status?.uppercase() ?: "DESCONHECIDO") to Color.Gray
    }

    Surface(
        color = Color.Transparent,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, color)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = color,
            fontSize = 10.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ListEstimateScreenPreview() {
    val sampleEstimates = listOf(
        EstimateDto(
            id = 83,
            clientName = "Teste Logística",
            vehiclePlate = "AAA1134",
            vehicleModel = "BMW 320i",
            status = "aguardando",
            totalValue = 500.0,
            createdAt = "2023-10-27T10:00:00Z"
        ),
        EstimateDto(
            id = 52,
            clientName = "Cliente",
            vehiclePlate = "AAA1134",
            vehicleModel = "Toyota Corolla XEi 1.8/1.8 Flex 16V Aut.",
            status = "demanda",
            totalValue = 1200.50,
            createdAt = "2023-10-26T15:30:00Z"
        )
    )
    val uiState = ListEstimateUiState(
        estimates = sampleEstimates,
        isLoading = false,
        selectedStatus = null
    )

    GarageHubTheme(darkTheme = true) {
        ListEstimateContent(
            uiState = uiState,
            onIntent = {},
            onNavigateToCreateEstimate = {},
            onNavigateToEstimateDetails = {}
        )
    }
}
