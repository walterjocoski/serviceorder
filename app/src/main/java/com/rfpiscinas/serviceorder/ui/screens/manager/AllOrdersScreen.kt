package com.rfpiscinas.serviceorder.ui.screens.manager

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rfpiscinas.serviceorder.util.DateMaskTransformation
import com.rfpiscinas.serviceorder.util.DateUtils
import com.rfpiscinas.serviceorder.data.model.OrderStatus
import com.rfpiscinas.serviceorder.ui.screens.employee.OrderCard
import com.rfpiscinas.serviceorder.ui.viewmodel.AllOrdersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllOrdersScreen(
    onNavigateBack: () -> Unit,
    onOrderClick: (Long) -> Unit,
    viewModel: AllOrdersViewModel = hiltViewModel()
) {
    val filteredOrders by viewModel.filteredOrders.collectAsState()
    val hasActiveFilters by viewModel.hasActiveFilters.collectAsState()
    val employeeNames by viewModel.employeeNames.collectAsState()
    val clientNames by viewModel.clientNames.collectAsState()
    val selectedStatus by viewModel.selectedStatus.collectAsState()
    val selectedEmployee by viewModel.selectedEmployee.collectAsState()
    val selectedClient by viewModel.selectedClient.collectAsState()
    val dateFrom by viewModel.dateFrom.collectAsState()
    val dateTo by viewModel.dateTo.collectAsState()

    var showFilterPanel by remember { mutableStateOf(false) }

    // Paginação manual: 15 itens por página
    val pageSize = 15
    var visibleCount by remember(filteredOrders) { mutableIntStateOf(pageSize) }
    val visibleOrders = filteredOrders.take(visibleCount)
    var showStatusDialog by remember { mutableStateOf(false) }
    var showEmployeeDialog by remember { mutableStateOf(false) }
    var showClientDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todas as Ordens", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    BadgedBox(badge = { if (hasActiveFilters) Badge() }) {
                        IconButton(onClick = { showFilterPanel = !showFilterPanel }) {
                            Icon(Icons.Default.FilterList, contentDescription = "Filtros")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            // Painel de filtros expansível
            AnimatedVisibility(visible = showFilterPanel) {
                FilterPanel(
                    selectedStatus = selectedStatus,
                    selectedEmployee = selectedEmployee,
                    selectedClient = selectedClient,
                    dateFrom = dateFrom,
                    dateTo = dateTo,
                    onStatusClick = { showStatusDialog = true },
                    onEmployeeClick = { showEmployeeDialog = true },
                    onClientClick = { showClientDialog = true },
                    onDateFromChange = { viewModel.setDateFrom(it.ifBlank { null }) },
                    onDateToChange = { viewModel.setDateTo(it.ifBlank { null }) },
                    onClearAll = { viewModel.clearAllFilters() }
                )
            }

            // Chips dos filtros ativos
            if (hasActiveFilters) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    selectedStatus?.let {
                        InputChip(selected = true, onClick = { viewModel.setStatusFilter(null) },
                            label = { Text(it.displayName) },
                            trailingIcon = { Icon(Icons.Default.Close, null, Modifier.size(16.dp)) })
                    }
                    selectedEmployee?.let {
                        InputChip(selected = true, onClick = { viewModel.setEmployeeFilter(null) },
                            label = { Text(it) },
                            trailingIcon = { Icon(Icons.Default.Close, null, Modifier.size(16.dp)) })
                    }
                    selectedClient?.let {
                        InputChip(selected = true, onClick = { viewModel.setClientFilter(null) },
                            label = { Text(it) },
                            trailingIcon = { Icon(Icons.Default.Close, null, Modifier.size(16.dp)) })
                    }
                    dateFrom?.let {
                        InputChip(selected = true, onClick = { viewModel.setDateFrom(null) },
                            label = { Text("De: $it") },
                            trailingIcon = { Icon(Icons.Default.Close, null, Modifier.size(16.dp)) })
                    }
                    dateTo?.let {
                        InputChip(selected = true, onClick = { viewModel.setDateTo(null) },
                            label = { Text("Até: $it") },
                            trailingIcon = { Icon(Icons.Default.Close, null, Modifier.size(16.dp)) })
                    }
                }
            }

            // Contador
            Text(
                "${filteredOrders.size} ordem(ns) encontrada(s)",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Lista ou estado vazio
            if (filteredOrders.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.SearchOff, null, Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
                        Spacer(Modifier.height(16.dp))
                        Text("Nenhuma ordem encontrada",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (hasActiveFilters) {
                            Spacer(Modifier.height(8.dp))
                            TextButton(onClick = { viewModel.clearAllFilters() }) { Text("Limpar filtros") }
                        }
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(visibleOrders, key = { it.id }) { order ->
                        OrderCard(order = order, onClick = { onOrderClick(order.id) })
                    }
                    if (visibleOrders.size < filteredOrders.size) {
                        item {
                            TextButton(
                                onClick = { visibleCount += pageSize },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text("Carregar mais (${filteredOrders.size - visibleOrders.size} restantes)")
                            }
                        }
                    }
                    item { Spacer(Modifier.height(16.dp)) }
                }
            }
        }
    }

    // --- Dialogs ---

    if (showStatusDialog) {
        OptionDialog(
            title = "Filtrar por Status",
            allLabel = "Todos os status",
            options = OrderStatus.values().map { it.displayName },
            selected = selectedStatus?.displayName,
            onSelect = { label ->
                val status = OrderStatus.values().first { it.displayName == label }
                viewModel.setStatusFilter(status)
                showStatusDialog = false
            },
            onClear = { viewModel.setStatusFilter(null); showStatusDialog = false },
            onDismiss = { showStatusDialog = false }
        )
    }

    if (showEmployeeDialog) {
        OptionDialog(
            title = "Filtrar por Prestador",
            allLabel = "Todos os prestadores",
            options = employeeNames,
            selected = selectedEmployee,
            onSelect = { viewModel.setEmployeeFilter(it); showEmployeeDialog = false },
            onClear = { viewModel.setEmployeeFilter(null); showEmployeeDialog = false },
            onDismiss = { showEmployeeDialog = false }
        )
    }

    if (showClientDialog) {
        OptionDialog(
            title = "Filtrar por Cliente",
            allLabel = "Todos os clientes",
            options = clientNames,
            selected = selectedClient,
            onSelect = { viewModel.setClientFilter(it); showClientDialog = false },
            onClear = { viewModel.setClientFilter(null); showClientDialog = false },
            onDismiss = { showClientDialog = false }
        )
    }
}

