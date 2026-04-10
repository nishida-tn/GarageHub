package com.hsgaragepecas.garagehub.ui.estimate.edit

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.hsgaragepecas.garagehub.data.model.EstimateItemDto
import com.hsgaragepecas.garagehub.ui.estimate.edit.EditEstimateContract.EditEstimateUiEvent
import com.hsgaragepecas.garagehub.ui.estimate.edit.EditEstimateContract.EditEstimateUiIntent
import com.hsgaragepecas.garagehub.ui.estimate.edit.EditEstimateContract.EditEstimateUiState
import com.hsgaragepecas.garagehub.ui.theme.GarageDivider
import com.hsgaragepecas.garagehub.ui.theme.GarageGreyText
import com.hsgaragepecas.garagehub.ui.theme.GarageHubTheme
import com.hsgaragepecas.garagehub.ui.theme.GarageYellow

@Composable
fun EditEstimateScreen(
    estimateId: Int,
    viewModel: EditEstimateViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(estimateId) {
        viewModel.onIntent(EditEstimateUiIntent.LoadEstimate(estimateId))
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is EditEstimateUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                EditEstimateUiEvent.NavigateBack -> onNavigateBack()
                is EditEstimateUiEvent.OpenUri -> {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(event.uri, "application/pdf")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(Intent.createChooser(intent, "Abrir PDF"))
                }
            }
        }
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
                        value = uiState.moHourValue,
                        onValueChange = { onIntent(EditEstimateUiIntent.OnMoHourValueChange(it)) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    EditInputField(
                        label = "Valor hora Pintura",
                        value = uiState.paintingHourValue,
                        onValueChange = { onIntent(EditEstimateUiIntent.OnPaintingHourValueChange(it)) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                SectionHeader(title = "Dados de cliente")
                EditInputField(label = "Nome", value = uiState.clientName, onValueChange = { onIntent(EditEstimateUiIntent.OnClientNameChange(it)) })
                EditInputField(label = "Tel.", value = uiState.clientTel, onValueChange = { onIntent(EditEstimateUiIntent.OnClientTelChange(it)) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
                EditInputField(label = "Whats", value = uiState.clientWhats, onValueChange = { onIntent(EditEstimateUiIntent.OnClientWhatsChange(it)) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
                EditInputField(label = "CEP", value = uiState.clientCep, onValueChange = { onIntent(EditEstimateUiIntent.OnClientCepChange(it)) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                EditInputField(label = "Endereço", value = uiState.clientAddress, onValueChange = { onIntent(EditEstimateUiIntent.OnClientAddressChange(it)) })
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    EditInputField(label = "N°", value = uiState.clientNumber, onValueChange = { onIntent(EditEstimateUiIntent.OnClientNumberChange(it)) }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    EditInputField(label = "Bairro", value = uiState.clientNeighborhood, onValueChange = { onIntent(EditEstimateUiIntent.OnClientNeighborhoodChange(it)) }, modifier = Modifier.weight(2f))
                }
                EditInputField(label = "Cidade", value = uiState.clientCity, onValueChange = { onIntent(EditEstimateUiIntent.OnClientCityChange(it)) })
                EditInputField(label = "UF", value = uiState.clientUf, onValueChange = { onIntent(EditEstimateUiIntent.OnClientUfChange(it)) })
                EditInputField(label = "Complemento", value = uiState.clientComplement, onValueChange = { onIntent(EditEstimateUiIntent.OnClientComplementChange(it)) })

                SectionHeader(title = "Dados de veículo")
                EditInputField(label = "Placa", value = uiState.vehiclePlate, onValueChange = { onIntent(EditEstimateUiIntent.OnVehiclePlateChange(it)) })
                
                EditDropdownField(
                    label = "Marca",
                    options = uiState.brands,
                    selectedOption = uiState.vehicleBrand,
                    onOptionSelected = { onIntent(EditEstimateUiIntent.OnBrandSelected(it)) },
                    optionLabel = { it.name }
                )
                
                EditDropdownField(
                    label = "Modelo",
                    options = uiState.models,
                    selectedOption = uiState.vehicleModel,
                    onOptionSelected = { onIntent(EditEstimateUiIntent.OnModelSelected(it)) },
                    optionLabel = { it.name }
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    EditDropdownField(
                        label = "Ano fabric.",
                        options = uiState.years,
                        selectedOption = uiState.selectedYear?.name ?: uiState.vehicleYearFab,
                        onOptionSelected = { onIntent(EditEstimateUiIntent.OnYearSelected(it)) },
                        optionLabel = { it.name },
                        modifier = Modifier.weight(1f)
                    )
                    EditInputField(
                        label = "Ano modelo",
                        value = uiState.vehicleYearMod,
                        onValueChange = { onIntent(EditEstimateUiIntent.OnVehicleYearModChange(it)) },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                EditInputField(label = "Chassi (opcional)", value = uiState.vehicleChassis, onValueChange = { onIntent(EditEstimateUiIntent.OnVehicleChassisChange(it)) })
                EditInputField(label = "Combustível (opcional)", value = uiState.vehicleFuel, onValueChange = { onIntent(EditEstimateUiIntent.OnVehicleFuelChange(it)) })
                EditInputField(label = "Ar condicionado (opcional)", value = uiState.vehicleAir, onValueChange = { onIntent(EditEstimateUiIntent.OnVehicleAirChange(it)) })
                EditInputField(label = "Direção (opcional)", value = uiState.vehicleSteering, onValueChange = { onIntent(EditEstimateUiIntent.OnVehicleSteeringChange(it)) })
                EditInputField(label = "Câmbio (opcional)", value = uiState.vehicleTransmission, onValueChange = { onIntent(EditEstimateUiIntent.OnVehicleTransmissionChange(it)) })

                SectionHeader(title = "Fotos do veículo (máx. 10)")
                PhotoPicker()
                if (uiState.photos.isNotEmpty() || uiState.vehiclePhotos.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(uiState.photos) { photoUrl ->
                            val fullUrl = if (photoUrl.startsWith("http")) photoUrl else "https://oficina.hsgaragepecas.com.br/storage/$photoUrl"
                            AsyncImage(
                                model = fullUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                        items(uiState.vehiclePhotos) { uri ->
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                SectionHeader(title = "Itens / serviços")
                ItemInputSection(uiState, onIntent)

                Spacer(modifier = Modifier.height(16.dp))
                
                uiState.items.forEach { item ->
                    ItemRow(item = item, onIntent = onIntent)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                SummarySection(uiState.items)

                Spacer(modifier = Modifier.height(24.dp))
                
                ActionButtons(uiState, onIntent)
                
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
    placeholder: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    readOnly: Boolean = false
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
            singleLine = true,
            keyboardOptions = keyboardOptions,
            readOnly = readOnly
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> EditDropdownField(
    label: String,
    options: List<T>,
    selectedOption: String,
    onOptionSelected: (T) -> Unit,
    optionLabel: (T) -> String,
    modifier: Modifier = Modifier,
    placeholder: String = "Selecione"
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text(text = placeholder, color = GarageGreyText) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GarageYellow,
                    unfocusedBorderColor = Color.DarkGray
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = optionLabel(option)) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
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
private fun ItemInputSection(uiState: EditEstimateUiState, onIntent: (EditEstimateUiIntent) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        EditInputField(label = "Cód. genuíno", value = uiState.itemGenuineCode, onValueChange = { onIntent(EditEstimateUiIntent.OnItemGenuineCodeChange(it)) })
        EditInputField(label = "Peça", value = uiState.itemPartName, onValueChange = { onIntent(EditEstimateUiIntent.OnItemPartNameChange(it)) }, placeholder = "Ex: Paralama esquerdo")
        
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ItemCheckbox(label = "T (h)", checked = uiState.itemTH, onCheckedChange = { onIntent(EditEstimateUiIntent.OnItemTHChange(it)) }, value = uiState.itemTHValue, onValueChange = { onIntent(EditEstimateUiIntent.OnItemTHValueChange(it)) })
            ItemCheckbox(label = "R&I (h)", checked = uiState.itemRiH, onCheckedChange = { onIntent(EditEstimateUiIntent.OnItemRiHChange(it)) }, value = uiState.itemRiHValue, onValueChange = { onIntent(EditEstimateUiIntent.OnItemRiHValueChange(it)) })
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ItemCheckbox(label = "R (h)", checked = uiState.itemRH, onCheckedChange = { onIntent(EditEstimateUiIntent.OnItemRHChange(it)) }, value = uiState.itemRHValue, onValueChange = { onIntent(EditEstimateUiIntent.OnItemRHValueChange(it)) })
            ItemCheckbox(label = "P (h)", checked = uiState.itemPH, onCheckedChange = { onIntent(EditEstimateUiIntent.OnItemPHChange(it)) }, value = uiState.itemPHValue, onValueChange = { onIntent(EditEstimateUiIntent.OnItemPHValueChange(it)) })
        }
        
        EditInputField(label = "Preço peça", value = uiState.itemPartPrice, onValueChange = { onIntent(EditEstimateUiIntent.OnItemPartPriceChange(it)) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        EditInputField(label = "Total", value = uiState.itemTotal, onValueChange = {}, readOnly = true)
        
        Button(
            onClick = { onIntent(EditEstimateUiIntent.AddItem) },
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(containerColor = GarageYellow),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Add", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ItemCheckbox(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.width(150.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(checkedColor = GarageYellow)
            )
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().padding(start = 12.dp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GarageYellow,
                unfocusedBorderColor = Color.DarkGray
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
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
                if (item.selT == 1) LaborBadge("T")
                if (item.selRi == 1) LaborBadge("R&I")
                if (item.selR == 1) LaborBadge("R")
                if (item.selP == 1) LaborBadge("P")
                
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item.partName ?: "", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            }
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = "R$ ${item.totalValue ?: 0.0}", color = Color.Cyan, fontWeight = FontWeight.Bold)
                    Text(text = "R$ Peça: ${item.unitPrice ?: 0.0}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                Column(horizontalAlignment = Alignment.End) {
                    val laborTotal = (item.valueT ?: 0.0) + (item.valueRi ?: 0.0) + (item.valueR ?: 0.0) + (item.valueP ?: 0.0)
                    Text(text = "R$ Serv: $laborTotal", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Row {
                        IconButton(onClick = { /* Share item logic */ }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Share, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = { item.id?.let { onIntent(EditEstimateUiIntent.DeleteItem(it)) } }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LaborBadge(text: String) {
    Surface(
        color = GarageYellow, 
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.padding(end = 4.dp)
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Composable
private fun SummarySection(items: List<EstimateItemDto>) {
    val totalParts = items.sumOf { it.unitPrice ?: 0.0 }
    val totalLabor = items.sumOf { (it.valueT ?: 0.0) + (it.valueRi ?: 0.0) + (it.valueR ?: 0.0) + (it.valueP ?: 0.0) }
    val total = totalParts + totalLabor

    Card(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SummaryRow(label = "Total Peças", value = "R$ $totalParts")
            SummaryRow(label = "Total M.O.", value = "R$ $totalLabor")
            Spacer(modifier = Modifier.height(8.dp))
            SummaryRow(label = "Total Geral", value = "R$ $total", isTotal = true)
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String, isTotal: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal, fontSize = if (isTotal) 18.sp else 14.sp)
        Text(text = value, fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal, fontSize = if (isTotal) 18.sp else 14.sp, color = if (isTotal) GarageYellow else Color.White)
    }
}

@Composable
private fun ActionButtons(uiState: EditEstimateUiState, onIntent: (EditEstimateUiIntent) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = { onIntent(EditEstimateUiIntent.SaveEstimate) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = GarageYellow),
            shape = RoundedCornerShape(8.dp),
            enabled = !uiState.isSaving
        ) {
            if (uiState.isSaving) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.Black)
            else Text("Salvar Orçamento", color = Color.Black, fontWeight = FontWeight.Bold)
        }
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ActionButton(text = "PDF", icon = Icons.Default.Description, modifier = Modifier.weight(1f)) {
                onIntent(EditEstimateUiIntent.GeneratePdf)
            }
            ActionButton(text = "Whats", icon = Icons.Default.Send, modifier = Modifier.weight(1f)) {
                onIntent(EditEstimateUiIntent.SendWhatsApp)
            }
            ActionButton(text = "Demanda", icon = Icons.Default.Share, modifier = Modifier.weight(1f), containerColor = Color(0xFF0D6EFD)) {
                onIntent(EditEstimateUiIntent.CreateDemand)
            }
        }
        
        Button(
            onClick = { onIntent(EditEstimateUiIntent.MakeOrder) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Gerar Pedido", color = Color.White)
        }
    }
}

@Composable
private fun ActionButton(
    text: String, 
    icon: androidx.compose.ui.graphics.vector.ImageVector, 
    modifier: Modifier = Modifier,
    containerColor: Color = Color(0xFF212529),
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        shape = RoundedCornerShape(8.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, fontSize = 12.sp)
    }
}
