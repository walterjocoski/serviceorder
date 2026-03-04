package com.rfpiscinas.serviceorder.ui.screens.employee

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rfpiscinas.serviceorder.R
import com.rfpiscinas.serviceorder.data.model.*
import com.rfpiscinas.serviceorder.ui.viewmodel.AddServicesViewModel
import androidx.hilt.navigation.compose.hiltViewModel

// ── Estados de UI ───────────────────────────────────────────────────────────

data class ServiceItemState(
    val service: Service,
    val observations: String = "",
    val products: List<ProductItemState> = emptyList()
)

data class ProductItemState(
    val product: Product,
    val quantity: String = ""
)

private fun ServiceOrderItem.toState() = ServiceItemState(
    service = service,
    observations = observations,
    products = products.map { ProductItemState(it.product, it.quantity.toString()) }
)

private fun ServiceItemState.toOrderItem() = ServiceOrderItem(
    service = service,
    observations = observations,
    products = products.mapNotNull { p ->
        val qty = p.quantity.toDoubleOrNull()?.takeIf { it > 0 } ?: return@mapNotNull null
        ServiceOrderProduct(product = p.product, quantity = qty)
    }
)

// ── AddServicesScreen ───────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddServicesScreen(
    orderId: Long,
    clientId: Long,        // mantido para compatibilidade de navegação (não usado)
    startDateTime: String, // mantido para compatibilidade de navegação (não usado)
    onNavigateBack: () -> Unit,
    onOrderFinished: () -> Unit,
    viewModel: AddServicesViewModel = hiltViewModel()
) {
    val client            by viewModel.client.collectAsState()
    val order             by viewModel.order.collectAsState()
    val orderLoaded       by viewModel.orderLoaded.collectAsState()
    val availableServices by viewModel.services.collectAsState()
    val availableProducts by viewModel.products.collectAsState()
    val orderFinalized    by viewModel.orderFinalized.collectAsState()

    // Carrega a OS ao abrir a tela
    LaunchedEffect(orderId) { viewModel.loadOrder(orderId) }

    // ── Estado local dos itens ──────────────────────────────────────────────
    // Inicializado UMA vez, quando orderLoaded fica true (garante que os itens
    // do banco já estão em _order antes de popular a lista da tela).
    var serviceItems by remember { mutableStateOf<List<ServiceItemState>>(emptyList()) }
    var itemsInitialized by remember { mutableStateOf(false) }

    LaunchedEffect(orderLoaded) {
        if (orderLoaded && !itemsInitialized) {
            serviceItems = order?.items?.map { it.toState() } ?: emptyList()
            itemsInitialized = true
        }
    }

    // Navega de volta quando finalizado ou salvo
    LaunchedEffect(orderFinalized) {
        if (orderFinalized) {
            viewModel.resetState()
            onOrderFinished()
        }
    }

    // ── Estado de diálogos ──────────────────────────────────────────────────
    var showServiceDialog   by remember { mutableStateOf(false) }
    var showFinalizeDialog  by remember { mutableStateOf(false) }
    var currentServiceIndex by remember { mutableStateOf<Int?>(null) }
    var showProductDialog   by remember { mutableStateOf(false) }

    // ── Derivações ──────────────────────────────────────────────────────────
    val isInProgress = order?.status == OrderStatus.IN_PROGRESS
    val canFinish    = serviceItems.isNotEmpty()
    val hasInvalidProducts = serviceItems.any { item ->
        item.service.usesProducts && item.products.any { p ->
            p.quantity.isBlank() || (p.quantity.toDoubleOrNull() ?: 0.0) <= 0
        }
    }

    // ── Scaffold ────────────────────────────────────────────────────────────
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            if (isInProgress && itemsInitialized && serviceItems.isNotEmpty())
                                "Editar Serviços — OS #$orderId"
                            else
                                "Adicionar Serviços — OS #$orderId",
                            fontWeight = FontWeight.Bold
                        )
                        order?.status?.let { status ->
                            Text(
                                status.displayName,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    // Salvar rascunho (OS em andamento com itens)
                    if (isInProgress && canFinish && !hasInvalidProducts) {
                        IconButton(onClick = {
                            viewModel.saveItemsWithoutFinalizing(
                                orderId, serviceItems.map { it.toOrderItem() }
                            )
                        }) {
                            Icon(
                                Icons.Default.Save,
                                contentDescription = "Salvar sem finalizar",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            if (canFinish && !hasInvalidProducts) {
                ExtendedFloatingActionButton(
                    onClick = { showFinalizeDialog = true },
                    icon = { Icon(Icons.Default.Check, null) },
                    text = { Text("Finalizar OS") },
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            }
        }
    ) { paddingValues ->

        if (!orderLoaded) {
            // Loading state
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Banner: produtos sem quantidade
            if (hasInvalidProducts) {
                item {
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Warning, null, tint = MaterialTheme.colorScheme.error)
                            Text(
                                "Informe a quantidade de todos os produtos antes de finalizar.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }

            // Card resumo da OS
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Cliente",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                        )
                        Text(
                            client?.name ?: "—",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        HorizontalDivider()
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Data/Hora Início",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                        )
                        Text(
                            order?.startDateTime ?: "—",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Cabeçalho serviços + botão adicionar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Serviços", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        if (serviceItems.isNotEmpty()) {
                            Text(
                                "${serviceItems.size} serviço(s)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    FilledTonalButton(onClick = { showServiceDialog = true }) {
                        Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Adicionar")
                    }
                }
            }

            // Empty state
            if (serviceItems.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.Build,
                                    null,
                                    modifier = Modifier.size(40.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                )
                                Spacer(Modifier.height(8.dp))
                                Text("Nenhum serviço adicionado", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    "Toque em 'Adicionar' para começar",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }

            // Lista de serviços
            itemsIndexed(serviceItems) { index, item ->
                ServiceItemCard(
                    item = item,
                    index = index,
                    total = serviceItems.size,
                    onObservationsChange = { obs ->
                        serviceItems = serviceItems.toMutableList().also { it[index] = it[index].copy(observations = obs) }
                    },
                    onAddProduct = { currentServiceIndex = index; showProductDialog = true },
                    onRemoveProduct = { pi ->
                        serviceItems = serviceItems.toMutableList().also { list ->
                            list[index] = list[index].copy(products = list[index].products.toMutableList().also { it.removeAt(pi) })
                        }
                    },
                    onUpdateProductQuantity = { pi, qty ->
                        serviceItems = serviceItems.toMutableList().also { list ->
                            list[index] = list[index].copy(products = list[index].products.toMutableList().also { it[pi] = it[pi].copy(quantity = qty) })
                        }
                    },
                    onRemove = {
                        serviceItems = serviceItems.toMutableList().also { it.removeAt(index) }
                    }
                )
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }

    // ── Diálogo: selecionar serviço ─────────────────────────────────────────
    if (showServiceDialog) {
        ServiceSelectionDialog(
            services = availableServices,
            onServiceSelected = { svc ->
                serviceItems = serviceItems + ServiceItemState(svc)
                showServiceDialog = false
            },
            onDismiss = { showServiceDialog = false }
        )
    }

    // ── Diálogo: selecionar produto ─────────────────────────────────────────
    if (showProductDialog && currentServiceIndex != null) {
        val idx = currentServiceIndex!!
        ProductSelectionDialog(
            products = availableProducts,
            onProductSelected = { product, quantity ->
                serviceItems = serviceItems.toMutableList().also { list ->
                    list[idx] = list[idx].copy(products = list[idx].products + ProductItemState(product, quantity))
                }
                showProductDialog = false
                currentServiceIndex = null
            },
            onDismiss = { showProductDialog = false; currentServiceIndex = null }
        )
    }

    // ── Diálogo: confirmar finalização ──────────────────────────────────────
    if (showFinalizeDialog) {
        AlertDialog(
            onDismissRequest = { showFinalizeDialog = false },
            icon = { Icon(Icons.Default.CheckCircle, null) },
            title = { Text("Finalizar OS #$orderId?") },
            text = {
                Column {
                    Text("Confirma a finalização com ${serviceItems.size} serviço(s)?")
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Esta ação não pode ser desfeita.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    showFinalizeDialog = false
                    viewModel.finalizeOrder(orderId, serviceItems.map { it.toOrderItem() })
                }) { Text("Finalizar") }
            },
            dismissButton = {
                TextButton(onClick = { showFinalizeDialog = false }) { Text("Voltar") }
            }
        )
    }
}

// ── ServiceItemCard ─────────────────────────────────────────────────────────

@Composable
fun ServiceItemCard(
    item: ServiceItemState,
    index: Int,
    total: Int,
    onObservationsChange: (String) -> Unit,
    onAddProduct: () -> Unit,
    onRemoveProduct: (Int) -> Unit,
    onUpdateProductQuantity: (Int, String) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Cabeçalho do serviço
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Surface(color = MaterialTheme.colorScheme.secondary, shape = MaterialTheme.shapes.small) {
                        Text(
                            "${index + 1}/$total",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(item.service.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(
                            item.service.type,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, "Remover serviço", tint = MaterialTheme.colorScheme.error)
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = item.observations,
                onValueChange = onObservationsChange,
                label = { Text("Observações") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4
            )

            if (item.service.usesProducts) {
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Produtos", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    FilledTonalButton(
                        onClick = onAddProduct,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Adicionar", style = MaterialTheme.typography.labelMedium)
                    }
                }
                item.products.forEachIndexed { pi, product ->
                    Spacer(Modifier.height(8.dp))
                    ProductItemRow(
                        product = product,
                        onQuantityChange = { onUpdateProductQuantity(pi, it) },
                        onRemove = { onRemoveProduct(pi) }
                    )
                }
            }
        }
    }
}

// ── ProductItemRow ──────────────────────────────────────────────────────────

@Composable
fun ProductItemRow(
    product: ProductItemState,
    onQuantityChange: (String) -> Unit,
    onRemove: () -> Unit
) {
    val qty = product.quantity.toDoubleOrNull()
    val isInvalid = product.quantity.isBlank() || qty == null || qty <= 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isInvalid)
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(product.product.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                Text(
                    product.product.unitMeasure.displayName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            OutlinedTextField(
                value = product.quantity,
                onValueChange = onQuantityChange,
                label = { Text("Qtd *") },
                suffix = { Text(product.product.unitMeasure.abbreviation) },
                isError = isInvalid,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.width(120.dp)
            )
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Close, "Remover produto", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

// ── Dialogs reutilizáveis ───────────────────────────────────────────────────

@Composable
fun ClientSelectionDialog(
    clients: List<Client>,
    onClientSelected: (Client) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.select_client)) },
        text = {
            LazyColumn {
                items(clients.filter { it.active }.size) { i ->
                    val c = clients.filter { it.active }[i]
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), onClick = { onClientSelected(c) }) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(c.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(c.address, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) } }
    )
}

@Composable
fun ServiceSelectionDialog(
    services: List<Service>,
    onServiceSelected: (Service) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.select_service)) },
        text = {
            LazyColumn {
                items(services.size) { i ->
                    val s = services[i]
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), onClick = { onServiceSelected(s) }) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(s.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(s.type, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) } }
    )
}

