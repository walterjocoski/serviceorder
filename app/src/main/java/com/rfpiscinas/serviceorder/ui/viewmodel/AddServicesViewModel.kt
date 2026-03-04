package com.rfpiscinas.serviceorder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rfpiscinas.serviceorder.data.model.*
import com.rfpiscinas.serviceorder.data.repository.ClientRepository
import com.rfpiscinas.serviceorder.data.repository.ProductRepository
import com.rfpiscinas.serviceorder.data.repository.ServiceOrderRepository
import com.rfpiscinas.serviceorder.data.repository.ServiceRepository
import com.rfpiscinas.serviceorder.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddServicesViewModel @Inject constructor(
    private val clientRepository: ClientRepository,
    private val serviceRepository: ServiceRepository,
    private val productRepository: ProductRepository,
    private val serviceOrderRepository: ServiceOrderRepository
) : ViewModel() {

    private val _client = MutableStateFlow<Client?>(null)
    val client: StateFlow<Client?> = _client.asStateFlow()

    private val _services = MutableStateFlow<List<Service>>(emptyList())
    val services: StateFlow<List<Service>> = _services.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _orderFinalized = MutableStateFlow(false)
    val orderFinalized: StateFlow<Boolean> = _orderFinalized.asStateFlow()

    // OS carregada — usada pela tela para saber status e data de início
    private val _order = MutableStateFlow<ServiceOrder?>(null)
    val order: StateFlow<ServiceOrder?> = _order.asStateFlow()

    // Sinaliza que a OS foi carregada e a tela pode inicializar os itens.
    // Separado de _order para evitar race condition na inicialização da lista.
    private val _orderLoaded = MutableStateFlow(false)
    val orderLoaded: StateFlow<Boolean> = _orderLoaded.asStateFlow()

    // Usado por CreateOrderInitialScreen para listar clientes ativos
    private val _activeClients = MutableStateFlow<List<Client>>(emptyList())
    val activeClients: StateFlow<List<Client>> = _activeClients.asStateFlow()

    // ID da OS recém-criada — CreateOrderInitialScreen navega para AddServices ao receber
    private val _orderCreatedId = MutableStateFlow<Long?>(null)
    val orderCreatedId: StateFlow<Long?> = _orderCreatedId.asStateFlow()

    init {
        viewModelScope.launch { serviceRepository.getActiveServices().collect { _services.value = it } }
        viewModelScope.launch { productRepository.getActiveProducts().collect { _products.value = it } }
        viewModelScope.launch { clientRepository.getActiveClients().collect { _activeClients.value = it } }
    }

    /** Cria e inicia uma OS nova — usado por CreateOrderInitialScreen. */
    fun createAndStartOrder(client: Client, employeeId: Long, employeeName: String, startDateTime: String) {
        viewModelScope.launch {
            val order = ServiceOrder(
                id = 0, clientId = client.id, clientName = client.name,
                clientAddress = client.address, employeeId = employeeId,
                employeeName = employeeName, status = OrderStatus.IN_PROGRESS,
                startDateTime = startDateTime, endDateTime = null,
                items = emptyList(), synced = false
            )
            _orderCreatedId.value = serviceOrderRepository.insertOrder(order)
        }
    }

    /** Carrega a OS completa — seta _order e sinaliza _orderLoaded. */
    fun loadOrder(orderId: Long) {
        viewModelScope.launch {
            val os = serviceOrderRepository.getOrderById(orderId) ?: return@launch
            _order.value = os
            _client.value = clientRepository.getById(os.clientId)
            _orderLoaded.value = true   // sinaliza DEPOIS de order estar populado
        }
    }

    /** Finaliza a OS: persiste itens e muda status para COMPLETED. */
    fun finalizeOrder(orderId: Long, items: List<ServiceOrderItem>) {
        viewModelScope.launch {
            serviceOrderRepository.finalizeOrder(orderId, DateUtils.now(), items)
            _orderFinalized.value = true
        }
    }

    /**
     * Salva os itens na OS em andamento SEM finalizar (mantém status IN_PROGRESS).
     * Reutiliza _orderFinalized para disparar a navegação de volta.
     */
    fun saveItemsWithoutFinalizing(orderId: Long, items: List<ServiceOrderItem>) {
        viewModelScope.launch {
            serviceOrderRepository.updateOrderItems(orderId, items)
            _orderFinalized.value = true
        }
    }

    fun resetState() {
        _orderFinalized.value = false
        _orderLoaded.value = false
        _orderCreatedId.value = null
        _order.value = null
        _client.value = null
    }
}