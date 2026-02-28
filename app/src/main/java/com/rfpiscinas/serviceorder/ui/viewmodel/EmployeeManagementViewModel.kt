package com.rfpiscinas.serviceorder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rfpiscinas.serviceorder.data.model.User
import com.rfpiscinas.serviceorder.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeManagementViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _employees = MutableStateFlow<List<User>>(emptyList())
    val employees: StateFlow<List<User>> = _employees.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getEmployees().collect { _employees.value = it }
        }
    }

    fun addEmployee(user: User) {
        viewModelScope.launch {
            userRepository.insert(user)
            _message.value = "Funcionário cadastrado com sucesso!"
        }
    }

    fun updateEmployee(user: User) {
        viewModelScope.launch {
            userRepository.update(user)
            _message.value = "Funcionário atualizado!"
        }
    }

    fun clearMessage() { _message.value = null }
}
