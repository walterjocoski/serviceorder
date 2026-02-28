package com.rfpiscinas.serviceorder.ui.screens.manager

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
    val allOrders by viewModel.allOrders.collectAsState()
    val employeeNames by viewModel.employeeNames.collectAsState()
    val selectedStatus by viewModel.selectedStatus.collectAsState()
    val selectedEmployee by viewModel.selectedEmployee.collectAsState()

    var showFilterDialog by remember { mutableStateOf(false) }
    var showEmployeeDialog by remember { mutableStateOf(false) }

    val filteredOrders = viewModel.getFilteredOrders()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Todas as Ordens",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    // Filter by employee
                    IconButton(onClick = { showEmployeeDialog = true }) {
                        Icon(Icons.Default.Person, contentDescription = "Filtrar por Prestador")
                    }
                    // Filter by status
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtrar por Status")
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Active filters chips
            if (selectedStatus != null || selectedEmployee != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    selectedStatus?.let { status ->
                        FilterChip(
                            selected = true,
                            onClick = { viewModel.setStatusFilter(null) },
                            label = { Text("Status: ${status.displayName}") }
                        )
                    }
                    selectedEmployee?.let { employee ->
                        FilterChip(
                            selected = true,
                            onClick = { viewModel.setEmployeeFilter(null) },
                            label = { Text("Prestador: $employee") }
                        )
                    }
                }
            }

            // Order count
            Text(
                text = "${filteredOrders.size} ordem(ns) encontrada(s)",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredOrders) { order ->
                    OrderCard(
                        order = order,
                        onClick = { onOrderClick(order.id) }
                    )
                }

                if (filteredOrders.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            Text(
                                text = "Nenhuma ordem encontrada",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    // Status Filter Dialog
    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("Filtrar por Status") },
            text = {
                Column {
                    // Option to clear
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        onClick = {
                            viewModel.setStatusFilter(null)
                            showFilterDialog = false
                        }
                    ) {
                        Text(
                            text = "Todos os Status",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    OrderStatus.values().forEach { status ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            onClick = {
                                viewModel.setStatusFilter(status)
                                showFilterDialog = false
                            }
                        ) {
                            Text(
                                text = status.displayName,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showFilterDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Employee Filter Dialog
    if (showEmployeeDialog) {
        AlertDialog(
            onDismissRequest = { showEmployeeDialog = false },
            title = { Text("Filtrar por Prestador") },
            text = {
                Column {
                    // Option to clear
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        onClick = {
                            viewModel.setEmployeeFilter(null)
                            showEmployeeDialog = false
                        }
                    ) {
                        Text(
                            text = "Todos os Prestadores",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    employeeNames.forEach { name ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            onClick = {
                                viewModel.setEmployeeFilter(name)
                                showEmployeeDialog = false
                            }
                        ) {
                            Text(
                                text = name,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showEmployeeDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
