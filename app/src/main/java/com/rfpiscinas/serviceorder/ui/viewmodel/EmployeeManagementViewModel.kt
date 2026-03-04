package com.rfpiscinas.serviceorder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rfpiscinas.serviceorder.data.model.User
import com.rfpiscinas.serviceorder.data.repository.UserRepository
import com.rfpiscinas.serviceorder.util.PasswordUtils
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

    /**
     * Insere novo funcionário.
     * [plainPassword] é a senha em texto plano — será hasheada aqui.
     * Valida duplicidade de e-mail antes de inserir.
     */
    fun addEmployee(user: User, plainPassword: String) {
        viewModelScope.launch {
            if (userRepository.isEmailTaken(user.email)) {
                _message.value = "Já existe um usuário com este e-mail."
                return@launch
            }
            val hashed = user.copy(passwordHash = PasswordUtils.hash(plainPassword))
            userRepository.insert(hashed)
            _message.value = "Funcionário cadastrado com sucesso!"
        }
    }

    /**
     * Atualiza funcionário.
     * Se [newPlainPassword] não for vazio, a senha é atualizada.
     * Se vazio, mantém o hash atual (passado em [user.passwordHash]).
     */
    fun updateEmployee(user: User, newPlainPassword: String = "") {
        viewModelScope.launch {
            // Verifica e-mail duplicado excluindo o próprio usuário
            if (userRepository.isEmailTaken(user.email, excludeId = user.id)) {
                _message.value = "Já existe outro usuário com este e-mail."
                return@launch
            }
            val toSave = if (newPlainPassword.isNotBlank())
                user.copy(passwordHash = PasswordUtils.hash(newPlainPassword))
            else
                user
            userRepository.update(toSave)
            _message.value = "Funcionário atualizado!"
        }
    }

    fun clearMessage() { _message.value = null }
}
