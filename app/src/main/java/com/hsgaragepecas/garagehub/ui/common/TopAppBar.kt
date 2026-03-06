package com.hsgaragepecas.garagehub.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hsgaragepecas.garagehub.R
import com.hsgaragepecas.garagehub.ui.theme.GarageYellow

/**
 * A custom top app bar for the GarageHub application.
 *
 * @param onMenuClick A lambda to be called when the menu button is clicked.
 * @param modifier The modifier to be applied to the top app bar.
 */
@Composable
fun GarageTopAppBar(
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onMenuClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = GarageYellow.copy(alpha = 0.1f),
                contentColor = GarageYellow
            )
        ) {
            Text(text = "≡ " + stringResource(R.string.menu_button))
        }

        Text(
            text = stringResource(R.string.portal_oficina),
            style = TextStyle(color = GarageYellow, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )
    }
}
