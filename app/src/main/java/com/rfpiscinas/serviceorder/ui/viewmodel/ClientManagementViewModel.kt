package com.rfpiscinas.serviceorder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rfpiscinas.serviceorder.data.model.Client
import com.rfpiscinas.serviceorder.data.repository.ClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientManagementViewModel @Inject constructor(
    private val clientRepository: ClientRepository
) : ViewModel() {

    private val _clients = MutableStateFlow<List<Client>>(emptyList())
    val clients: StateFlow<List<Client>> = _clients.asStateFlow()

    init {
        viewModelScope.launch {
            clientRepository.getAll().collect { _clients.value = it }
        }
    }

    fun addClient(client: Client) {
        viewModelScope.launch {
            clientRepository.insert(client)
        }
    }

    fun updateClient(client: Client) {
        viewModelScope.launch {
            clientRepository.update(client)
        }
    }

    fun deleteClient(client: Client) {
        viewModelScope.launch {
            clientRepository.delete(client)
        }
    }
}
