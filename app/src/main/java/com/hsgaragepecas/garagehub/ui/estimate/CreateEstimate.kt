package com.hsgaragepecas.garagehub.ui.estimate

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hsgaragepecas.garagehub.R
import com.hsgaragepecas.garagehub.ui.common.GarageTopAppBar
import com.hsgaragepecas.garagehub.ui.theme.GarageDarkBackground
import com.hsgaragepecas.garagehub.ui.theme.GarageDivider
import com.hsgaragepecas.garagehub.ui.theme.GarageGreyText
import com.hsgaragepecas.garagehub.ui.theme.GarageHubTheme
import com.hsgaragepecas.garagehub.ui.theme.GarageYellow

/**
 * A screen that allows the user to create a new estimate.
 *
 * @param modifier The modifier to be applied to the screen.
 */
@Composable
fun CreateEstimateScreen(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GarageDarkBackground)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        GarageTopAppBar(onMenuClick = { /*TODO*/ })

        Spacer(modifier = Modifier.height(32.dp))

        // New Estimate Section
        Text(
            text = stringResource(R.string.new_estimate_title),
            style = TextStyle(
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            EstimateInputField(
                label = stringResource(R.string.mo_hour_value_label),
                value = "80,00",
                onValueChange = {},
                modifier = Modifier.weight(1f)
            )
            EstimateInputField(
                label = stringResource(R.string.painting_hour_value_label),
                value = "100,00",
                onValueChange = {},
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

        EstimateInputField(label = stringResource(R.string.customer_name_label), value = "", onValueChange = {})
        Spacer(modifier = Modifier.height(12.dp))
        EstimateInputField(label = stringResource(R.string.customer_tel_label), value = "", onValueChange = {})
        Spacer(modifier = Modifier.height(12.dp))
        EstimateInputField(label = stringResource(R.string.customer_whatsapp_label), value = "", onValueChange = {})
        Spacer(modifier = Modifier.height(12.dp))
        EstimateInputField(label = stringResource(R.string.customer_cep_label), value = "", onValueChange = {})
        Spacer(modifier = Modifier.height(12.dp))
        EstimateInputField(label = stringResource(R.string.customer_address_label), value = "", onValueChange = {})
        Spacer(modifier = Modifier.height(12.dp))
        EstimateInputField(label = stringResource(R.string.customer_number_label), value = "", onValueChange = {})
        Spacer(modifier = Modifier.height(12.dp))
        EstimateInputField(label = stringResource(R.string.customer_neighborhood_label), value = "", onValueChange = {})
        Spacer(modifier = Modifier.height(12.dp))
        EstimateInputField(label = stringResource(R.string.customer_city_label), value = "", onValueChange = {})
        Spacer(modifier = Modifier.height(12.dp))
        EstimateInputField(label = stringResource(R.string.customer_uf_label), value = "", onValueChange = {})
        Spacer(modifier = Modifier.height(12.dp))
        EstimateInputField(label = stringResource(R.string.customer_complement_label), value = "", onValueChange = {})

        Spacer(modifier = Modifier.height(24.dp))

        // Vehicle Data Section
        EstimateSectionHeader(title = stringResource(R.string.vehicle_data_section))

        EstimateInputField(label = stringResource(R.string.vehicle_plate_label), value = "", onValueChange = {})
        Spacer(modifier = Modifier.height(12.dp))
        EstimateDropdownField(label = stringResource(R.string.vehicle_brand_label), selectedOption = stringResource(R.string.select_option))
        Spacer(modifier = Modifier.height(12.dp))
        EstimateDropdownField(label = stringResource(R.string.vehicle_model_label), selectedOption = stringResource(R.string.select_brand_option))
        Spacer(modifier = Modifier.height(12.dp))
        EstimateDropdownField(label = stringResource(R.string.vehicle_manufacturing_year_label), selectedOption = stringResource(R.string.select_model_option))
        Spacer(modifier = Modifier.height(12.dp))
        EstimateDropdownField(label = stringResource(R.string.vehicle_model_year_label), selectedOption = stringResource(R.string.select_model_option))
        Spacer(modifier = Modifier.height(12.dp))
        EstimateInputField(label = stringResource(R.string.vehicle_chassis_label), value = "", onValueChange = {})
        Spacer(modifier = Modifier.height(12.dp))
        EstimateDropdownField(label = stringResource(R.string.vehicle_fuel_label), selectedOption = stringResource(R.string.select_option))
        Spacer(modifier = Modifier.height(12.dp))
        EstimateDropdownField(label = stringResource(R.string.vehicle_air_conditioning_label), selectedOption = stringResource(R.string.select_option))
        Spacer(modifier = Modifier.height(12.dp))
        EstimateDropdownField(label = stringResource(R.string.vehicle_steering_label), selectedOption = stringResource(R.string.select_option))
        Spacer(modifier = Modifier.height(12.dp))
        EstimateDropdownField(label = stringResource(R.string.vehicle_transmission_label), selectedOption = stringResource(R.string.select_option))

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.vehicle_photos_label),
            style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.weight(1f),
                color = Color.Black,
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, GarageDivider)
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(2.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.height(24.dp)
                    ) {
                        Text(text = stringResource(R.string.choose_files_button), color = Color.Black, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.no_file_chosen), color = Color.White, fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                modifier = Modifier.size(40.dp),
                color = GarageDivider,
                shape = RoundedCornerShape(4.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Box(modifier = Modifier.size(24.dp).background(Color.Gray)) // Placeholder for camera icon
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Items / Services Section
        EstimateSectionHeader(title = stringResource(R.string.items_services_section))

        EstimateInputField(label = stringResource(R.string.item_genuine_code_label), value = "", onValueChange = {})
        Spacer(modifier = Modifier.height(12.dp))
        EstimateInputField(
            label = stringResource(R.string.item_part_label),
            value = "",
            onValueChange = {},
            placeholder = stringResource(R.string.item_part_placeholder)
        )
        Spacer(modifier = Modifier.height(12.dp))

        ItemCheckBoxWithInput(label = stringResource(R.string.item_t_h_label))
        ItemCheckBoxWithInput(label = stringResource(R.string.item_ri_h_label))
        ItemCheckBoxWithInput(label = stringResource(R.string.item_r_h_label))
        ItemCheckBoxWithInput(label = stringResource(R.string.item_p_h_label))

        Spacer(modifier = Modifier.height(12.dp))
        EstimateInputField(label = stringResource(R.string.item_part_price_label), value = "0,00", onValueChange = {})
        Spacer(modifier = Modifier.height(12.dp))
        EstimateInputField(label = stringResource(R.string.item_total_label), value = "", onValueChange = {})

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = GarageYellow),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = stringResource(R.string.add_button), color = Color.Black, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Final Buttons
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = GarageYellow),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text(text = stringResource(R.string.save_estimate_button), color = Color.Black, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ActionButton(text = stringResource(R.string.generate_pdf_button), modifier = Modifier.weight(1f))
            ActionButton(text = stringResource(R.string.send_whatsapp_button), modifier = Modifier.weight(1.3f))
            ActionButton(
                text = stringResource(R.string.demand_button),
                modifier = Modifier.weight(1f),
                containerColor = Color(0xFF0D6EFD)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
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
            style = TextStyle(
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
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
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = placeholder?.let { { Text(text = it, color = GarageGreyText) } },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Black,
                unfocusedContainerColor = Color.Black,
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
 * @param modifier The modifier to be applied to the dropdown field.
 */
@Composable
private fun EstimateDropdownField(
    label: String,
    selectedOption: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Black,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, GarageDivider)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = selectedOption, color = Color.White)
                Text(text = "▼", color = Color.White, fontSize = 10.sp)
            }
        }
    }
}

/**
 * A composable that displays a checkbox with an input field for the estimate screen.
 *
 * @param label The label to be displayed next to the checkbox.
 */
@Composable
private fun ItemCheckBoxWithInput(label: String) {
    var checked by remember { mutableStateOf(false) }
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = checked,
                onCheckedChange = { checked = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.White,
                    uncheckedColor = Color.White,
                    checkmarkColor = Color.Black
                )
            )
            Text(text = label, color = Color.White, fontWeight = FontWeight.Bold)
        }
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth().padding(start = 12.dp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Black,
                unfocusedContainerColor = Color.Black,
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
    GarageHubTheme(darkTheme = true) {
        CreateEstimateScreen()
    }
}
