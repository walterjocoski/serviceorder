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

data class ServiceItemState(
    val service: Service,
    val observations: String = "",
    val products: List<ProductItemState> = emptyList()
)

data class ProductItemState(
    val product: Product,
    val quantity: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddServicesScreen(
    orderId: Long,
    clientId: Long,
    startDateTime: String,
    onNavigateBack: () -> Unit,
    onOrderFinished: () -> Unit,
    viewModel: AddServicesViewModel = hiltViewModel()
) {
    val client by viewModel.client.collectAsState()
    val availableServices by viewModel.services.collectAsState()
    val availableProducts by viewModel.products.collectAsState()
    
    val orderFinalized by viewModel.orderFinalized.collectAsState()

    // Navegar quando OS for finalizada
    LaunchedEffect(orderFinalized) {
        if (orderFinalized) {
            viewModel.resetState()
            onOrderFinished()
        }
    }

    LaunchedEffect(clientId) {
        viewModel.loadClient(clientId)
    }
    var serviceItems by remember { mutableStateOf<List<ServiceItemState>>(emptyList()) }
    var showServiceDialog by remember { mutableStateOf(false) }
    var currentServiceIndex by remember { mutableStateOf<Int?>(null) }
    var showProductDialog by remember { mutableStateOf(false) }
    
    val canFinish = serviceItems.isNotEmpty()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Adicionar Serviços - OS #$orderId",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
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
            if (canFinish) {
                ExtendedFloatingActionButton(
                    onClick = {
                        // Converter ServiceItemState -> ServiceOrderItem e finalizar
                        val orderItems = serviceItems.map { itemState ->
                            com.rfpiscinas.serviceorder.data.model.ServiceOrderItem(
                                service = itemState.service,
                                observations = itemState.observations,
                                products = itemState.products.mapNotNull { p ->
                                    val qty = p.quantity.toDoubleOrNull() ?: return@mapNotNull null
                                    com.rfpiscinas.serviceorder.data.model.ServiceOrderProduct(
                                        product = p.product,
                                        quantity = qty
                                    )
                                }
                            )
                        }
                        viewModel.finalizeOrder(orderId, orderItems)
                    },
                    icon = { Icon(Icons.Default.Check, contentDescription = null) },
                    text = { Text("Finalizar OS") },
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Instruções
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Column {
                            Text(
                                text = "Passo 2 de 2",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Adicione os serviços executados neste atendimento. Você pode adicionar observações e produtos para cada serviço.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
            
            // Informações da Ordem
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Cliente",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = client?.name ?: "Cliente não encontrado",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Data/Hora Início",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = startDateTime,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
            
            // Serviços
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.services),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    FilledTonalButton(
                        onClick = { showServiceDialog = true }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.add_service))
                    }
                }
            }
            
            if (serviceItems.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Nenhum serviço adicionado",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            itemsIndexed(serviceItems) { index, item ->
                ServiceItemCard(
                    item = item,
                    onObservationsChange = { newObs ->
                        serviceItems = serviceItems.toMutableList().apply {
                            this[index] = this[index].copy(observations = newObs)
                        }
                    },
                    onAddProduct = {
                        currentServiceIndex = index
                        showProductDialog = true
                    },
                    onRemoveProduct = { productIndex ->
                        serviceItems = serviceItems.toMutableList().apply {
                            this[index] = this[index].copy(
                                products = this[index].products.toMutableList().apply {
                                    removeAt(productIndex)
                                }
                            )
                        }
                    },
                    onUpdateProductQuantity = { productIndex, quantity ->
                        serviceItems = serviceItems.toMutableList().apply {
                            this[index] = this[index].copy(
                                products = this[index].products.toMutableList().apply {
                                    this[productIndex] = this[productIndex].copy(quantity = quantity)
                                }
                            )
                        }
                    },
                    onRemove = {
                        serviceItems = serviceItems.toMutableList().apply {
                            removeAt(index)
                        }
                    }
                )
            }
            
            // Espaço para o FAB
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
    
    // Dialog de seleção de serviço
    if (showServiceDialog) {
        ServiceSelectionDialog(
            services = availableServices,
            onServiceSelected = { service ->
                serviceItems = serviceItems + ServiceItemState(service)
                showServiceDialog = false
            },
            onDismiss = { showServiceDialog = false }
        )
    }
    
    // Dialog de seleção de produto
    if (showProductDialog && currentServiceIndex != null) {
        ProductSelectionDialog(
            products = availableProducts,
            onProductSelected = { product, quantity ->
                serviceItems = serviceItems.toMutableList().apply {
                    this[currentServiceIndex!!] = this[currentServiceIndex!!].copy(
                        products = this[currentServiceIndex!!].products + 
                            ProductItemState(product, quantity)
                    )
                }
                showProductDialog = false
                currentServiceIndex = null
            },
            onDismiss = {
                showProductDialog = false
                currentServiceIndex = null
            }
        )
    }
}

