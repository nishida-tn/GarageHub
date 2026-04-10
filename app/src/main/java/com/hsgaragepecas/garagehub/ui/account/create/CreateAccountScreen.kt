package com.hsgaragepecas.garagehub.ui.account.create

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hsgaragepecas.garagehub.R
import com.hsgaragepecas.garagehub.ui.theme.GarageCardBackground
import com.hsgaragepecas.garagehub.ui.theme.GarageDarkBackground
import com.hsgaragepecas.garagehub.ui.theme.GarageDivider
import com.hsgaragepecas.garagehub.ui.theme.GarageGreyText
import com.hsgaragepecas.garagehub.ui.theme.GarageHubTheme
import com.hsgaragepecas.garagehub.ui.theme.GarageYellow

/**
 * A screen that allows the user to create a new account.
 *
 * @param viewModel The ViewModel that manages the screen's state.
 * @param onNavigateBack A lambda to be called when navigating back.
 * @param onNavigateToMain A lambda to be called when navigating to the main screen.
 */
@Composable
fun CreateAccountScreen(
    viewModel: CreateAccountContract,
    onNavigateBack: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is CreateAccountContract.CreateAccountSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is CreateAccountContract.CreateAccountSideEffect.NavigateBack -> {
                    onNavigateBack()
                }
                is CreateAccountContract.CreateAccountSideEffect.NavigateToMain -> {
                    onNavigateToMain()
                }
            }
        }
    }

    CreateAccountContent(
        uiState = uiState,
        onCreateAccountClick = { name, email, whatsapp, password ->
            viewModel.setEvent(
                CreateAccountContract.CreateAccountUiEvent.OnCreateAccountClick(
                    name = name,
                    email = email,
                    whatsapp = whatsapp,
                    password = password
                )
            )
        },
        onBackToLoginClick = onNavigateBack
    )
}

/**
 * The content of the create account screen.
 *
 * @param uiState The UI state of the screen.
 * @param onCreateAccountClick A lambda to be called when the create account button is clicked.
 * @param onBackToLoginClick A lambda to be called when the back to login button is clicked.
 */
@Composable
private fun CreateAccountContent(
    uiState: CreateAccountContract.CreateAccountUiState,
    onCreateAccountClick: (name: String, email: String, whatsapp: String, password: String) -> Unit,
    onBackToLoginClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GarageDarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = GarageCardBackground,
            border = null
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(R.string.create_account_title),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.create_account_subtitle),
                    style = TextStyle(
                        color = GarageGreyText,
                        fontSize = 16.sp,
                        lineHeight = 22.sp
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                CreateAccountInputField(
                    label = stringResource(R.string.name_label),
                    value = name,
                    onValueChange = { name = it },
                    placeholder = stringResource(R.string.name_placeholder),
                    enabled = !uiState.isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                CreateAccountInputField(
                    label = stringResource(R.string.email_label),
                    value = email,
                    onValueChange = { email = it },
                    placeholder = stringResource(R.string.email_placeholder),
                    isRequired = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    enabled = !uiState.isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                CreateAccountInputField(
                    label = stringResource(R.string.whatsapp_label),
                    value = whatsapp,
                    onValueChange = { whatsapp = it },
                    placeholder = stringResource(R.string.whatsapp_placeholder),
                    isRequired = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    enabled = !uiState.isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                CreateAccountInputField(
                    label = stringResource(R.string.password_label),
                    value = password,
                    onValueChange = { password = it },
                    placeholder = stringResource(R.string.password_hint),
                    isRequired = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    enabled = !uiState.isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                CreateAccountInputField(
                    label = stringResource(R.string.confirm_password_label),
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = stringResource(R.string.confirm_password_placeholder),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    enabled = !uiState.isLoading
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (password == confirmPassword) {
                            onCreateAccountClick(name, email, whatsapp, password)
                        } else {
                            // Local validation for password confirmation
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GarageYellow,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.Black,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.create_account_button),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onBackToLoginClick,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    enabled = !uiState.isLoading
                ) {
                    Text(
                        text = stringResource(R.string.already_have_account),
                        style = TextStyle(
                            color = GarageGreyText,
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }
    }
}

/**
 * A composable that displays an input field for the create account screen.
 *
 * @param label The label to be displayed above the input field.
 * @param value The value of the input field.
 * @param onValueChange A lambda to be called when the value of the input field changes.
 * @param placeholder The placeholder to be displayed in the input field.
 * @param modifier The modifier to be applied to the input field.
 * @param isRequired Whether the input field is required.
 * @param visualTransformation The visual transformation to be applied to the input field.
 * @param keyboardOptions The keyboard options to be applied to the input field.
 */
@Composable
private fun CreateAccountInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
    enabled: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = buildAnnotatedString {
                append(label)
                if (isRequired) {
                    withStyle(style = SpanStyle(color = Color.Red)) {
                        append(" *")
                    }
                }
            },
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
            enabled = enabled,
            placeholder = {
                Text(
                    text = placeholder,
                    color = GarageGreyText
                )
            },
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedBorderColor = GarageDivider,
                unfocusedBorderColor = GarageDivider
            ),
            singleLine = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateAccountScreenPreview() {
    GarageHubTheme(darkTheme = true) {
        CreateAccountContent(
            uiState = CreateAccountContract.CreateAccountUiState(),
            onCreateAccountClick = { _, _, _, _ -> },
            onBackToLoginClick = {}
        )
    }
}
