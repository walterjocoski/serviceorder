package com.rfpiscinas.serviceorder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rfpiscinas.serviceorder.data.model.User
import com.rfpiscinas.serviceorder.data.repository.UserRepository
import com.rfpiscinas.serviceorder.util.PasswordUtils
import com.rfpiscinas.serviceorder.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loginResult = MutableStateFlow<User?>(null)
    val loginResult: StateFlow<User?> = _loginResult.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /** Verifica se há sessão ativa ao iniciar o app */
    fun checkExistingSession(): Long = sessionManager.getUserId()

    fun getSessionRole() = sessionManager.getUserRole()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _errorMessage.value = "Preencha e-mail e senha"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val user = userRepository.getByEmail(email.lowercase().trim())
                when {
                    user == null -> _errorMessage.value = "E-mail não encontrado"
                    !user.active -> _errorMessage.value = "Usuário inativo. Contate o gerente."
                    !PasswordUtils.verify(password, user.passwordHash) ->
                        _errorMessage.value = "Senha incorreta"
                    else -> {
                        sessionManager.saveSession(user.id, user.name, user.email, user.role)
                        _loginResult.value = user
                    }
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() { _errorMessage.value = null }
}
