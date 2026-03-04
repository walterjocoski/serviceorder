package com.rfpiscinas.serviceorder.ui.screens.manager

import android.app.DatePickerDialog
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.rfpiscinas.serviceorder.util.DateMaskTransformation
import com.rfpiscinas.serviceorder.util.DateUtils
import com.rfpiscinas.serviceorder.ui.viewmodel.ReportViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    onNavigateBack: () -> Unit,
    viewModel: ReportViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val pdfUri by viewModel.pdfUri.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val message by viewModel.message.collectAsState()
    val canGenerate by viewModel.canGenerate.collectAsState()
    val selectedClient by viewModel.selectedClient.collectAsState()
    val selectedOrderIds by viewModel.selectedOrderIds.collectAsState()
    val ordersForClient by viewModel.ordersForClient.collectAsState()
    val dateFrom by viewModel.dateFrom.collectAsState()
    val dateTo by viewModel.dateTo.collectAsState()
    // Digit states for masked input — armazenam apenas dígitos
    var dateFromDigits by remember { mutableStateOf(DateUtils.displayToDigits(viewModel.dateFrom.value)) }
    var dateToDigits by remember { mutableStateOf(DateUtils.displayToDigits(viewModel.dateTo.value)) }
    // Sincroniza dígitos quando ViewModel limpa os filtros externamente
    LaunchedEffect(dateFrom) {
        val d = DateUtils.displayToDigits(dateFrom)
        if (dateFromDigits != d) dateFromDigits = d
    }
    LaunchedEffect(dateTo) {
        val d = DateUtils.displayToDigits(dateTo)
        if (dateToDigits != d) dateToDigits = d
    }
    val clientSearch by viewModel.clientSearch.collectAsState()
    val filteredClients by viewModel.filteredClients.collectAsState()

    var showClientPanel by remember { mutableStateOf(false) }
    var showOrderPanel by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Abre PDF quando pronto
    LaunchedEffect(pdfUri) {
        pdfUri?.let { uri ->
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(Intent.createChooser(intent, "Abrir relatório PDF"))
            viewModel.clearPdfUri()
        }
    }

    LaunchedEffect(message) {
        message?.let { snackbarHostState.showSnackbar(it); viewModel.clearMessage() }
    }

    // DatePickers
    val cal = Calendar.getInstance()
    val fromPicker = DatePickerDialog(context, { _, y, m, d ->
        val dStr = "${d.toString().padStart(2,'0')}/${(m+1).toString().padStart(2,'0')}/$y"
        dateFromDigits = DateUtils.displayToDigits(dStr)
        viewModel.setDateFrom(dStr)
    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))

    val toPicker = DatePickerDialog(context, { _, y, m, d ->
        val dStr = "${d.toString().padStart(2,'0')}/${(m+1).toString().padStart(2,'0')}/$y"
        dateToDigits = DateUtils.displayToDigits(dStr)
        viewModel.setDateTo(dStr)
    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Relatórios", fontWeight = FontWeight.Bold) },
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
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Instrução ──────────────────────────────────────────────
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Info, null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "Preencha pelo menos um filtro. Cada OS ocupará uma página no PDF.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // ── Filtro: Cliente ────────────────────────────────────────
            FilterCard(
                title = "Cliente",
                icon = Icons.Default.PeopleAlt,
                summary = selectedClient?.name ?: "Nenhum selecionado",
                hasValue = selectedClient != null,
                onClear = { viewModel.selectClient(null); viewModel.setClientSearch("") },
                onClick = { showClientPanel = !showClientPanel }
            )

            AnimatedVisibility(visible = showClientPanel) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        // Campo de busca
                        OutlinedTextField(
                            value = clientSearch,
                            onValueChange = { viewModel.setClientSearch(it) },
                            label = { Text("Buscar cliente") },
                            leadingIcon = { Icon(Icons.Default.Search, null) },
                            trailingIcon = {
                                if (clientSearch.isNotEmpty())
                                    IconButton(onClick = { viewModel.setClientSearch("") }) {
                                        Icon(Icons.Default.Clear, null)
                                    }
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))

                        if (filteredClients.isEmpty()) {
                            Text("Nenhum cliente encontrado", style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(8.dp))
                        } else {
                            filteredClients.forEach { client ->
                                val isSelected = selectedClient?.id == client.id
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.selectClient(if (isSelected) null else client)
                                            if (!isSelected) showClientPanel = false
                                        }
                                        .background(
                                            if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                            else MaterialTheme.colorScheme.surface
                                        )
                                        .padding(horizontal = 8.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (isSelected)
                                        Icon(Icons.Default.Check, null, Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                                    else
                                        Spacer(Modifier.size(18.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Column {
                                        Text(client.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                                        Text(client.cpfCnpj, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }

            // ── Filtro: OS (só habilitado se cliente selecionado) ──────
            FilterCard(
                title = "Ordens de Serviço",
                icon = Icons.Default.Assignment,
                summary = when {
                    selectedClient == null -> "Selecione um cliente primeiro"
                    selectedOrderIds.isEmpty() -> "Todas as OS do cliente (${ordersForClient.size})"
                    else -> "${selectedOrderIds.size} OS selecionada(s)"
                },
                hasValue = selectedOrderIds.isNotEmpty(),
                enabled = selectedClient != null,
                onClear = { /* OS are cleared when client is cleared */ },
                onClick = { if (selectedClient != null) showOrderPanel = !showOrderPanel }
            )

            AnimatedVisibility(visible = showOrderPanel && selectedClient != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Selecione as OS (mais recentes primeiro)",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                            if (selectedOrderIds.isNotEmpty())
                                TextButton(onClick = { ordersForClient.forEach { viewModel.toggleOrder(it.id) } }) {
                                    Text("Limpar")
                                }
                        }
                        Spacer(Modifier.height(4.dp))

                        if (ordersForClient.isEmpty()) {
                            Text("Nenhuma OS encontrada para este cliente.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(8.dp))
                        } else {
                            ordersForClient.forEach { order ->
                                val checked = order.id in selectedOrderIds
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.toggleOrder(order.id) }
                                        .background(
                                            if (checked) MaterialTheme.colorScheme.primaryContainer
                                            else MaterialTheme.colorScheme.surface
                                        )
                                        .padding(horizontal = 8.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(checked = checked, onCheckedChange = { viewModel.toggleOrder(order.id) })
                                    Spacer(Modifier.width(8.dp))
                                    Column {
                                        Text("OS #${order.id} — ${order.status.displayName}",
                                            style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                                        Text("${order.startDateTime} · ${order.employeeName}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(order.items.joinToString(", ") { it.service.name },
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    }
                                }
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }

            // ── Filtro: Período ────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DateRange, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text("Período", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.weight(1f))
                        if (dateFrom.isNotBlank() || dateTo.isNotBlank()) {
                            TextButton(onClick = { viewModel.setDateFrom(""); viewModel.setDateTo("") }) {
                                Text("Limpar")
                            }
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = dateFromDigits,
                            onValueChange = { val v = DateUtils.filterDateDigits(it); dateFromDigits = v; viewModel.setDateFrom(DateUtils.digitsToDisplay(v)) },
                            visualTransformation = DateMaskTransformation(),
                            label = { Text("De") },
                            placeholder = { Text("DD/MM/AAAA") },
                            leadingIcon = {
                                IconButton(onClick = { fromPicker.show() }) {
                                    Icon(Icons.Default.CalendarToday, null, tint = MaterialTheme.colorScheme.primary)
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = dateToDigits,
                            onValueChange = { val v = DateUtils.filterDateDigits(it); dateToDigits = v; viewModel.setDateTo(DateUtils.digitsToDisplay(v)) },
                            visualTransformation = DateMaskTransformation(),
                            label = { Text("Até") },
                            placeholder = { Text("DD/MM/AAAA") },
                            leadingIcon = {
                                IconButton(onClick = { toPicker.show() }) {
                                    Icon(Icons.Default.CalendarToday, null, tint = MaterialTheme.colorScheme.primary)
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Text("Hora inicial: 00:00:00 · Hora final: 23:59:59 (automático)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // ── Botão Gerar ────────────────────────────────────────────
            Button(
                onClick = { viewModel.generateReport(context) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                enabled = canGenerate && !isGenerating,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                    Spacer(Modifier.width(10.dp))
                    Text("Gerando PDF...")
                } else {
                    Icon(Icons.Default.PictureAsPdf, null)
                    Spacer(Modifier.width(10.dp))
                    Text("Gerar Relatório PDF", fontWeight = FontWeight.Bold)
                }
            }

            // Botão limpar tudo
            if (canGenerate) {
                TextButton(onClick = { viewModel.clearAll() }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.ClearAll, null, Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Limpar todos os filtros")
                }
            }
        }
    }
}

// ── Componente de card de filtro reutilizável ────────────────────────────────
@Composable
private fun FilterCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    summary: String,
    hasValue: Boolean,
    enabled: Boolean = true,
    onClear: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(enabled = enabled, onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = when {
                !enabled -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                hasValue -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icon, null,
                tint = if (enabled) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.labelMedium,
                    color = if (enabled) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                Text(summary, style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (hasValue) FontWeight.Bold else FontWeight.Normal,
                    color = if (!enabled) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    else if (hasValue) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            if (hasValue) {
                IconButton(onClick = onClear, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Close, null, Modifier.size(16.dp))
                }
            } else {
                Icon(Icons.Default.ChevronRight, null,
                    tint = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant
                    else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
            }
        }
    }
}