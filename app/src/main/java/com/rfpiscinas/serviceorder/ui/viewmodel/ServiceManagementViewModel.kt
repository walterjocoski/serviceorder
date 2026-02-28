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

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    init {
        viewModelScope.launch {
            serviceRepository.getAll().collect { _services.value = it }
        }
    }

    fun addService(service: Service) {
        viewModelScope.launch {
            serviceRepository.insert(service)
            _message.value = "Serviço cadastrado com sucesso!"
        }
    }

    fun updateService(service: Service) {
        viewModelScope.launch {
            serviceRepository.update(service)
            _message.value = "Serviço atualizado!"
        }
    }

    fun toggleActive(service: Service) {
        viewModelScope.launch {
            serviceRepository.update(service.copy(active = !service.active))
            val status = if (!service.active) "ativado" else "inativado"
            _message.value = "Serviço $status!"
        }
    }

    fun clearMessage() { _message.value = null }
}
