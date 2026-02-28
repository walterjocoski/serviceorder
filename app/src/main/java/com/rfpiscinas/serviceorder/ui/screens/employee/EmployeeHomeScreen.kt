package com.rfpiscinas.serviceorder.ui.screens.employee

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rfpiscinas.serviceorder.data.model.OrderStatus
import com.rfpiscinas.serviceorder.data.model.ServiceOrder
import com.rfpiscinas.serviceorder.data.model.User
import com.rfpiscinas.serviceorder.ui.theme.*
import com.rfpiscinas.serviceorder.ui.viewmodel.EmployeeHomeViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeHomeScreen(
    currentUser: User,
    onCreateOrder: () -> Unit,
    onOrderClick: (Long) -> Unit,
    onAddServices: (Long) -> Unit,
    viewModel: EmployeeHomeViewModel = hiltViewModel()
) {
    val orders by viewModel.orders.collectAsState()
    val actionMessage by viewModel.actionMessage.collectAsState()
    var orderToCancel by remember { mutableStateOf<ServiceOrder?>(null) }

    LaunchedEffect(currentUser.id) {
        viewModel.loadOrdersForEmployee(currentUser.id)
    }

    // Snackbar de feedback
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(actionMessage) {
        actionMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearActionMessage()
        }
    }

    // Separar por status
    val inProgressOrders = orders.filter { it.status == OrderStatus.IN_PROGRESS }
    val createdOrders = orders.filter { it.status == OrderStatus.CREATED }
    val completedOrders = orders.filter { it.status == OrderStatus.COMPLETED }
    val cancelledOrders = orders.filter { it.status == OrderStatus.CANCELLED }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Minhas OS", fontWeight = FontWeight.Bold)
                        Text(
                            text = "Olá, ${currentUser.name.split(" ").first()}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCreateOrder,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Nova OS") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhuma ordem de serviço encontrada",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Seção: Em Andamento
                if (inProgressOrders.isNotEmpty()) {
                    item { SectionHeader("Em Andamento", StatusInProgress) }
                    items(inProgressOrders) { order ->
                        OrderCard(
                            order = order,
                            onClick = { onOrderClick(order.id) },
                            onAddServices = { onAddServices(order.id) },
                            onCancel = { orderToCancel = order }
                        )
                    }
                }

                // Seção: Criadas (aguardando início)
                if (createdOrders.isNotEmpty()) {
                    item { SectionHeader("Aguardando Início", StatusCreated) }
                    items(createdOrders) { order ->
                        OrderCard(
                            order = order,
                            onClick = { onOrderClick(order.id) },
                            onStartOrder = { viewModel.startOrder(order.id) },
                            onCancel = { orderToCancel = order }
                        )
                    }
                }

                // Seção: Finalizadas
                if (completedOrders.isNotEmpty()) {
                    item { SectionHeader("Finalizadas", StatusCompleted) }
                    items(completedOrders) { order ->
                        OrderCard(
                            order = order,
                            onClick = { onOrderClick(order.id) }
                        )
                    }
                }

                // Seção: Canceladas
                if (cancelledOrders.isNotEmpty()) {
                    item { SectionHeader("Canceladas", StatusCancelled) }
                    items(cancelledOrders) { order ->
                        OrderCard(
                            order = order,
                            onClick = { onOrderClick(order.id) }
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }

    // Dialog de confirmação de cancelamento
    orderToCancel?.let { order ->
        AlertDialog(
            onDismissRequest = { orderToCancel = null },
            title = { Text("Cancelar OS #${order.id}?") },
            text = {
                Text(
                    if (order.status == OrderStatus.CREATED)
                        "A ordem ainda não foi iniciada. Deseja cancelá-la?"
                    else
                        "A ordem está em andamento mas não tem serviços lançados. Deseja cancelá-la?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.cancelOrder(order)
                        orderToCancel = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Cancelar OS")
                }
            },
            dismissButton = {
                TextButton(onClick = { orderToCancel = null }) {
                    Text("Voltar")
                }
            }
        )
    }
}

@Composable
fun SectionHeader(title: String, color: androidx.compose.ui.graphics.Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Surface(
            color = color.copy(alpha = 0.2f),
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelLarge,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Divider(modifier = Modifier.weight(1f), color = color.copy(alpha = 0.3f))
    }
}

@Composable
fun OrderCard(
    order: ServiceOrder,
    onClick: () -> Unit,
    onStartOrder: (() -> Unit)? = null,
    onAddServices: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null
) {
    val statusColor = when (order.status) {
        OrderStatus.CREATED -> StatusCreated
        OrderStatus.IN_PROGRESS -> StatusInProgress
        OrderStatus.COMPLETED -> StatusCompleted
        OrderStatus.CANCELLED -> StatusCancelled
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "OS #${order.id}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Surface(
                    color = statusColor.copy(alpha = 0.15f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = order.status.displayName,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = statusColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = order.clientName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = order.clientAddress,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Início",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = order.startDateTime,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                if (order.endDateTime != null) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Finalização",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = order.endDateTime,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            if (order.items.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${order.items.size} serviço(s) lançado(s)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            // Botões de ação conforme status
            val showActions = onStartOrder != null || onAddServices != null || onCancel != null
            if (showActions) {
                Spacer(modifier = Modifier.height(12.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Botão Iniciar (só para CREATED)
                    onStartOrder?.let {
                        FilledTonalButton(
                            onClick = it,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = StatusInProgress.copy(alpha = 0.15f),
                                contentColor = StatusInProgress
                            )
                        ) {
                            Icon(
                                Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("Iniciar", style = MaterialTheme.typography.labelMedium)
                        }
                    }

                    // Botão Lançar Serviços (só para IN_PROGRESS)
                    onAddServices?.let {
                        FilledTonalButton(
                            onClick = it,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("Serviços", style = MaterialTheme.typography.labelMedium)
                        }
                    }

                    // Botão Cancelar
                    onCancel?.let {
                        OutlinedButton(
                            onClick = it,
                            modifier = if (onStartOrder == null && onAddServices == null)
                                Modifier.weight(1f) else Modifier,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(
                                Icons.Default.Cancel,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("Cancelar", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }
    }
}
