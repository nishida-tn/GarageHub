package com.hsgaragepecas.garagehub.ui.account.forgot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hsgaragepecas.garagehub.R
import com.hsgaragepecas.garagehub.ui.theme.GarageCardBackground
import com.hsgaragepecas.garagehub.ui.theme.GarageDarkBackground
import com.hsgaragepecas.garagehub.ui.theme.GarageDivider
import com.hsgaragepecas.garagehub.ui.theme.GarageGreyText
import com.hsgaragepecas.garagehub.ui.theme.GarageHubTheme
import com.hsgaragepecas.garagehub.ui.theme.GarageYellow
import kotlinx.coroutines.flow.collectLatest

/**
 * A screen that allows the user to reset their password.
 *
 * @param viewModel The view model for the screen.
 * @param onBackToLoginClick A lambda to be called when the back to login button is clicked.
 */
@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel,
    onBackToLoginClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ForgotPasswordContract.Effect.NavigateBackToLogin -> {
                    onBackToLoginClick()
                }
                is ForgotPasswordContract.Effect.ShowSuccessMessage -> {
                    // Show a success message (e.g., Toast or Snackbar)
                    // For simplicity, we can also navigate back or show a success state in UI
                }
            }
        }
    }

    ForgotPasswordContent(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        onBackToLoginClick = onBackToLoginClick
    )
}

/**
 * The content of the forgot password screen.
 *
 * @param uiState The current UI state.
 * @param onIntent A lambda to be called when an intent is triggered.
 * @param onBackToLoginClick A lambda to be called when the back to login button is clicked.
 */
@Composable
private fun ForgotPasswordContent(
    uiState: ForgotPasswordContract.State,
    onIntent: (ForgotPasswordContract.Intent) -> Unit,
    onBackToLoginClick: () -> Unit
) {
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
            color = GarageCardBackground
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.forgot_password_title),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.forgot_password_subtitle),
                    style = TextStyle(
                        color = GarageGreyText,
                        fontSize = 16.sp
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.email_label),
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = { onIntent(ForgotPasswordContract.Intent.EmailChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = stringResource(R.string.forgot_password_email_placeholder),
                                color = GarageGreyText
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = GarageDivider,
                            unfocusedBorderColor = GarageDivider
                        ),
                        singleLine = true,
                        enabled = !uiState.isLoading
                    )
                }

                if (uiState.error != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.error,
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }

                if (uiState.isSuccess) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Link de recuperação enviado com sucesso!",
                        color = GarageYellow,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onIntent(ForgotPasswordContract.Intent.SendLinkClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GarageYellow,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp),
                    enabled = !uiState.isLoading && !uiState.isSuccess
                ) {
                    Text(
                        text = if (uiState.isLoading) "Enviando..." else stringResource(R.string.forgot_password_send_button),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = GarageDivider
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onBackToLoginClick,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(R.string.forgot_password_back_to_login),
                        style = TextStyle(
                            color = GarageGreyText,
                            fontSize = 16.sp
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ForgotPasswordScreenPreview() {
    GarageHubTheme(darkTheme = true) {
        ForgotPasswordContent(
            uiState = ForgotPasswordContract.State(),
            onIntent = {},
            onBackToLoginClick = {}
        )
    }
}
