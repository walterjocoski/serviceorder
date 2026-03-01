package com.rfpiscinas.serviceorder.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rfpiscinas.serviceorder.data.model.Client
import com.rfpiscinas.serviceorder.data.model.ServiceOrder
import com.rfpiscinas.serviceorder.data.repository.ClientRepository
import com.rfpiscinas.serviceorder.data.repository.ServiceOrderRepository
import com.rfpiscinas.serviceorder.util.DateUtils
import com.rfpiscinas.serviceorder.util.PdfReportGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val serviceOrderRepository: ServiceOrderRepository,
    private val clientRepository: ClientRepository
) : ViewModel() {

    // ─── Dados base ───────────────────────────────────────────────
    private val _allOrders = MutableStateFlow<List<ServiceOrder>>(emptyList())
    val allOrders: StateFlow<List<ServiceOrder>> = _allOrders.asStateFlow()
    val allClients = MutableStateFlow<List<Client>>(emptyList())
    private val _ordersForClient = MutableStateFlow<List<ServiceOrder>>(emptyList())

    // ─── Filtros ──────────────────────────────────────────────────
    private val _selectedClient = MutableStateFlow<Client?>(null)
    val selectedClient: StateFlow<Client?> = _selectedClient.asStateFlow()

    // OS selecionadas (múltipla escolha)
    private val _selectedOrderIds = MutableStateFlow<Set<Long>>(emptySet())
    val selectedOrderIds: StateFlow<Set<Long>> = _selectedOrderIds.asStateFlow()

    private val _dateFrom = MutableStateFlow("")
    val dateFrom: StateFlow<String> = _dateFrom.asStateFlow()

    private val _dateTo = MutableStateFlow("")
    val dateTo: StateFlow<String> = _dateTo.asStateFlow()

    // Busca de cliente com digitação
    private val _clientSearch = MutableStateFlow("")
    val clientSearch: StateFlow<String> = _clientSearch.asStateFlow()

    // Lista de clientes filtrada pela busca, ordenada alfabeticamente
    val filteredClients: StateFlow<List<Client>> = combine(
        allClients, _clientSearch
    ) { clients, query ->
        if (query.isBlank()) clients.sortedBy { it.name }
        else clients.filter { it.name.contains(query, ignoreCase = true) }.sortedBy { it.name }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // OS disponíveis para o cliente selecionado (mais nova → mais antiga)
    val ordersForClient: StateFlow<List<ServiceOrder>> = _ordersForClient.asStateFlow()

    // ─── Validação ────────────────────────────────────────────────
    // O botão gerar é habilitado se pelo menos um filtro principal foi preenchido
    val canGenerate: StateFlow<Boolean> = combine(
        _selectedClient, _dateFrom, _dateTo
    ) { client, from, to ->
        client != null || from.isNotBlank() || to.isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // ─── Estado de geração ────────────────────────────────────────
    private val _pdfUri = MutableStateFlow<Uri?>(null)
    val pdfUri: StateFlow<Uri?> = _pdfUri.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    init {
        viewModelScope.launch {
            serviceOrderRepository.getAllOrders().collect { _allOrders.value = it }
        }
        viewModelScope.launch {
            clientRepository.getAll().collect { allClients.value = it.filter { c -> c.active } }
        }
    }

    fun setClientSearch(query: String) {
        _clientSearch.value = query
    }

    fun selectClient(client: Client?) {
        _selectedClient.value = client
        _selectedOrderIds.value = emptySet() // limpa OS ao trocar cliente
        _ordersForClient.value = emptyList()
        if (client != null) {
            viewModelScope.launch {
                serviceOrderRepository.getOrdersByClient(client.id)
                    .collect { orders ->
                        // mais nova (maior id) primeiro
                        _ordersForClient.value = orders.sortedByDescending { it.id }
                    }
            }
        }
    }

    fun toggleOrder(orderId: Long) {
        val current = _selectedOrderIds.value.toMutableSet()
        if (current.contains(orderId)) current.remove(orderId) else current.add(orderId)
        _selectedOrderIds.value = current
    }

    fun setDateFrom(date: String) { _dateFrom.value = date }
    fun setDateTo(date: String) { _dateTo.value = date }

    fun clearAll() {
        _selectedClient.value = null
        _selectedOrderIds.value = emptySet()
        _ordersForClient.value = emptyList()
        _dateFrom.value = ""
        _dateTo.value = ""
        _clientSearch.value = ""
    }

    fun clearMessage() { _message.value = null }
    fun clearPdfUri() { _pdfUri.value = null }

    /**
     * Resolve quais OS devem entrar no relatório conforme regras de negócio:
     *
     * - Se cliente + OS selecionadas → apenas essas OS
     * - Se cliente + período          → OS do cliente no período
     * - Se cliente só                 → todas as OS do cliente
     * - Se apenas período             → todas as OS do sistema no período
     */
    fun generateReport(context: Context) {
        viewModelScope.launch {
            _isGenerating.value = true
            _message.value = null
            try {
                val client = _selectedClient.value
                val selectedIds = _selectedOrderIds.value
                val from = _dateFrom.value.trim()
                val to = _dateTo.value.trim()

                // Pool inicial: do cliente ou de todas
                var pool: List<ServiceOrder> = if (client != null) {
                    _ordersForClient.value
                } else {
                    _allOrders.value
                }

                // Filtro de OS específicas (só aplica se cliente estiver selecionado e houver seleção)
                if (client != null && selectedIds.isNotEmpty()) {
                    pool = pool.filter { it.id in selectedIds }
                }

                // Filtro de período
                if (from.isNotBlank()) {
                    val fromDt = DateUtils.startOfDay(from)
                    pool = pool.filter { DateUtils.isAfterOrEqual(it.startDateTime, fromDt) }
                }
                if (to.isNotBlank()) {
                    val toDt = DateUtils.endOfDay(to)
                    pool = pool.filter { DateUtils.isBeforeOrEqual(it.startDateTime, toDt) }
                }

                if (pool.isEmpty()) {
                    _message.value = "Nenhuma OS encontrada para os filtros selecionados."
                    return@launch
                }

                // Ordena da mais nova para a mais antiga no PDF
                val sorted = pool.sortedByDescending { it.id }

                val uri = PdfReportGenerator.generateMultiOrderReport(context, sorted)
                _pdfUri.value = uri
            } catch (e: Exception) {
                _message.value = "Erro ao gerar relatório: ${e.message}"
            } finally {
                _isGenerating.value = false
            }
        }
    }
}
