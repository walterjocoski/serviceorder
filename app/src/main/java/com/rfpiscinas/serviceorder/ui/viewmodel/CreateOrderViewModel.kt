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
class CreateOrderViewModel @Inject constructor(
    private val serviceOrderRepository: ServiceOrderRepository,
    private val clientRepository: ClientRepository,
    private val serviceRepository: ServiceRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _clients = MutableStateFlow<List<Client>>(emptyList())
    val clients: StateFlow<List<Client>> = _clients.asStateFlow()

    private val _services = MutableStateFlow<List<Service>>(emptyList())
    val services: StateFlow<List<Service>> = _services.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _orderCreated = MutableStateFlow(false)
    val orderCreated: StateFlow<Boolean> = _orderCreated.asStateFlow()

    init {
        viewModelScope.launch {
            clientRepository.getActiveClients().collect { _clients.value = it }
        }
        viewModelScope.launch {
            serviceRepository.getActiveServices().collect { _services.value = it }
        }
        viewModelScope.launch {
            productRepository.getActiveProducts().collect { _products.value = it }
        }
    }

    fun createOrder(
        client: Client,
        employeeId: Long,
        employeeName: String,
        items: List<ServiceOrderItem>
    ) {
        viewModelScope.launch {
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val order = ServiceOrder(
                id = 0,
                clientId = client.id,
                clientName = client.name,
                clientAddress = client.address,
                employeeId = employeeId,
                employeeName = employeeName,
                status = OrderStatus.COMPLETED,
                startDateTime = now.format(formatter),
                endDateTime = now.format(formatter),
                items = items,
                synced = false
            )
            serviceOrderRepository.insertOrder(order)
            _orderCreated.value = true
        }
    }
}
