package com.rfpiscinas.serviceorder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rfpiscinas.serviceorder.data.model.Service
import com.rfpiscinas.serviceorder.data.repository.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceManagementViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository
) : ViewModel() {

    private val _services = MutableStateFlow<List<Service>>(emptyList())
    val services: StateFlow<List<Service>> = _services.asStateFlow()

    init {
        viewModelScope.launch {
            serviceRepository.getAll().collect { _services.value = it }
        }
    }

    fun addService(service: Service) {
        viewModelScope.launch {
            serviceRepository.insert(service)
        }
    }

    fun updateService(service: Service) {
        viewModelScope.launch {
            serviceRepository.update(service)
        }
    }

    fun deleteService(service: Service) {
        viewModelScope.launch {
            serviceRepository.delete(service)
        }
    }
}
