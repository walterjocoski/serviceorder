package com.rfpiscinas.serviceorder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rfpiscinas.serviceorder.data.model.Client
import com.rfpiscinas.serviceorder.data.repository.ClientRepository
import com.rfpiscinas.serviceorder.util.CpfCnpjUtils
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

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    // Erros de duplicidade vindos do banco — exibidos no formulário
    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _cpfCnpjError = MutableStateFlow<String?>(null)
    val cpfCnpjError: StateFlow<String?> = _cpfCnpjError.asStateFlow()

    init {
        viewModelScope.launch {
            clientRepository.getAll().collect { _clients.value = it }
        }
    }

    fun addClient(client: Client, cpfCnpjDigits: String) {
        viewModelScope.launch {
            // Valida duplicidade antes de salvar
            if (clientRepository.isEmailTaken(client.email)) {
                _emailError.value = "Este e-mail já está cadastrado"
                return@launch
            }
            if (clientRepository.isCpfCnpjTaken(cpfCnpjDigits)) {
                _cpfCnpjError.value = "Este CPF/CNPJ já está cadastrado"
                return@launch
            }
            clientRepository.insert(client)
            clearErrors()
            _message.value = "Cliente cadastrado com sucesso!"
        }
    }

    fun updateClient(client: Client, cpfCnpjDigits: String) {
        viewModelScope.launch {
            // Exclui o próprio cliente da verificação de duplicidade
            if (clientRepository.isEmailTaken(client.email, excludeId = client.id)) {
                _emailError.value = "Este e-mail já está cadastrado por outro cliente"
                return@launch
            }
            if (clientRepository.isCpfCnpjTaken(cpfCnpjDigits, excludeId = client.id)) {
                _cpfCnpjError.value = "Este CPF/CNPJ já está cadastrado por outro cliente"
                return@launch
            }
            clientRepository.update(client)
            clearErrors()
            _message.value = "Cliente atualizado!"
        }
    }

    // Toggle ativo/inativo não precisa de validação
    fun toggleActive(client: Client) {
        viewModelScope.launch {
            clientRepository.update(client.copy(active = !client.active))
        }
    }

    fun clearErrors() {
        _emailError.value = null
        _cpfCnpjError.value = null
    }

    fun clearMessage() { _message.value = null }
}