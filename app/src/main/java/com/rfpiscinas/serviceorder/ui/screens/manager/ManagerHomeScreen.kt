package com.rfpiscinas.serviceorder.ui.screens.manager

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerHomeScreen(
    onNavigateToClients: () -> Unit = {},
    onNavigateToEmployees: () -> Unit = {},
    onNavigateToServices: () -> Unit = {},
    onNavigateToProducts: () -> Unit = {},
    onNavigateToOrders: () -> Unit = {},
    onNavigateToReports: () -> Unit = {}
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "RF Piscinas - Gerente",
                        fontWeight = FontWeight.Bold
                    ) 
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Dashboard do Gerente",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Cards de funcionalidades
            ManagerFeatureCard(
                icon = Icons.Default.Assignment,
                title = "Todas as Ordens",
                description = "Visualizar e gerenciar todas as ordens de serviço",
                onClick = onNavigateToOrders
            )
            
            ManagerFeatureCard(
                icon = Icons.Default.People,
                title = "Clientes",
                description = "Cadastrar e gerenciar clientes",
                onClick = onNavigateToClients
            )
            
            ManagerFeatureCard(
                icon = Icons.Default.Person,
                title = "Funcionários",
                description = "Cadastrar e gerenciar funcionários",
                onClick = onNavigateToEmployees
            )
            
            ManagerFeatureCard(
                icon = Icons.Default.Build,
                title = "Serviços",
                description = "Cadastrar e gerenciar serviços oferecidos",
                onClick = onNavigateToServices
            )
            
            ManagerFeatureCard(
                icon = Icons.Default.Inventory,
                title = "Produtos",
                description = "Cadastrar e gerenciar produtos utilizados",
                onClick = onNavigateToProducts
            )
            
            ManagerFeatureCard(
                icon = Icons.Default.Assessment,
                title = "Relatórios",
                description = "Gerar relatórios de ordens e clientes",
                onClick = onNavigateToReports
            )

        }
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
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f)
            )
        }
    }
}
