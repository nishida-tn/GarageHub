package com.hsgaragepecas.garagehub.ui.estimate.edit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.hsgaragepecas.garagehub.data.model.EstimateFullDto
import com.hsgaragepecas.garagehub.data.model.EstimateItemDto
import com.hsgaragepecas.garagehub.ui.estimate.edit.EditEstimateContract.EditEstimateUiIntent
import com.hsgaragepecas.garagehub.ui.estimate.edit.EditEstimateContract.EditEstimateUiState
import com.hsgaragepecas.garagehub.ui.theme.GarageHubTheme
import com.hsgaragepecas.garagehub.ui.theme.GarageYellow

@Composable
fun EditEstimateScreen(
    estimateId: Int,
    viewModel: EditEstimateViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(estimateId) {
        viewModel.onIntent(EditEstimateUiIntent.LoadEstimate(estimateId))
    }

    EditEstimateContent(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        onBackClick = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditEstimateContent(
    uiState: EditEstimateUiState,
    onIntent: (EditEstimateUiIntent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Orçamento #${uiState.estimate?.id ?: ""}", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GarageYellow)
            }
        } else {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    EditInputField(
                        label = "Valor hora M.O.",
                        value = uiState.estimate?.moHourValue?.toString() ?: "",
                        onValueChange = {},
                        modifier = Modifier.weight(1f)
                    )
                    EditInputField(
                        label = "Valor hora Pintura",
                        value = uiState.estimate?.paintingHourValue?.toString() ?: "",
                        onValueChange = {},
                        modifier = Modifier.weight(1f)
                    )
                }

                SectionHeader(title = "Dados de cliente")
                EditInputField(label = "Nome", value = uiState.estimate?.clientName ?: "", onValueChange = {})
                EditInputField(label = "Tel.", value = uiState.estimate?.clientTel ?: "", onValueChange = {})
                EditInputField(label = "Whats", value = uiState.estimate?.clientWhats ?: "", onValueChange = {})
                EditInputField(label = "CEP", value = uiState.estimate?.clientCep ?: "", onValueChange = {})
                EditInputField(label = "Endereço", value = uiState.estimate?.clientAddress ?: "", onValueChange = {})
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    EditInputField(label = "N°", value = uiState.estimate?.clientNumber ?: "", onValueChange = {}, modifier = Modifier.weight(1f))
                    EditInputField(label = "Bairro", value = uiState.estimate?.clientNeighborhood ?: "", onValueChange = {}, modifier = Modifier.weight(2f))
                }
                EditInputField(label = "Cidade", value = uiState.estimate?.clientCity ?: "", onValueChange = {})
                EditInputField(label = "UF", value = uiState.estimate?.clientUf ?: "", onValueChange = {})
                EditInputField(label = "Complemento", value = uiState.estimate?.clientComplement ?: "", onValueChange = {})

                SectionHeader(title = "Dados de veículo")
                EditInputField(label = "Placa", value = uiState.estimate?.vehiclePlate ?: "", onValueChange = {})
                DropdownField(label = "Marca", selectedOption = uiState.estimate?.vehicleBrand ?: "Selecione")
                DropdownField(label = "Modelo", selectedOption = uiState.estimate?.vehicleModel ?: "Selecione")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    EditInputField(label = "Ano fabric.", value = uiState.estimate?.vehicleYearFab?.toString() ?: "", onValueChange = {}, modifier = Modifier.weight(1f))
                    EditInputField(label = "Ano modelo", value = uiState.estimate?.vehicleYearMod?.toString() ?: "", onValueChange = {}, modifier = Modifier.weight(1f))
                }
                EditInputField(label = "Chassi (opcional)", value = uiState.estimate?.vehicleChassis ?: "", onValueChange = {})
                DropdownField(label = "Combustível (opcional)", selectedOption = uiState.estimate?.vehicleFuel ?: "Selecione")
                DropdownField(label = "Ar condicionado (opcional)", selectedOption = uiState.estimate?.vehicleAir ?: "Selecione")
                DropdownField(label = "Direção (opcional)", selectedOption = uiState.estimate?.vehicleSteering ?: "Selecione")
                DropdownField(label = "Câmbio (opcional)", selectedOption = uiState.estimate?.vehicleTransmission ?: "Selecione")

                SectionHeader(title = "Fotos do veículo (máx. 10)")
                PhotoPicker()
                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                    uiState.photos.forEach { photoUrl ->
                        AsyncImage(
                            model = photoUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .padding(end = 8.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                SectionHeader(title = "Itens / serviços")
                ItemInputSection()

                Spacer(modifier = Modifier.height(16.dp))
                
                uiState.items.forEach { item ->
                    ItemRow(item = item, onIntent = onIntent)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                SummarySection(uiState.items)

                Spacer(modifier = Modifier.height(24.dp))
                
                ActionButtons(onIntent)
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

@Composable
private fun EditInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null
) {
    Column(modifier = modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = placeholder?.let { { Text(it, color = Color.Gray) } },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GarageYellow,
                unfocusedBorderColor = Color.DarkGray
            ),
            singleLine = true
        )
    }
}

@Composable
private fun DropdownField(label: String, selectedOption: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Surface(
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.DarkGray),
            color = Color.Transparent
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = selectedOption, color = Color.White)
                Text(text = "▼", fontSize = 10.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun PhotoPicker() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.weight(1f).height(40.dp),
            color = Color.DarkGray.copy(alpha = 0.3f),
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(1.dp, Color.DarkGray)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(color = Color.White, shape = RoundedCornerShape(2.dp)) {
                    Text(
                        "Choose Files",
                        fontSize = 12.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("No file chosen", fontSize = 12.sp, color = Color.Gray)
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = {}, modifier = Modifier.size(40.dp)) {
            Icon(Icons.Default.PhotoCamera, contentDescription = "Camera", tint = GarageYellow)
        }
    }
}

@Composable
private fun ItemInputSection() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        EditInputField(label = "Cód. genuíno", value = "", onValueChange = {})
        EditInputField(label = "Peça", value = "", onValueChange = {}, placeholder = "Ex: Paralama esquerdo")
        
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ItemCheckbox(label = "T (h)")
            ItemCheckbox(label = "R&I (h)")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ItemCheckbox(label = "R (h)")
            ItemCheckbox(label = "P (h)")
        }
        
        EditInputField(label = "Preço peça", value = "0,00", onValueChange = {})
        EditInputField(label = "Total", value = "", onValueChange = {})
        
        Button(
            onClick = {},
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(containerColor = GarageYellow),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Add", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ItemCheckbox(label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = false,
            onCheckedChange = {},
            colors = CheckboxDefaults.colors(checkedColor = GarageYellow)
        )
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun ItemRow(item: EstimateItemDto, onIntent: (EditEstimateUiIntent) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray.copy(alpha = 0.2f)),
        border = BorderStroke(1.dp, Color.DarkGray)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(color = GarageYellow, shape = RoundedCornerShape(4.dp)) {
                    Text(
                        text = "T", // This should be dynamic based on selection
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item.partName ?: "", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            }
            Text(text = "D-EPD", color = Color.Green, fontSize = 10.sp) // Dummy status
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = "R$ ${item.totalValue ?: 0.0}", color = Color.Cyan, fontWeight = FontWeight.Bold)
                    Text(text = "R$ Peça: ${item.unitPrice ?: 0.0}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "R$ Serv: ${item.valueT ?: 0.0}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Row {
                        IconButton(onClick = {}, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Share, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = {}, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
            Text(
                text = "Total R$ ${item.totalValue ?: 0.0}",
                color = GarageYellow,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun SummarySection(items: List<EstimateItemDto>) {
    val totalParts = items.sumOf { (it.unitPrice ?: 0.0) * it.quantity }
    val totalService = items.sumOf { (it.valueT ?: 0.0) + (it.valueRi ?: 0.0) + (it.valueR ?: 0.0) + (it.valueP ?: 0.0) }
    val total = totalParts + totalService

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black),
        border = BorderStroke(1.dp, GarageYellow)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            SummaryItem(label = "TOTAL PEÇAS", value = "R$ ${"%,.2f".format(totalParts)}")
            SummaryItem(label = "TOTAL SERV.", value = "R$ ${"%,.2f".format(totalService)}", valueColor = GarageYellow)
            SummaryItem(label = "TOTAL ORÇAMENTO", value = "R$ ${"%,.2f".format(total)}", valueColor = GarageYellow)
        }
    }
}

@Composable
private fun SummaryItem(label: String, value: String, valueColor: Color = Color.White) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 10.sp, color = Color.Gray)
        Text(text = value, fontWeight = FontWeight.Bold, color = valueColor, fontSize = 14.sp)
    }
}

