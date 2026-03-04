package com.rfpiscinas.serviceorder.ui.screens.manager

import android.app.DatePickerDialog
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rfpiscinas.serviceorder.data.model.User
import com.rfpiscinas.serviceorder.data.model.UserRole
import com.rfpiscinas.serviceorder.ui.viewmodel.EmployeeManagementViewModel
import com.rfpiscinas.serviceorder.util.DateMaskTransformation
import com.rfpiscinas.serviceorder.util.DateUtils
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeManagementScreen(
    onNavigateBack: () -> Unit,
    viewModel: EmployeeManagementViewModel = hiltViewModel()
) {
    val employees by viewModel.employees.collectAsState()
    val message by viewModel.message.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingEmployee by remember { mutableStateOf<User?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        message?.let { snackbarHostState.showSnackbar(it); viewModel.clearMessage() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Funcionários", fontWeight = FontWeight.Bold) },
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
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("Novo Funcionário") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (employees.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Engineering, null, Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
                    Spacer(Modifier.height(16.dp))
                    Text("Nenhum funcionário cadastrado",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(employees, key = { it.id }) { employee ->
                    EmployeeCard(
                        employee = employee,
                        onEdit = { editingEmployee = employee },
                        onToggleActive = {
                            viewModel.updateEmployee(employee.copy(active = !employee.active))
                        }
                    )
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }

    if (showAddDialog || editingEmployee != null) {
        EmployeeFormDialog(
            employee = editingEmployee,
            onDismiss = { showAddDialog = false; editingEmployee = null },
            onSave = { employee, plainPassword ->
                if (editingEmployee != null)
                    viewModel.updateEmployee(employee, plainPassword)
                else
                    viewModel.addEmployee(employee, plainPassword)
                showAddDialog = false
                editingEmployee = null
            }
        )
    }
}

@Composable
fun EmployeeCard(employee: User, onEdit: () -> Unit, onToggleActive: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (employee.active) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Person, null, Modifier.size(42.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(employee.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            if (!employee.active) {
                                Spacer(Modifier.width(8.dp))
                                Surface(color = MaterialTheme.colorScheme.errorContainer, shape = MaterialTheme.shapes.small) {
                                    Text("INATIVO", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        Text(employee.email, style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f))
                        if (employee.startDate.isNotEmpty()) {
                            Text("Início: ${employee.startDate}", style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f))
                        }
                    }
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, "Editar", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onToggleActive) {
                        Icon(
                            if (employee.active) Icons.Default.ToggleOn else Icons.Default.ToggleOff,
                            if (employee.active) "Inativar" else "Ativar",
                            tint = if (employee.active) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            if (employee.phone.isNotEmpty() || employee.address.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(Modifier.height(8.dp))
            }
            if (employee.phone.isNotEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, null, Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(employee.phone, style = MaterialTheme.typography.bodySmall)
                }
            }
            if (employee.address.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(employee.address, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun EmployeeFormDialog(
    employee: User?,
    onDismiss: () -> Unit,
    onSave: (User, String) -> Unit  // (user, plainPassword)
) {
    val context = LocalContext.current
    val isEditing = employee != null

    var name by remember { mutableStateOf(employee?.name ?: "") }
    var email by remember { mutableStateOf(employee?.email ?: "") }
    var phone by remember { mutableStateOf(employee?.phone ?: "") }
    var address by remember { mutableStateOf(employee?.address ?: "") }
    // startDate armazena apenas dígitos; DateMaskTransformation exibe DD/MM/YYYY
    var startDate by remember { mutableStateOf(DateUtils.displayToDigits(employee?.startDate ?: "")) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Senha: obrigatória em novo cadastro, opcional na edição
    val passwordOk = if (!isEditing) password.length >= 6 && password == confirmPassword
    else password.isEmpty() || (password.length >= 6 && password == confirmPassword)
    // Erro do campo senha: só valida tamanho mínimo
    val passwordError = if (password.isNotEmpty() && password.length < 6) "Mínimo 6 caracteres" else null
    // Erro do campo confirmação: valida se coincidem (só exibe quando confirmPassword foi tocado)
    val confirmError = if (confirmPassword.isNotEmpty() && password != confirmPassword) "Senhas não coincidem" else null
    val isValid = name.isNotBlank() && email.isNotBlank() && phone.isNotBlank() && passwordOk

    // DatePickerDialog para data de início
    val cal = Calendar.getInstance()
    val datePicker = DatePickerDialog(context, { _, y, m, d ->
        startDate = DateUtils.displayToDigits("${d.toString().padStart(2,'0')}/${(m+1).toString().padStart(2,'0')}/$y")
    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditing) "Editar Funcionário" else "Novo Funcionário") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(value = name, onValueChange = { name = it },
                    label = { Text("Nome *") }, singleLine = true, modifier = Modifier.fillMaxWidth())

                OutlinedTextField(value = email, onValueChange = { email = it },
                    label = { Text("E-mail *") }, singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth())

                OutlinedTextField(value = phone, onValueChange = { phone = it },
                    label = { Text("Telefone *") }, singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth())

                OutlinedTextField(value = address, onValueChange = { address = it },
                    label = { Text("Endereço") }, minLines = 2, maxLines = 3,
                    modifier = Modifier.fillMaxWidth())

                // Data de início com DatePicker
                OutlinedTextField(
                    value = startDate,
                    onValueChange = { startDate = DateUtils.filterDateDigits(it) },
                    visualTransformation = DateMaskTransformation(),
                    label = { Text("Data de Início") },
                    placeholder = { Text("DD/MM/AAAA") },
                    leadingIcon = {
                        IconButton(onClick = { datePicker.show() }) {
                            Icon(Icons.Default.CalendarToday, null, tint = MaterialTheme.colorScheme.primary)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                HorizontalDivider()

                // Seção senha
                Text(
                    if (isEditing) "Senha (deixe em branco para não alterar)" else "Senha de acesso *",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = password, onValueChange = { password = it },
                    label = { Text(if (isEditing) "Nova senha (opcional)" else "Senha *") },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                null
                            )
                        }
                    },
                    isError = passwordError != null,
                    supportingText = if (passwordError != null) {{ Text(passwordError) }} else null,
                    modifier = Modifier.fillMaxWidth()
                )

                if (password.isNotEmpty()) {
                    OutlinedTextField(
                        value = confirmPassword, onValueChange = { confirmPassword = it },
                        label = { Text("Confirmar senha") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        isError = confirmError != null,
                        supportingText = if (confirmError != null) {{ Text(confirmError) }} else null,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Text("* Campos obrigatórios",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        User(
                            id = employee?.id ?: 0,
                            name = name.trim(), email = email.trim().lowercase(),
                            phone = phone.trim(), address = address.trim(),
                            startDate = DateUtils.digitsToDisplay(startDate),
                            passwordHash = employee?.passwordHash ?: "", // mantido pelo VM
                            role = UserRole.EMPLOYEE,
                            active = employee?.active ?: true
                        ),
                        password  // plain text — VM faz o hash
                    )
                },
                enabled = isValid
            ) { Text("Salvar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}