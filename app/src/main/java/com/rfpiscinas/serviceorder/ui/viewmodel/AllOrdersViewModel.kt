package com.rfpiscinas.serviceorder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rfpiscinas.serviceorder.data.model.OrderStatus
import com.rfpiscinas.serviceorder.data.model.ServiceOrder
import com.rfpiscinas.serviceorder.data.repository.ServiceOrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// Data class auxiliar para agrupar os 5 filtros em um único objeto
private data class Filters(
    val status: OrderStatus?,
    val employee: String?,
    val client: String?,
    val from: String?,
    val to: String?
)

@HiltViewModel
class AllOrdersViewModel @Inject constructor(
    private val serviceOrderRepository: ServiceOrderRepository
) : ViewModel() {

    private val _allOrders = MutableStateFlow<List<ServiceOrder>>(emptyList())

    private val _selectedStatus = MutableStateFlow<OrderStatus?>(null)
    val selectedStatus: StateFlow<OrderStatus?> = _selectedStatus.asStateFlow()

    private val _selectedEmployee = MutableStateFlow<String?>(null)
    val selectedEmployee: StateFlow<String?> = _selectedEmployee.asStateFlow()

    private val _selectedClient = MutableStateFlow<String?>(null)
    val selectedClient: StateFlow<String?> = _selectedClient.asStateFlow()

    private val _dateFrom = MutableStateFlow<String?>(null)
    val dateFrom: StateFlow<String?> = _dateFrom.asStateFlow()

    private val _dateTo = MutableStateFlow<String?>(null)
    val dateTo: StateFlow<String?> = _dateTo.asStateFlow()

    val employeeNames = MutableStateFlow<List<String>>(emptyList())
    val clientNames = MutableStateFlow<List<String>>(emptyList())

    // combine suporta no máximo 5 flows — agrupamos os filtros num objeto intermediário
    private val _filters: Flow<Filters> = combine(
        _selectedStatus, _selectedEmployee, _selectedClient, _dateFrom, _dateTo
    ) { status, employee, client, from, to ->
        Filters(status, employee, client, from, to)
    }

    val filteredOrders: StateFlow<List<ServiceOrder>> = combine(
        _allOrders, _filters
    ) { orders, f ->
        orders
            .let { list -> if (f.status != null) list.filter { it.status == f.status } else list }
            .let { list -> if (f.employee != null) list.filter { it.employeeName == f.employee } else list }
            .let { list -> if (f.client != null) list.filter { it.clientName == f.client } else list }
            .let { list -> if (f.from != null) list.filter { it.startDateTime >= f.from } else list }
            .let { list -> if (f.to != null) list.filter { it.startDateTime <= "${f.to} 23:59" } else list }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val hasActiveFilters: StateFlow<Boolean> = _filters.map { f ->
        f.status != null || f.employee != null || f.client != null || f.from != null || f.to != null
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        viewModelScope.launch {
            serviceOrderRepository.getAllOrders().collect { _allOrders.value = it }
        }
        viewModelScope.launch {
            serviceOrderRepository.getDistinctEmployeeNames().collect { employeeNames.value = it }
        }
        viewModelScope.launch {
            serviceOrderRepository.getDistinctClientNames().collect { clientNames.value = it }
        }
    }

    fun setStatusFilter(status: OrderStatus?) { _selectedStatus.value = status }
    fun setEmployeeFilter(name: String?) { _selectedEmployee.value = name }
    fun setClientFilter(name: String?) { _selectedClient.value = name }
    fun setDateFrom(date: String?) { _dateFrom.value = date }
    fun setDateTo(date: String?) { _dateTo.value = date }

    fun clearAllFilters() {
        _selectedStatus.value = null
        _selectedEmployee.value = null
        _selectedClient.value = null
        _dateFrom.value = null
        _dateTo.value = null
    }
}