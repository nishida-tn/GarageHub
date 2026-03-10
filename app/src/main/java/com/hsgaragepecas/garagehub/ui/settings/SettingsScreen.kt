package com.hsgaragepecas.garagehub.ui.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hsgaragepecas.garagehub.R
import com.hsgaragepecas.garagehub.ui.theme.GarageDarkBackground
import com.hsgaragepecas.garagehub.ui.theme.GarageDivider
import com.hsgaragepecas.garagehub.ui.theme.GarageGreyText
import com.hsgaragepecas.garagehub.ui.theme.GarageHubTheme
import com.hsgaragepecas.garagehub.ui.theme.GarageYellow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * A screen that allows the user to change the application settings.
 *
 * @param modifier The modifier to be applied to the screen.
 * @param viewModel The view model that manages the state of the screen.
 */
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.receiveAsFlow().collect { event ->
            when (event) {
                is SettingsUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GarageDarkBackground)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.settings_title),
                style = TextStyle(color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            )
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(containerColor = GarageYellow)
            ) {
                Text(text = stringResource(R.string.back_button), color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        WorkshopDataSection(uiState = uiState, viewModel = viewModel)
        Spacer(modifier = Modifier.height(24.dp))
        SecuritySection(uiState = uiState, viewModel = viewModel)
        Spacer(modifier = Modifier.height(24.dp))
        LaborSection(uiState = uiState, viewModel = viewModel)
    }
}

/**
 * A composable that displays the workshop data section of the settings screen.
 *
 * @param uiState The state of the settings screen.
 * @param viewModel The view model that manages the state of the screen.
 */
