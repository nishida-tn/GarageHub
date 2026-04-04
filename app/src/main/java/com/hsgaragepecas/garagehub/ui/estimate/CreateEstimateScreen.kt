package com.hsgaragepecas.garagehub.ui.estimate

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.hsgaragepecas.garagehub.R
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
 * @param viewModel The ViewModel that manages the screen state.
 */
@Composable
fun CreateEstimateScreen(
    viewModel: CreateEstimateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    CreateEstimateContent(
        uiState = uiState,
        onIntent = viewModel::onIntent
    )
}

@Composable
private fun CreateEstimateContent(
    uiState: CreateEstimateUiState,
    onIntent: (CreateEstimateUiIntent) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 10)
    ) { uris ->
        if (uris.isNotEmpty()) {
            onIntent(CreateEstimateUiIntent.OnAddVehiclePhotos(uris))
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            tempImageUri?.let { uri ->
                onIntent(CreateEstimateUiIntent.OnAddVehiclePhotos(listOf(uri)))
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // New Estimate Section
            Text(
                text = stringResource(R.string.new_estimate_title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EstimateInputField(
                    label = stringResource(R.string.mo_hour_value_label),
                    value = uiState.moHourValue,
                    onValueChange = { onIntent(CreateEstimateUiIntent.OnMoHourValueChange(it)) },
                    modifier = Modifier.weight(1f)
                )
                EstimateInputField(
                    label = stringResource(R.string.painting_hour_value_label),
                    value = uiState.paintingHourValue,
                    onValueChange = { onIntent(CreateEstimateUiIntent.OnPaintingHourValueChange(it)) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = GarageYellow),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.save_values_button),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
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
            EstimateInputField(
                label = stringResource(R.string.customer_tel_label),
                value = uiState.clientTel,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnClientTelChange(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            EstimateInputField(
                label = stringResource(R.string.customer_whatsapp_label),
                value = uiState.clientWhats,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnClientWhatsChange(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            EstimateInputField(
                label = stringResource(R.string.customer_cep_label),
                value = uiState.clientCep,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnClientCepChange(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            EstimateInputField(
                label = stringResource(R.string.customer_address_label),
                value = uiState.clientAddress,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnClientAddressChange(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            EstimateInputField(
                label = stringResource(R.string.customer_number_label),
                value = uiState.clientNumber,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnClientNumberChange(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            EstimateInputField(
                label = stringResource(R.string.customer_neighborhood_label),
                value = uiState.clientNeighborhood,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnClientNeighborhoodChange(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            EstimateInputField(
                label = stringResource(R.string.customer_city_label),
                value = uiState.clientCity,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnClientCityChange(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            EstimateInputField(
                label = stringResource(R.string.customer_uf_label),
                value = uiState.clientUf,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnClientUfChange(it)) }
            )
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
            
            // Placeholder for brand/model/years - in a real app these would come from an API
            val dummyBrands = listOf("Volkswagen", "Fiat", "Chevrolet", "Ford", "Toyota", "Honda", "Hyundai")
            val dummyModels = listOf("Gol", "Uno", "Onix", "Ka", "Corolla", "Civic", "HB20")
            val dummyYears = (2000..2025).map { it.toString() }.reversed()
            val fuelOptions = listOf("Gasolina", "Álcool", "Flex", "Diesel", "GNV", "Elétrico", "Híbrido")
            val airOptions = listOf("Sim", "Não")
            val steeringOptions = listOf("Hidráulica", "Elétrica", "Mecânica", "Eletro-hidráulica")
            val transmissionOptions = listOf("Manual", "Automático", "CVT", "Automatizado")

            EstimateDropdownField(
                label = stringResource(R.string.vehicle_brand_label),
                selectedOption = if (uiState.vehicleBrand.isEmpty()) stringResource(R.string.select_option) else uiState.vehicleBrand,
                options = dummyBrands,
                onOptionSelected = { onIntent(CreateEstimateUiIntent.OnVehicleBrandChange(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            EstimateDropdownField(
                label = stringResource(R.string.vehicle_model_label),
                selectedOption = if (uiState.vehicleModel.isEmpty()) stringResource(R.string.select_brand_option) else uiState.vehicleModel,
                options = dummyModels,
                onOptionSelected = { onIntent(CreateEstimateUiIntent.OnVehicleModelChange(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            EstimateDropdownField(
                label = stringResource(R.string.vehicle_manufacturing_year_label),
                selectedOption = if (uiState.vehicleYearFab.isEmpty()) stringResource(R.string.select_model_option) else uiState.vehicleYearFab,
                options = dummyYears,
                onOptionSelected = { onIntent(CreateEstimateUiIntent.OnVehicleYearFabChange(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            EstimateDropdownField(
                label = stringResource(R.string.vehicle_model_year_label),
                selectedOption = if (uiState.vehicleYearMod.isEmpty()) stringResource(R.string.select_model_option) else uiState.vehicleYearMod,
                options = dummyYears,
                onOptionSelected = { onIntent(CreateEstimateUiIntent.OnVehicleYearModChange(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            EstimateInputField(
                label = stringResource(R.string.vehicle_chassis_label),
                value = uiState.vehicleChassis,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnVehicleChassisChange(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            EstimateDropdownField(
                label = stringResource(R.string.vehicle_fuel_label),
                selectedOption = if (uiState.vehicleFuel.isEmpty()) stringResource(R.string.select_option) else uiState.vehicleFuel,
                options = fuelOptions,
                onOptionSelected = { onIntent(CreateEstimateUiIntent.OnVehicleFuelChange(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            EstimateDropdownField(
                label = stringResource(R.string.vehicle_air_conditioning_label),
                selectedOption = if (uiState.vehicleAir.isEmpty()) stringResource(R.string.select_option) else uiState.vehicleAir,
                options = airOptions,
                onOptionSelected = { onIntent(CreateEstimateUiIntent.OnVehicleAirChange(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            EstimateDropdownField(
                label = stringResource(R.string.vehicle_steering_label),
                selectedOption = if (uiState.vehicleSteering.isEmpty()) stringResource(R.string.select_option) else uiState.vehicleSteering,
                options = steeringOptions,
                onOptionSelected = { onIntent(CreateEstimateUiIntent.OnVehicleSteeringChange(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            EstimateDropdownField(
                label = stringResource(R.string.vehicle_transmission_label),
                selectedOption = if (uiState.vehicleTransmission.isEmpty()) stringResource(R.string.select_option) else uiState.vehicleTransmission,
                options = transmissionOptions,
                onOptionSelected = { onIntent(CreateEstimateUiIntent.OnVehicleTransmissionChange(it)) }
            )

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
                onCheckedChange = { onIntent(CreateEstimateUiIntent.OnItemTHChange(it)) }
            )
            ItemCheckBoxWithInput(
                label = stringResource(R.string.item_ri_h_label),
                checked = uiState.itemRiH,
                onCheckedChange = { onIntent(CreateEstimateUiIntent.OnItemRiHChange(it)) }
            )
            ItemCheckBoxWithInput(
                label = stringResource(R.string.item_r_h_label),
                checked = uiState.itemRH,
                onCheckedChange = { onIntent(CreateEstimateUiIntent.OnItemRHChange(it)) }
            )
            ItemCheckBoxWithInput(
                label = stringResource(R.string.item_p_h_label),
                checked = uiState.itemPH,
                onCheckedChange = { onIntent(CreateEstimateUiIntent.OnItemPHChange(it)) }
            )

            Spacer(modifier = Modifier.height(12.dp))
            EstimateInputField(
                label = stringResource(R.string.item_part_price_label),
                value = uiState.itemPartPrice,
                onValueChange = { onIntent(CreateEstimateUiIntent.OnItemPartPriceChange(it)) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            EstimateInputField(
                label = stringResource(R.string.item_total_label),
                value = "", // This should be calculated
                onValueChange = {})

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { },
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
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = stringResource(R.string.send_whatsapp_button),
                    modifier = Modifier.weight(1.3f)
                )
                ActionButton(
                    text = stringResource(R.string.demand_button),
                    modifier = Modifier.weight(1f),
                    containerColor = Color(0xFF0D6EFD)
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
 */
@Composable
private fun EstimateInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null
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
            singleLine = true
        )
    }
}

/**
 * A composable that displays a dropdown field for the estimate screen.
 *
 * @param label The label to be displayed above the dropdown field.
 * @param selectedOption The currently selected option in the dropdown field.
 * @param options The list of options to be displayed in the dropdown menu.
 * @param onOptionSelected A lambda to be called when an option is selected.
 * @param modifier The modifier to be applied to the dropdown field.
 */
@Composable
private fun EstimateDropdownField(
    label: String,
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
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
        Box {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, GarageDivider)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = selectedOption, color = MaterialTheme.colorScheme.onSurface)
                    Text(text = "▼", color = MaterialTheme.colorScheme.onSurface, fontSize = 10.sp)
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
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
 */
@Composable
private fun ItemCheckBoxWithInput(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
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
            value = "", // This would also need to be in the state if you want to track hours per action
            onValueChange = {},
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
            singleLine = true
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
 */
@Composable
private fun ActionButton(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = Color(0xFF212529)
) {
    Button(
        onClick = { },
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
