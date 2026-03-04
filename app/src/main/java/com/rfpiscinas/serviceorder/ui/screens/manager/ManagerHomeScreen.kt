package com.rfpiscinas.serviceorder.ui.screens.manager

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rfpiscinas.serviceorder.data.model.User
import com.rfpiscinas.serviceorder.data.model.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerHomeScreen(
    currentUser: User,
    onNavigateToClients: () -> Unit = {},
    onNavigateToEmployees: () -> Unit = {},
    onNavigateToServices: () -> Unit = {},
    onNavigateToProducts: () -> Unit = {},
    onNavigateToOrders: () -> Unit = {},
    onNavigateToReports: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    val roleLabel = when (currentUser.role) {
        UserRole.MASTER -> "Administrador"
        UserRole.MANAGER -> "Gerente"
        else -> currentUser.role.name
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("RF Piscinas", fontWeight = FontWeight.Bold)
                        Text(
                            text = "$roleLabel · ${currentUser.name.split(" ").first()}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(
                            Icons.Default.Logout,
                            contentDescription = "Sair",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Saudação
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.AccountCircle,
                        null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            "Olá, ${currentUser.name.split(" ").first()}!",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            currentUser.email,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                        Text(
                            roleLabel,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            ManagerFeatureCard(Icons.Default.Assignment, "Todas as Ordens",
                "Visualizar e gerenciar todas as OS", onNavigateToOrders)
            ManagerFeatureCard(Icons.Default.People, "Clientes",
                "Cadastrar e gerenciar clientes", onNavigateToClients)
            ManagerFeatureCard(Icons.Default.Engineering, "Funcionários",
                "Cadastrar e gerenciar funcionários", onNavigateToEmployees)
            ManagerFeatureCard(Icons.Default.Build, "Serviços",
                "Cadastrar e gerenciar serviços", onNavigateToServices)
            ManagerFeatureCard(Icons.Default.Inventory, "Produtos",
                "Cadastrar e gerenciar produtos", onNavigateToProducts)
            ManagerFeatureCard(Icons.Default.Assessment, "Relatórios",
                "Gerar relatórios em PDF", onNavigateToReports)

            Spacer(Modifier.height(16.dp))
        }
    }

    // Confirmação de logout
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = { Icon(Icons.Default.Logout, null) },
            title = { Text("Sair do sistema") },
            text = { Text("Deseja encerrar a sessão de ${currentUser.name.split(" ").first()}?") },
            confirmButton = {
                Button(
                    onClick = { showLogoutDialog = false; onLogout() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Sair") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun ManagerFeatureCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
            Icon(Icons.Default.ChevronRight, null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.4f))
        }
    }
}
