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
class AllOrdersViewModel @Inject constructor(
    private val serviceOrderRepository: ServiceOrderRepository
) : ViewModel() {

    private val _allOrders = MutableStateFlow<List<ServiceOrder>>(emptyList())
    val allOrders: StateFlow<List<ServiceOrder>> = _allOrders.asStateFlow()

    private val _employeeNames = MutableStateFlow<List<String>>(emptyList())
    val employeeNames: StateFlow<List<String>> = _employeeNames.asStateFlow()

    private val _selectedStatus = MutableStateFlow<OrderStatus?>(null)
    val selectedStatus: StateFlow<OrderStatus?> = _selectedStatus.asStateFlow()

    private val _selectedEmployee = MutableStateFlow<String?>(null)
    val selectedEmployee: StateFlow<String?> = _selectedEmployee.asStateFlow()

    init {
        viewModelScope.launch {
            serviceOrderRepository.getAllOrders().collect { _allOrders.value = it }
        }
        viewModelScope.launch {
            serviceOrderRepository.getDistinctEmployeeNames().collect { _employeeNames.value = it }
        }
    }

    fun setStatusFilter(status: OrderStatus?) {
        _selectedStatus.value = status
    }

    fun setEmployeeFilter(employeeName: String?) {
        _selectedEmployee.value = employeeName
    }

    fun getFilteredOrders(): List<ServiceOrder> {
        var orders = _allOrders.value
        _selectedStatus.value?.let { status ->
            orders = orders.filter { it.status == status }
        }
        _selectedEmployee.value?.let { employee ->
            orders = orders.filter { it.employeeName == employee }
        }
        return orders
    }
}