// Painel de filtros
@Composable
private fun FilterPanel(
    selectedStatus: OrderStatus?,
    selectedEmployee: String?,
    selectedClient: String?,
    dateFrom: String?,
    dateTo: String?,
    onStatusClick: () -> Unit,
    onEmployeeClick: () -> Unit,
    onClientClick: () -> Unit,
    onDateFromChange: (String) -> Unit,
    onDateToChange: (String) -> Unit,
    onClearAll: () -> Unit
) {
    var localFrom by remember(dateFrom) { mutableStateOf(dateFrom ?: "") }
    var localTo by remember(dateTo) { mutableStateOf(dateTo ?: "") }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Filtros", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                TextButton(onClick = onClearAll) {
                    Icon(Icons.Default.ClearAll, null, Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Limpar tudo")
                }
            }

            // Botões de filtro: Status | Prestador | Cliente
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterButton("Status", selectedStatus?.displayName, onStatusClick, Modifier.weight(1f))
                FilterButton("Prestador", selectedEmployee, onEmployeeClick, Modifier.weight(1f))
                FilterButton("Cliente", selectedClient, onClientClick, Modifier.weight(1f))
            }

            // Período
            Text("Período (data de início)", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = localFrom,
                    onValueChange = { val v = DateUtils.filterDateDigits(it); localFrom = v; onDateFromChange(DateUtils.digitsToDisplay(v)) },
                    visualTransformation = DateMaskTransformation(),
                    label = { Text("De") },
                    placeholder = { Text("DD/MM/AAAA") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = localTo,
                    onValueChange = { val v = DateUtils.filterDateDigits(it); localTo = v; onDateToChange(DateUtils.digitsToDisplay(v)) },
                    visualTransformation = DateMaskTransformation(),
                    label = { Text("Até") },
                    placeholder = { Text("DD/MM/AAAA") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun FilterButton(label: String, value: String?, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                value ?: "Todos",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (value != null) FontWeight.Bold else FontWeight.Normal,
                color = if (value != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}

// Dialog genérico de seleção com opção única
@Composable
private fun OptionDialog(
    title: String,
    allLabel: String,
    options: List<String>,
    selected: String?,
    onSelect: (String) -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                // Opção "Todos"
                Card(
                    onClick = onClear,
                    colors = CardDefaults.cardColors(
                        containerColor = if (selected == null)
                            MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (selected == null)
                            Icon(Icons.Default.Check, null, Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                        else
                            Spacer(Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(allLabel, fontWeight = FontWeight.Bold)
                    }
                }
                // Opções
                options.forEach { option ->
                    Card(
                        onClick = { onSelect(option) },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selected == option)
                                MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (selected == option)
                                Icon(Icons.Default.Check, null, Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                            else
                                Spacer(Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(option)
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text("Fechar") } }
    )
}