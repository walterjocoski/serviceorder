package com.rfpiscinas.serviceorder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rfpiscinas.serviceorder.data.model.*
import com.rfpiscinas.serviceorder.data.repository.ClientRepository
import com.rfpiscinas.serviceorder.data.repository.ProductRepository
import com.rfpiscinas.serviceorder.data.repository.ServiceOrderRepository
import com.rfpiscinas.serviceorder.data.repository.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

    private val _activeClients = MutableStateFlow<List<Client>>(emptyList())
    val activeClients: StateFlow<List<Client>> = _activeClients.asStateFlow()

    private val _orderCreatedId = MutableStateFlow<Long?>(null)
    val orderCreatedId: StateFlow<Long?> = _orderCreatedId.asStateFlow()

    private val _orderFinalized = MutableStateFlow(false)
    val orderFinalized: StateFlow<Boolean> = _orderFinalized.asStateFlow()

    init {
        viewModelScope.launch {
            serviceRepository.getActiveServices().collect { _services.value = it }
        }
        viewModelScope.launch {
            productRepository.getActiveProducts().collect { _products.value = it }
        }
        viewModelScope.launch {
            clientRepository.getActiveClients().collect { _activeClients.value = it }
        }
    }

    fun loadClient(clientId: Long) {
        viewModelScope.launch {
            _client.value = clientRepository.getById(clientId)
        }
    }

    // Cria a OS com status IN_PROGRESS (prestador iniciou na hora)
    fun createAndStartOrder(
        client: Client,
        employeeId: Long,
        employeeName: String,
        startDateTime: String
    ) {
        viewModelScope.launch {
            val order = ServiceOrder(
                id = 0,
                clientId = client.id,
                clientName = client.name,
                clientAddress = client.address,
                employeeId = employeeId,
                employeeName = employeeName,
                status = OrderStatus.IN_PROGRESS,
                startDateTime = startDateTime,
                endDateTime = null,
                items = emptyList(),
                synced = false
            )
            val newId = serviceOrderRepository.insertOrder(order)
            _orderCreatedId.value = newId
        }
    }

    // Finaliza a OS: salva os serviços e marca como COMPLETED
    fun finalizeOrder(orderId: Long, items: List<ServiceOrderItem>) {
        viewModelScope.launch {
            val endDateTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            serviceOrderRepository.finalizeOrder(orderId, endDateTime, items)
            _orderFinalized.value = true
        }
    }

    fun resetState() {
        _orderCreatedId.value = null
        _orderFinalized.value = false
    }
}