@Composable
fun ProductSelectionDialog(
    products: List<Product>,
    onProductSelected: (Product, String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var quantity        by remember { mutableStateOf("") }
    val qty             = quantity.toDoubleOrNull()
    val isInvalid       = quantity.isBlank() || qty == null || qty <= 0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.select_product)) },
        text = {
            if (selectedProduct == null) {
                LazyColumn {
                    items(products.size) { i ->
                        val p = products[i]
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), onClick = { selectedProduct = p }) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(p.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(p.unitMeasure.displayName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            } else {
                Column {
                    Text(selectedProduct!!.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("${stringResource(R.string.quantity)} *") },
                        suffix = { Text(selectedProduct!!.unitMeasure.abbreviation) },
                        isError = quantity.isNotEmpty() && isInvalid,
                        supportingText = if (quantity.isNotEmpty() && isInvalid) {
                            { Text("Informe um valor maior que zero") }
                        } else null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            if (selectedProduct != null) {
                Button(
                    onClick = { onProductSelected(selectedProduct!!, quantity) },
                    enabled = !isInvalid
                ) { Text(stringResource(R.string.add)) }
            }
        },
        dismissButton = {
            TextButton(onClick = { if (selectedProduct != null) selectedProduct = null else onDismiss() }) {
                Text(if (selectedProduct != null) "Voltar" else stringResource(R.string.cancel))
            }
        }
    )
}