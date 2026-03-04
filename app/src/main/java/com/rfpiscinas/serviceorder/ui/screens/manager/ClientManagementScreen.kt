package com.rfpiscinas.serviceorder.ui.screens.manager

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.rfpiscinas.serviceorder.data.model.Client
import com.rfpiscinas.serviceorder.ui.viewmodel.ClientManagementViewModel
import com.rfpiscinas.serviceorder.util.CpfCnpjMaskTransformation
import com.rfpiscinas.serviceorder.util.CpfCnpjUtils
import com.rfpiscinas.serviceorder.util.PhoneMaskTransformation
import com.rfpiscinas.serviceorder.util.PhoneUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientManagementScreen(
    onNavigateBack: () -> Unit,
    viewModel: ClientManagementViewModel = hiltViewModel()
) {
    val clients by viewModel.clients.collectAsState()
    val message by viewModel.message.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    // Paginação manual
    val pageSize = 20
    var clientsVisible by remember(clients) { mutableIntStateOf(pageSize) }
    val visibleClients = clients.take(clientsVisible)
    var editingClient by remember { mutableStateOf<Client?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Erros de duplicidade vindos do banco (e-mail e CPF/CNPJ)
    val emailError by viewModel.emailError.collectAsState()
    val cpfCnpjError by viewModel.cpfCnpjError.collectAsState()

    LaunchedEffect(message) {
        message?.let {
            // Fecha o formulário antes de exibir o snackbar (salvo com sucesso)
            if (showAddDialog || editingClient != null) {
                showAddDialog = false
                editingClient = null
            }
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clientes", fontWeight = FontWeight.Bold) },
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
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Novo Cliente") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (clients.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.PeopleAlt,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Nenhum cliente cadastrado",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(visibleClients, key = { it.id }) { client ->
                    ClientCard(
                        client = client,
                        onEdit = { editingClient = client },
                        onToggleActive = { viewModel.toggleActive(client) }
                    )
                }
                if (visibleClients.size < clients.size) {
                    item {
                        TextButton(
                            onClick = { clientsVisible += pageSize },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.ExpandMore, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Carregar mais (${clients.size - visibleClients.size} restantes)")
                        }
                    }
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }

    if (showAddDialog || editingClient != null) {
        ClientFormDialog(
            client = editingClient,
            emailError = emailError,
            cpfCnpjError = cpfCnpjError,
            onDismiss = {
                showAddDialog = false
                editingClient = null
                viewModel.clearErrors()
            },
            onSave = { client, cpfCnpjDigits ->
                if (editingClient != null) viewModel.updateClient(client, cpfCnpjDigits)
                else viewModel.addClient(client, cpfCnpjDigits)
            }
        )
    }
}

// ─── ClientCard ────────────────────────────────────────────────────────────

@Composable
fun ClientCard(client: Client, onEdit: () -> Unit, onToggleActive: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (client.active)
                MaterialTheme.colorScheme.surfaceVariant
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = client.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (!client.active) {
                            Spacer(Modifier.width(8.dp))
                            Surface(
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = MaterialTheme.shapes.small
                            ) {
                                Text(
                                    "INATIVO",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.error,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Text(
                        text = client.cpfCnpj,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onToggleActive) {
                        Icon(
                            if (client.active) Icons.Default.ToggleOn else Icons.Default.ToggleOff,
                            contentDescription = if (client.active) "Inativar" else "Ativar",
                            tint = if (client.active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.width(4.dp))
                Text(client.address, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Phone, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.width(4.dp))
                Text(client.phone, style = MaterialTheme.typography.bodySmall)
            }
            if (client.email.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Email, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(4.dp))
                    Text(client.email, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

// ─── ClientFormDialog ──────────────────────────────────────────────────────

@Composable
fun ClientFormDialog(
    client: Client?,
    emailError: String?,
    cpfCnpjError: String?,
    onDismiss: () -> Unit,
    onSave: (Client, String) -> Unit    // (cliente com display values, cpfCnpjDigits)
) {
    val isEditing = client != null

    // ── Estado: dígitos brutos (sem máscara) ──────────────────────────────
    var name       by remember { mutableStateOf(client?.name ?: "") }
    // CPF/CNPJ: armazena só dígitos; máscara é visual
    var cpfDigits  by remember { mutableStateOf(CpfCnpjUtils.filterDigits(client?.cpfCnpj ?: "")) }
    var address    by remember { mutableStateOf(client?.address ?: "") }
    // Telefone: armazena só dígitos; máscara é visual
    var phoneDigits by remember { mutableStateOf(PhoneUtils.filterDigits(client?.phone ?: "")) }
    var email      by remember { mutableStateOf(client?.email ?: "") }

    // ── Validações locais (formato) ───────────────────────────────────────
    val cpfCnpjFormatError = CpfCnpjUtils.errorMessage(cpfDigits)
    // Erro real = formato inválido OU duplicidade do banco
    val cpfCnpjFieldError  = cpfCnpjFormatError ?: cpfCnpjError

    val phoneError = when {
        phoneDigits.isEmpty() -> null  // obrigatoriedade tratada no isValid
        phoneDigits.length < 10 -> "Telefone incompleto"
        else -> null
    }

    val emailFormatError = if (email.isNotEmpty() && !email.contains('@')) "E-mail inválido" else null
    val emailFieldError  = emailFormatError ?: emailError

    val isValid = name.isNotBlank()
            && cpfDigits.isNotBlank()
            && cpfCnpjFormatError == null    // formato OK
            && address.isNotBlank()
            && phoneDigits.length >= 10       // mínimo telefone fixo
            && phoneError == null
            && emailFormatError == null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditing) "Editar Cliente" else "Novo Cliente") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // ── Nome ──────────────────────────────────────────────────
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // ── CPF / CNPJ com máscara e validação ───────────────────
                OutlinedTextField(
                    value = cpfDigits,
                    onValueChange = { cpfDigits = CpfCnpjUtils.filterDigits(it) },
                    visualTransformation = CpfCnpjMaskTransformation(),
                    label = { Text("CPF / CNPJ *") },
                    placeholder = { Text("000.000.000-00") },
                    isError = cpfCnpjFieldError != null,
                    supportingText = if (cpfCnpjFieldError != null) {
                        { Text(cpfCnpjFieldError) }
                    } else null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // ── Endereço ──────────────────────────────────────────────
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Endereço *") },
                    minLines = 2, maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                // ── Telefone com máscara ──────────────────────────────────
                OutlinedTextField(
                    value = phoneDigits,
                    onValueChange = { phoneDigits = PhoneUtils.filterDigits(it) },
                    visualTransformation = PhoneMaskTransformation(),
                    label = { Text("Telefone *") },
                    placeholder = { Text("(41) 99999-0000") },
                    isError = phoneError != null,
                    supportingText = if (phoneError != null) {
                        { Text(phoneError) }
                    } else null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // ── E-mail com validação de duplicidade ───────────────────
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail") },
                    isError = emailFieldError != null,
                    supportingText = if (emailFieldError != null) {
                        { Text(emailFieldError) }
                    } else null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    "* Campos obrigatórios",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Salva o CPF/CNPJ no formato visual (com máscara) para exibição
                    // Passa os dígitos brutos para validação de duplicidade no VM
                    val displayCpfCnpj = buildString {
                        cpfDigits.forEachIndexed { i, ch ->
                            val isCnpj = cpfDigits.length > 11
                            if (!isCnpj) {
                                if (i == 3 || i == 6) append('.')
                                if (i == 9) append('-')
                            } else {
                                if (i == 2 || i == 5) append('.')
                                if (i == 8) append('/')
                                if (i == 12) append('-')
                            }
                            append(ch)
                        }
                    }
                    val displayPhone = PhoneUtils.digitsToDisplay(phoneDigits)

                    onSave(
                        Client(
                            id = client?.id ?: 0,
                            name = name.trim(),
                            cpfCnpj = displayCpfCnpj,
                            address = address.trim(),
                            phone = displayPhone,
                            email = email.trim().lowercase(),
                            active = client?.active ?: true
                        ),
                        cpfDigits  // dígitos puros para checar duplicidade no banco
                    )
                },
                enabled = isValid
            ) { Text("Salvar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}