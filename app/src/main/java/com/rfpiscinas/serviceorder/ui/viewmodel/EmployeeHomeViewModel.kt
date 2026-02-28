package com.rfpiscinas.serviceorder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rfpiscinas.serviceorder.data.model.OrderStatus
import com.rfpiscinas.serviceorder.data.model.ServiceOrder
import com.rfpiscinas.serviceorder.data.repository.ServiceOrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeHomeViewModel @Inject constructor(
    private val serviceOrderRepository: ServiceOrderRepository
) : ViewModel() {

    private val _orders = MutableStateFlow<List<ServiceOrder>>(emptyList())
    val orders: StateFlow<List<ServiceOrder>> = _orders.asStateFlow()

    private val _actionMessage = MutableStateFlow<String?>(null)
    val actionMessage: StateFlow<String?> = _actionMessage.asStateFlow()

    fun loadOrdersForEmployee(employeeId: Long) {
        viewModelScope.launch {
            serviceOrderRepository.getOrdersByEmployee(employeeId).collect {
                _orders.value = it
            }
        }
    }

    fun startOrder(orderId: Long) {
        viewModelScope.launch {
            serviceOrderRepository.updateOrderStatus(orderId, OrderStatus.IN_PROGRESS)
            _actionMessage.value = "Atendimento iniciado!"
        }
    }

    fun cancelOrder(order: ServiceOrder) {
        val canCancel = order.status == OrderStatus.CREATED ||
                (order.status == OrderStatus.IN_PROGRESS && order.items.isEmpty())
        if (!canCancel) {
            _actionMessage.value = "Não é possível cancelar: já existem serviços lançados."
            return
        }
        viewModelScope.launch {
            serviceOrderRepository.cancelOrder(order.id)
            _actionMessage.value = "OS #${order.id} cancelada."
        }
    }

    fun clearActionMessage() {
        _actionMessage.value = null
    }
}
