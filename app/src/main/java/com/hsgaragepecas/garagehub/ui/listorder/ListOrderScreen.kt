package com.hsgaragepecas.garagehub.ui.listorder

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hsgaragepecas.garagehub.R
import com.hsgaragepecas.garagehub.data.local.order.OrderEntity
import com.hsgaragepecas.garagehub.data.local.order.OrderStatus

/**
 * A screen that displays a list of orders.
 *
 * @param viewModel The view model that manages the state of the screen.
 * @param onNavigateToOrderDetails A lambda to be called when the user clicks on an order.
 */
@Composable
fun ListOrderScreen(
    viewModel: ListOrderViewModel = hiltViewModel(),
    onNavigateToOrderDetails: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadOrders()
    }

    ListOrderContent(
        uiState = uiState,
        onNavigateToOrderDetails = onNavigateToOrderDetails
    )
}

/**
 * The content of the list order screen.
 *
 * @param uiState The state of the screen.
 * @param onNavigateToOrderDetails A lambda to be called when the user clicks on an order.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOrderContent(
    uiState: ListOrderContract.ListOrderUiState,
    onNavigateToOrderDetails: (String) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Histórico de Pedidos") },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            FilterSection()
            Spacer(modifier = Modifier.height(16.dp))
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (uiState.error != null) {
                Text(
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.orders) { order ->
                        OrderCard(order = order, onNavigateToOrderDetails = onNavigateToOrderDetails)
                    }
                }
            }
        }
    }
}

/**
 * A section that allows the user to filter the list of orders.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        FilterChip(selected = true, onClick = { /* TODO */ }, label = { Text("Todos") })
        FilterChip(selected = false, onClick = { /* TODO */ }, label = { Text("Aberto") })
        FilterChip(selected = false, onClick = { /* TODO */ }, label = { Text("Concluído") })
        FilterChip(selected = false, onClick = { /* TODO */ }, label = { Text("Cancelado") })
    }
}

/**
 * A card that displays information about an order.
 *
 * @param order The order to be displayed.
 * @param onNavigateToOrderDetails A lambda to be called when the user clicks on the card.
 */
@Composable
fun OrderCard(
    order: OrderEntity,
    onNavigateToOrderDetails: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Car",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pedido #${order.orderNumber} - ${order.orderDate}",
                    style = MaterialTheme.typography.bodySmall
                )
                OrderStatusChip(status = order.status)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = order.carBrand,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(text = "Cliente: Nome do Cliente", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Itens: ${order.numberOfItems}")
                Text(
                    text = "R$ ${"%,.2f".format(order.price)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onNavigateToOrderDetails(order.orderNumber.toString()) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Detalhes")
            }
        }
    }
}

/**
 * A chip that displays the status of an order.
 *
 * @param status The status of the order.
 */
@Composable
fun OrderStatusChip(status: OrderStatus) {
    val (text, color) = when (status) {
        OrderStatus.ABERTO -> "Aberto" to Color.Yellow
        OrderStatus.CONCLUIDO -> "Concluído" to Color.Green
        OrderStatus.CANCELADO -> "Cancelado" to Color.Red
    }

    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )

}

@Preview(showBackground = true)
@Composable
fun ListOrderScreenPreview() {
    val orders = listOf(
        OrderEntity(1, 1678886400000, "Ford Maverick", OrderStatus.ABERTO, 2, 150.0),
        OrderEntity(2, 1678799999000, "Chevrolet Opala", OrderStatus.CONCLUIDO, 1, 200.0)
    )
    val uiState = ListOrderContract.ListOrderUiState(orders = orders)
    ListOrderContent(uiState = uiState, onNavigateToOrderDetails = {})
}
