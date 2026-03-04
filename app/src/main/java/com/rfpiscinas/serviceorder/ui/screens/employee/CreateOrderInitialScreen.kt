package com.rfpiscinas.serviceorder.ui.screens.employee

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rfpiscinas.serviceorder.data.model.Client
import com.rfpiscinas.serviceorder.data.model.User
import com.rfpiscinas.serviceorder.ui.viewmodel.AddServicesViewModel
import com.rfpiscinas.serviceorder.util.DateMaskTransformation
import com.rfpiscinas.serviceorder.util.DateUtils
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrderInitialScreen(
    currentUser: User,
    onNavigateBack: () -> Unit,
    onOrderCreated: (Long) -> Unit,
    viewModel: AddServicesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var selectedClient by remember { mutableStateOf<Client?>(null) }
    var showClientDialog by remember { mutableStateOf(false) }

    // Inicializa com data/hora atual no novo formato
    // startDate armazena apenas dígitos; DateMaskTransformation exibe DD/MM/YYYY
    var startDate by remember { mutableStateOf(DateUtils.displayToDigits(DateUtils.today())) }
    var startTime by remember { mutableStateOf("") }

    // Preenche hora atual na primeira composição
    LaunchedEffect(Unit) {
        val cal = Calendar.getInstance()
        val h = cal.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
        val m = cal.get(Calendar.MINUTE).toString().padStart(2, '0')
        startTime = "$h:$m"
    }

    val activeClients by viewModel.activeClients.collectAsState()
    val orderCreatedId by viewModel.orderCreatedId.collectAsState()

    LaunchedEffect(orderCreatedId) {
        orderCreatedId?.let {
            viewModel.resetState()
            onOrderCreated(it)
        }
    }

    val isValid = selectedClient != null && startDate.isNotEmpty() && startTime.isNotEmpty()

    // DatePickerDialog do Android — abre calendário nativo
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            startDate = DateUtils.displayToDigits("${day.toString().padStart(2, '0')}/${(month + 1).toString().padStart(2, '0')}/$year")
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // TimePickerDialog do Android — abre relógio nativo
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            startTime = "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true // formato 24h
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nova Ordem de Serviço", fontWeight = FontWeight.Bold) },
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Prestador
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.secondary)
                    Column {
                        Text("Prestador", style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f))
                        Text(currentUser.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Seleção de Cliente
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Cliente *", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    if (selectedClient != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(selectedClient!!.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Text(selectedClient!!.address, style = MaterialTheme.typography.bodySmall)
                                }
                                IconButton(onClick = { showClientDialog = true }) {
                                    Icon(Icons.Default.Edit, "Alterar cliente")
                                }
                            }
                        }
                    } else {
                        OutlinedButton(onClick = { showClientDialog = true }, modifier = Modifier.fillMaxWidth()) {
                            Icon(Icons.Default.PersonAdd, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Selecionar Cliente")
                        }
                    }
                }
            }

            // Data e Hora com pickers nativos
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Data e Hora de Início *", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Campo de data — abre DatePickerDialog ao tocar
                        OutlinedTextField(
                            value = startDate,
                            onValueChange = { startDate = DateUtils.filterDateDigits(it) },
                            visualTransformation = DateMaskTransformation(),
                            label = { Text("Data") },
                            placeholder = { Text("DD/MM/AAAA") },
                            leadingIcon = {
                                IconButton(onClick = { datePickerDialog.show() }) {
                                    Icon(Icons.Default.CalendarToday, "Abrir calendário",
                                        tint = MaterialTheme.colorScheme.primary)
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        // Campo de hora — abre TimePickerDialog ao tocar
                        OutlinedTextField(
                            value = startTime,
                            onValueChange = { startTime = it },
                            label = { Text("Hora") },
                            placeholder = { Text("HH:MM") },
                            leadingIcon = {
                                IconButton(onClick = { timePickerDialog.show() }) {
                                    Icon(Icons.Default.AccessTime, "Abrir relógio",
                                        tint = MaterialTheme.colorScheme.primary)
                                }
                            },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Toque no ícone para usar o calendário/relógio, ou digite diretamente.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    selectedClient?.let { client ->
                        // Monta o startDateTime no formato DD/MM/YYYY HH:mm:ss
                        val datetime = "${DateUtils.digitsToDisplay(startDate)} ${startTime.trim()}:00"
                        viewModel.createAndStartOrder(
                            client = client,
                            employeeId = currentUser.id,
                            employeeName = currentUser.name.split(" ").first(),
                            startDateTime = datetime
                        )
                    }
                },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.ArrowForward, null)
                Spacer(Modifier.width(8.dp))
                Text("Iniciar Atendimento e Lançar Serviços")
            }
        }
    }

    // Dialog de seleção de cliente
    if (showClientDialog) {
        AlertDialog(
            onDismissRequest = { showClientDialog = false },
            title = { Text("Selecionar Cliente") },
            text = {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (activeClients.isEmpty()) {
                        item {
                            Text("Nenhum cliente ativo disponível",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    } else {
                        items(activeClients) { client ->
                            Card(
                                modifier = Modifier.fillMaxWidth().clickable {
                                    selectedClient = client
                                    showClientDialog = false
                                },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedClient?.id == client.id)
                                        MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(client.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                    Text(client.address, style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showClientDialog = false }) { Text("Fechar") } }
        )
    }
}