@Composable
fun ServiceItemCard(
    item: ServiceItemState,
    onObservationsChange: (String) -> Unit,
    onAddProduct: () -> Unit,
    onRemoveProduct: (Int) -> Unit,
    onUpdateProductQuantity: (Int, String) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.service.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = item.service.type,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                    )
                }
                IconButton(onClick = onRemove) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remover serviço",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedTextField(
                value = item.observations,
                onValueChange = onObservationsChange,
                label = { Text(stringResource(R.string.observations)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4
            )
            
            if (item.service.usesProducts) {
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.products),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    FilledTonalButton(
                        onClick = onAddProduct,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Adicionar", style = MaterialTheme.typography.labelMedium)
                    }
                }
                
                if (item.products.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    item.products.forEachIndexed { index, product ->
                        ProductItemRow(
                            product = product,
                            onQuantityChange = { onUpdateProductQuantity(index, it) },
                            onRemove = { onRemoveProduct(index) }
                        )
                        if (index < item.products.size - 1) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItemRow(
    product: ProductItemState,
    onQuantityChange: (String) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = product.product.unitMeasure.displayName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            OutlinedTextField(
                value = product.quantity,
                onValueChange = onQuantityChange,
                label = { Text("Qtd") },
                suffix = { Text(product.product.unitMeasure.abbreviation) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.width(120.dp)
            )
            
            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Remover produto",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun ClientSelectionDialog(
    clients: List<Client>,
    onClientSelected: (Client) -> Unit,
    onDismiss: () -> Unit
) {
    val activeClients = clients.filter { it.active }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.select_client)) },
        text = {
            LazyColumn {
                items(activeClients.size) { index ->
                    val client = activeClients[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        onClick = { onClientSelected(client) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = client.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = client.address,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
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
                items(services.size) { index ->
                    val service = services[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        onClick = { onServiceSelected(service) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = service.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = service.type,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
fun ProductSelectionDialog(
    products: List<Product>,
    onProductSelected: (Product, String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var quantity by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.select_product)) },
        text = {
            Column {
                if (selectedProduct == null) {
                    LazyColumn {
                        items(products.size) { index ->
                            val product = products[index]
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                onClick = { selectedProduct = product }
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = product.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = product.unitMeasure.displayName,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Column {
                        Text(
                            text = selectedProduct!!.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = quantity,
                            onValueChange = { quantity = it },
                            label = { Text(stringResource(R.string.quantity)) },
                            suffix = { Text(selectedProduct!!.unitMeasure.abbreviation) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (selectedProduct != null) {
                Button(
                    onClick = {
                        if (quantity.isNotEmpty()) {
                            onProductSelected(selectedProduct!!, quantity)
                        }
                    },
                    enabled = quantity.isNotEmpty()
                ) {
                    Text(stringResource(R.string.add))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