@Composable
private fun ActionButtons(onIntent: (EditEstimateUiIntent) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = { onIntent(EditEstimateUiIntent.SaveEstimate) },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GarageYellow),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Salvar orçamento", color = Color.Black, fontWeight = FontWeight.Bold)
        }
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { onIntent(EditEstimateUiIntent.MakeOrder) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = GarageYellow),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Fazer Pedido", color = Color.Black, fontSize = 12.sp)
            }
            Button(
                onClick = { onIntent(EditEstimateUiIntent.GeneratePdf) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("PDF", fontSize = 12.sp)
            }
            Button(
                onClick = { onIntent(EditEstimateUiIntent.SendWhatsApp) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Whats", fontSize = 12.sp)
            }
        }
        
        Button(
            onClick = { onIntent(EditEstimateUiIntent.CreateDemand) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Demanda", color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditEstimateScreenPreview() {
    GarageHubTheme(darkTheme = true) {
        EditEstimateContent(
            uiState = EditEstimateUiState(
                estimate = EstimateFullDto(
                    id = 53,
                    clientName = "Teste Logística",
                    vehicleModel = "320i",
                    vehiclePlate = "AAA1134",
                    moHourValue = 100.0,
                    paintingHourValue = 120.0
                )
            ),
            onIntent = {},
            onBackClick = {}
        )
    }
}