@Composable
private fun WorkshopDataSection(
    uiState: SettingsUiState,
    viewModel: SettingsViewModel
) {
    SettingsSectionCard(title = stringResource(R.string.workshop_data_title)) {
        SettingsInputField(label = stringResource(R.string.fantasy_name_label), value = uiState.fantasyName, onValueChange = viewModel::onFantasyNameChange)
        Spacer(modifier = Modifier.height(16.dp))
        SettingsInputField(
            label = stringResource(R.string.login_email_label),
            value = uiState.email,
            enabled = false
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SettingsInputField(label = stringResource(R.string.cnpj_label), value = uiState.cnpj, onValueChange = viewModel::onCnpjChange, modifier = Modifier.weight(1f))
            SettingsInputField(label = stringResource(R.string.landline_label), value = uiState.landline, onValueChange = viewModel::onLandlineChange, modifier = Modifier.weight(1f))
            SettingsInputField(label = stringResource(R.string.whatsapp_label), value = uiState.whatsapp, onValueChange = viewModel::onWhatsappChange, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SettingsInputField(label = stringResource(R.string.customer_cep_label), value = uiState.cep, onValueChange = viewModel::onCepChange, modifier = Modifier.weight(0.7f))
            SettingsInputField(label = stringResource(R.string.address_label), value = uiState.address, onValueChange = viewModel::onAddressChange, modifier = Modifier.weight(1.3f))
            SettingsInputField(label = stringResource(R.string.customer_number_label), value = uiState.number, onValueChange = viewModel::onNumberChange, modifier = Modifier.weight(0.5f))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SettingsInputField(label = stringResource(R.string.customer_neighborhood_label), value = uiState.neighborhood, onValueChange = viewModel::onNeighborhoodChange, modifier = Modifier.weight(1f))
            SettingsInputField(label = stringResource(R.string.customer_city_label), value = uiState.city, onValueChange = viewModel::onCityChange, modifier = Modifier.weight(1f))
            SettingsInputField(label = stringResource(R.string.customer_uf_label), value = uiState.uf, onValueChange = viewModel::onUfChange, modifier = Modifier.weight(0.4f))
        }
        Spacer(modifier = Modifier.height(16.dp))
        SettingsInputField(label = stringResource(R.string.complement_optional_label), value = uiState.complement, onValueChange = viewModel::onComplementChange)
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.logo_label),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier.size(80.dp).padding(top = 8.dp).background(GarageDivider, RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Logo", color = GarageGreyText)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1.5f)) {
                Button(
                    onClick = { /*TODO: Handle logo selection*/ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.change_logo_button))
                }
                Text(
                    text = stringResource(R.string.logo_description),
                    color = GarageGreyText,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = viewModel::saveWorkshopData,
            colors = ButtonDefaults.buttonColors(containerColor = GarageYellow),
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text(text = stringResource(R.string.save_data_button), color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

/**
 * A composable that displays the security section of the settings screen.
 *
 * @param uiState The state of the settings screen.
 * @param viewModel The view model that manages the state of the screen.
 */
@Composable
private fun SecuritySection(
    uiState: SettingsUiState,
    viewModel: SettingsViewModel
) {
    SettingsSectionCard(title = stringResource(R.string.security_title)) {
        SettingsInputField(
            label = stringResource(R.string.current_password_label),
            value = uiState.currentPassword,
            onValueChange = viewModel::onCurrentPasswordChange,
            placeholder = stringResource(R.string.current_password_placeholder),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SettingsInputField(
                label = stringResource(R.string.new_password_label),
                value = uiState.newPassword,
                onValueChange = viewModel::onNewPasswordChange,
                placeholder = stringResource(R.string.new_password_placeholder),
                modifier = Modifier.weight(1f),
                visualTransformation = PasswordVisualTransformation()
            )
            SettingsInputField(
                label = stringResource(R.string.confirm_label),
                value = uiState.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                placeholder = stringResource(R.string.confirm_password_placeholder_settings),
                modifier = Modifier.weight(1f),
                visualTransformation = PasswordVisualTransformation()
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = viewModel::changePassword,
            colors = ButtonDefaults.buttonColors(containerColor = GarageYellow),
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text(text = stringResource(R.string.change_password_button), color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

/**
 * A composable that displays the labor section of the settings screen.
 *
 * @param uiState The state of the settings screen.
 * @param viewModel The view model that manages the state of the screen.
 */
@Composable
private fun LaborSection(
    uiState: SettingsUiState,
    viewModel: SettingsViewModel
) {
    SettingsSectionCard(title = stringResource(R.string.labor_title)) {
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SettingsInputField(label = stringResource(R.string.mechanics_hour_label), value = uiState.mechanicsRate, onValueChange = viewModel::onMechanicsRateChange, modifier = Modifier.weight(1f))
            SettingsInputField(label = stringResource(R.string.painting_hour_label), value = uiState.paintingRate, onValueChange = viewModel::onPaintingRateChange, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = viewModel::saveHourlyRates,
            colors = ButtonDefaults.buttonColors(containerColor = GarageYellow),
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text(text = stringResource(R.string.save_values_button), color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

/**
 * A composable that displays a card for a section of the settings screen.
 *
 * @param title The title of the section.
 * @param content The content of the section.
 */
@Composable
private fun SettingsSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(1.dp, GarageDivider),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = TextStyle(color = GarageYellow, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

/**
 * A composable that displays an input field for the settings screen.
 *
 * @param label The label to be displayed above the input field.
 * @param value The value of the input field.
 * @param modifier The modifier to be applied to the input field.
 * @param onValueChange A lambda to be called when the value of the input field changes.
 * @param placeholder The placeholder to be displayed in the input field.
 * @param enabled Whether the input field is enabled.
 * @param visualTransformation The visual transformation to be applied to the input field.
 */
@Composable
private fun SettingsInputField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    placeholder: String? = null,
    enabled: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            placeholder = placeholder?.let { { Text(text = it, color = GarageGreyText) } },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = if (enabled) Color.White else GarageGreyText,
                disabledTextColor = GarageGreyText,
                focusedContainerColor = if (enabled) Color.Black else GarageDivider,
                unfocusedContainerColor = if (enabled) Color.Black else GarageDivider,
                disabledContainerColor = GarageDivider.copy(alpha = 0.5f),
                focusedBorderColor = GarageDivider,
                unfocusedBorderColor = GarageDivider,
                disabledBorderColor = GarageDivider.copy(alpha = 0.5f),
            ),
            singleLine = true,
            visualTransformation = visualTransformation
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    val mockViewModel = object : SettingsViewModel {
        override val uiState = MutableStateFlow(SettingsUiState(
            fantasyName = "Hs Garage",
            cnpj = "45328696000118",
            landline = "15981410980",
            whatsapp = "15981410980",
            cep = "18078340",
            address = "Rua Pedro Ruiz",
            number = "120",
            neighborhood = "Parque Vitória Régia",
            city = "Sorocaba",
            uf = "SP",
            complement = "Sala, bloco, referência",
            mechanicsRate = "100,00",
            paintingRate = "120,00"
        ))
        override val uiEvent = Channel<SettingsUiEvent>(Channel.BUFFERED)
        override fun onFantasyNameChange(name: String) {}
        override fun onCnpjChange(cnpj: String) {}
        override fun onLandlineChange(landline: String) {}
        override fun onWhatsappChange(whatsapp: String) {}
        override fun onCepChange(cep: String) {}
        override fun onAddressChange(address: String) {}
        override fun onNumberChange(number: String) {}
        override fun onNeighborhoodChange(neighborhood: String) {}
        override fun onCityChange(city: String) {}
        override fun onUfChange(uf: String) {}
        override fun onComplementChange(complement: String) {}
        override fun onLogoPathChange(path: String) {}
        override fun onCurrentPasswordChange(password: String) {}
        override fun onNewPasswordChange(password: String) {}
        override fun onConfirmPasswordChange(password: String) {}
        override fun onMechanicsRateChange(rate: String) {}
        override fun onPaintingRateChange(rate: String) {}
        override fun saveWorkshopData() {}
        override fun changePassword() {}
        override fun saveHourlyRates() {}
    }
    GarageHubTheme(darkTheme = true) {
        SettingsScreen(viewModel = mockViewModel)
    }
}
