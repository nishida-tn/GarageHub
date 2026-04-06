package com.hsgaragepecas.garagehub.ui.estimate

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.hsgaragepecas.garagehub.R
import com.hsgaragepecas.garagehub.ui.estimate.CreateEstimateContract.CreateEstimateUiEvent
import com.hsgaragepecas.garagehub.ui.estimate.CreateEstimateContract.CreateEstimateUiIntent
import com.hsgaragepecas.garagehub.ui.estimate.CreateEstimateContract.CreateEstimateUiState
import com.hsgaragepecas.garagehub.ui.theme.GarageDivider
import com.hsgaragepecas.garagehub.ui.theme.GarageGreyText
import com.hsgaragepecas.garagehub.ui.theme.GarageHubTheme
import com.hsgaragepecas.garagehub.ui.theme.GarageYellow
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * A screen that allows the user to create a new estimate.
 *
 * @param onBack A lambda to be called when the user wants to navigate back.
 * @param viewModel The ViewModel that manages the screen state.
 */
@Composable
fun CreateEstimateScreen(
    onBack: () -> Unit,
    viewModel: CreateEstimateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is CreateEstimateUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                CreateEstimateUiEvent.NavigateBack -> {
                    onBack()
                }
                is CreateEstimateUiEvent.OpenUri -> {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(event.uri, "application/pdf")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(Intent.createChooser(intent, "Abrir PDF"))
                }
            }
        }
    }

    CreateEstimateContent(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateEstimateContent(
    uiState: CreateEstimateUiState,
    onIntent: (CreateEstimateUiIntent) -> Unit,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let { onIntent(CreateEstimateUiIntent.OnAddVehiclePhotos(listOf(it))) }
        }
    )

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                tempImageUri?.let { onIntent(CreateEstimateUiIntent.OnAddVehiclePhotos(listOf(it))) }
            }
        }
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                try {
                    val uri = createTempImageUri(context)
                    tempImageUri = uri
                    takePictureLauncher.launch(uri)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.new_estimate_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // MO Hour Values
            Row(modifier = Modifier.fillMaxWidth()) {
                EstimateInputField(
                    label = stringResource(R.string.mo_hour_value_label),
                    value = uiState.moHourValue,
                    onValueChange = { onIntent(CreateEstimateUiIntent.OnMoHourValueChange(it)) },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.width(16.dp))
                EstimateInputField(
                    label = stringResource(R.string.painting_hour_value_label),
                    value = uiState.paintingHourValue,
                    onValueChange = { onIntent(CreateEstimateUiIntent.OnPaintingHourValueChange(it)) },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Customer Data Section
            EstimateSectionHeader(title = stringResource(R.string.customer_data_section))

            EstimateInputField(
                label = stringResource(R.string.customer_name_label),
                value = uiState.clientName,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnClientNameChange(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                EstimateInputField(
                    label = stringResource(R.string.customer_tel_label),
                    value = uiState.clientTel,
                    onValueChange = { onIntent(CreateEstimateUiIntent.OnClientTelChange(it)) },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                Spacer(modifier = Modifier.width(16.dp))
                EstimateInputField(
                    label = stringResource(R.string.customer_whatsapp_label),
                    value = uiState.clientWhats,
                    onValueChange = { onIntent(CreateEstimateUiIntent.OnClientWhatsChange(it)) },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            EstimateInputField(
                label = stringResource(R.string.customer_cep_label),
                value = uiState.clientCep,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnClientCepChange(it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(12.dp))
            EstimateInputField(
                label = stringResource(R.string.customer_address_label),
                value = uiState.clientAddress,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnClientAddressChange(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                EstimateInputField(
                    label = stringResource(R.string.customer_number_label),
                    value = uiState.clientNumber,
                    onValueChange = { onIntent(CreateEstimateUiIntent.OnClientNumberChange(it)) },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.width(16.dp))
                EstimateInputField(
                    label = stringResource(R.string.customer_neighborhood_label),
                    value = uiState.clientNeighborhood,
                    onValueChange = { onIntent(CreateEstimateUiIntent.OnClientNeighborhoodChange(it)) },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                EstimateInputField(
                    label = stringResource(R.string.customer_city_label),
                    value = uiState.clientCity,
                    onValueChange = { onIntent(CreateEstimateUiIntent.OnClientCityChange(it)) },
                    modifier = Modifier.weight(2f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                EstimateInputField(
                    label = stringResource(R.string.customer_uf_label),
                    value = uiState.clientUf,
                    onValueChange = { onIntent(CreateEstimateUiIntent.OnClientUfChange(it)) },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            EstimateInputField(
                label = stringResource(R.string.customer_complement_label),
                value = uiState.clientComplement,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnClientComplementChange(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Vehicle Data Section
            EstimateSectionHeader(title = stringResource(R.string.vehicle_data_section))

            EstimateInputField(
                label = stringResource(R.string.vehicle_plate_label),
                value = uiState.vehiclePlate,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnVehiclePlateChange(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                EstimateDropdownField(
                    label = stringResource(R.string.vehicle_brand_label),
                    options = uiState.brands,
                    selectedOption = uiState.vehicleBrand,
                    onOptionSelected = { onIntent(CreateEstimateUiIntent.OnBrandSelected(it)) },
                    optionLabel = { it.name },
                    modifier = Modifier.weight(1f),
                    placeholder = "Selecione a marca"
                )
                Spacer(modifier = Modifier.width(16.dp))
                EstimateDropdownField(
                    label = stringResource(R.string.vehicle_model_label),
                    options = uiState.models,
                    selectedOption = uiState.vehicleModel,
                    onOptionSelected = { onIntent(CreateEstimateUiIntent.OnModelSelected(it)) },
                    optionLabel = { it.name },
                    modifier = Modifier.weight(1f),
                    placeholder = "Selecione o modelo"
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                EstimateDropdownField(
                    label = stringResource(R.string.vehicle_manufacturing_year_label),
                    options = uiState.years,
                    selectedOption = uiState.selectedYear?.name ?: uiState.vehicleYearFab,
                    onOptionSelected = { onIntent(CreateEstimateUiIntent.OnYearSelected(it)) },
                    optionLabel = { it.name },
                    modifier = Modifier.weight(1f),
                    placeholder = "Selecione o ano"
                )
                Spacer(modifier = Modifier.width(16.dp))
                EstimateInputField(
                    label = stringResource(R.string.vehicle_model_year_label),
                    value = uiState.vehicleYearMod,
                    onValueChange = { onIntent(CreateEstimateUiIntent.OnVehicleYearModChange(it)) },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            EstimateInputField(
                label = stringResource(R.string.vehicle_chassis_label),
                value = uiState.vehicleChassis,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnVehicleChassisChange(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                EstimateInputField(
                    label = stringResource(R.string.vehicle_fuel_label),
                    value = uiState.vehicleFuel,
                    onValueChange = { onIntent(CreateEstimateUiIntent.OnVehicleFuelChange(it)) },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                EstimateInputField(
                    label = stringResource(R.string.vehicle_air_conditioning_label),
                    value = uiState.vehicleAir,
                    onValueChange = { onIntent(CreateEstimateUiIntent.OnVehicleAirChange(it)) },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                EstimateInputField(
                    label = stringResource(R.string.vehicle_steering_label),
                    value = uiState.vehicleSteering,
                    onValueChange = { onIntent(CreateEstimateUiIntent.OnVehicleSteeringChange(it)) },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                EstimateInputField(
                    label = stringResource(R.string.vehicle_transmission_label),
                    value = uiState.vehicleTransmission,
                    onValueChange = { onIntent(CreateEstimateUiIntent.OnVehicleTransmissionChange(it)) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.vehicle_photos_label),
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, GarageDivider)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(2.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(24.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.choose_files_button),
                                color = Color.Black,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (uiState.vehiclePhotos.isEmpty()) stringResource(R.string.no_file_chosen)
                            else "${uiState.vehiclePhotos.size} files chosen",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            val permissionCheckResult = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            )
                            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                try {
                                    val uri = createTempImageUri(context)
                                    tempImageUri = uri
                                    takePictureLauncher.launch(uri)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            } else {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                    color = GarageDivider,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = "Add Photo",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            if (uiState.vehiclePhotos.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(uiState.vehiclePhotos) { uri ->
                        Box {
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
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Items / Services Section
            EstimateSectionHeader(title = stringResource(R.string.items_services_section))

            EstimateInputField(
                label = stringResource(R.string.item_genuine_code_label),
                value = uiState.itemGenuineCode,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnItemGenuineCodeChange(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            EstimateInputField(
                label = stringResource(R.string.item_part_label),
                value = uiState.itemPartName,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnItemPartNameChange(it)) },
                placeholder = stringResource(R.string.item_part_placeholder)
            )
            Spacer(modifier = Modifier.height(12.dp))

            ItemCheckBoxWithInput(
                label = stringResource(R.string.item_t_h_label),
                checked = uiState.itemTH,
                onCheckedChange = { onIntent(CreateEstimateUiIntent.OnItemTHChange(it)) },
                value = uiState.itemTHValue,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnItemTHValueChange(it)) }
            )
            ItemCheckBoxWithInput(
                label = stringResource(R.string.item_ri_h_label),
                checked = uiState.itemRiH,
                onCheckedChange = { onIntent(CreateEstimateUiIntent.OnItemRiHChange(it)) },
                value = uiState.itemRiHValue,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnItemRiHValueChange(it)) }
            )
            ItemCheckBoxWithInput(
                label = stringResource(R.string.item_r_h_label),
                checked = uiState.itemRH,
                onCheckedChange = { onIntent(CreateEstimateUiIntent.OnItemRHChange(it)) },
                value = uiState.itemRHValue,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnItemRHValueChange(it)) }
            )
            ItemCheckBoxWithInput(
                label = stringResource(R.string.item_p_h_label),
                checked = uiState.itemPH,
                onCheckedChange = { onIntent(CreateEstimateUiIntent.OnItemPHChange(it)) },
                value = uiState.itemPHValue,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnItemPHValueChange(it)) }
            )

            Spacer(modifier = Modifier.height(12.dp))
            EstimateInputField(
                label = stringResource(R.string.item_part_price_label),
                value = uiState.itemPartPrice,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnItemPartPriceChange(it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(12.dp))
            EstimateInputField(
                label = stringResource(R.string.item_total_label),
                value = uiState.itemTotal,
                onValueChange = {},
                readOnly = true
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onIntent(CreateEstimateUiIntent.AddItem) },
                colors = ButtonDefaults.buttonColors(containerColor = GarageYellow),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = stringResource(R.string.add_button),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Final Buttons
            Button(
                onClick = { onIntent(CreateEstimateUiIntent.SaveEstimate) },
                colors = ButtonDefaults.buttonColors(containerColor = GarageYellow),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = stringResource(R.string.save_estimate_button),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ActionButton(
                    text = stringResource(R.string.generate_pdf_button),
                    modifier = Modifier.weight(1f),
                    onClick = { onIntent(CreateEstimateUiIntent.GeneratePdf) }
                )
                ActionButton(
                    text = stringResource(R.string.send_whatsapp_button),
                    modifier = Modifier.weight(1.3f),
                    onClick = { /* Handle WhatsApp */ }
                )
                ActionButton(
                    text = stringResource(R.string.demand_button),
                    modifier = Modifier.weight(1f),
                    containerColor = Color(0xFF0D6EFD),
                    onClick = { /* Handle Demand */ }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

private fun createTempImageUri(context: Context): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = File(context.cacheDir, "images").apply {
        if (!exists()) {
            mkdirs()
        }
    }
    val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
    )
}

/**
 * A composable that displays a section header for the estimate screen.
 *
 * @param title The title to be displayed in the header.
 */
@Composable
private fun EstimateSectionHeader(title: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * A composable that displays an input field for the estimate screen.
 *
 * @param label The label to be displayed above the input field.
 * @param value The value of the input field.
 * @param onValueChange A lambda to be called when the value of the input field changes.
 * @param modifier The modifier to be applied to the input field.
 * @param placeholder The placeholder to be displayed in the input field.
 * @param keyboardOptions The keyboard options to be applied to the input field.
 * @param readOnly Whether the input field is read-only.
 */
@Composable
private fun EstimateInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    readOnly: Boolean = false
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = placeholder?.let { { Text(text = it, color = GarageGreyText) } },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = GarageDivider,
                unfocusedBorderColor = GarageDivider
            ),
            singleLine = true,
            keyboardOptions = keyboardOptions,
            readOnly = readOnly
        )
    }
}

/**
 * A composable that displays a dropdown field for the estimate screen.
 *
 * @param label The label to be displayed above the dropdown field.
 * @param options The list of options to be displayed in the dropdown.
 * @param selectedOption The currently selected option.
 * @param onOptionSelected A lambda to be called when an option is selected.
 * @param optionLabel A lambda to be called to get the label for an option.
 * @param modifier The modifier to be applied to the dropdown field.
 * @param placeholder The placeholder to be displayed in the dropdown field.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> EstimateDropdownField(
    label: String,
    options: List<T>,
    selectedOption: String,
    onOptionSelected: (T) -> Unit,
    optionLabel: (T) -> String,
    modifier: Modifier = Modifier,
    placeholder: String = ""
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                placeholder = if (placeholder.isNotEmpty()) {
                    { Text(text = placeholder, color = GarageGreyText) }
                } else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = GarageDivider,
                    unfocusedBorderColor = GarageDivider
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

/**
 * A composable that displays a checkbox with an input field for the estimate screen.
 *
 * @param label The label to be displayed next to the checkbox.
 * @param checked Whether the checkbox is checked.
 * @param onCheckedChange A lambda to be called when the checked state changes.
 * @param value The value of the input field.
 * @param onValueChange A lambda to be called when the value of the input field changes.
 */
@Composable
private fun ItemCheckBoxWithInput(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.onBackground,
                    uncheckedColor = MaterialTheme.colorScheme.onBackground,
                    checkmarkColor = MaterialTheme.colorScheme.background
                )
            )
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = GarageDivider,
                unfocusedBorderColor = GarageDivider
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

/**
 * A composable that displays an action button for the estimate screen.
 *
 * @param text The text to be displayed on the button.
 * @param modifier The modifier to be applied to the button.
 * @param containerColor The container color of the button.
 * @param onClick A lambda to be called when the button is clicked.
 */
@Composable
private fun ActionButton(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = Color(0xFF212529),
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.height(40.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Text(text = text, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateEstimateScreenPreview() {
    GarageHubTheme(darkTheme = false) {
        CreateEstimateContent(
            uiState = CreateEstimateUiState(),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateEstimateScreenDarkPreview() {
    GarageHubTheme(darkTheme = true) {
        CreateEstimateContent(
            uiState = CreateEstimateUiState(),
            onIntent = {}
        )
    }
}
