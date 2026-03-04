package com.rfpiscinas.serviceorder.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    val name: String,
    val email: String,
    val passwordHash: String = "",   // SHA-256 hex; vazio = sem senha definida (legado)
    val phone: String = "",
    val address: String = "",
    val role: UserRole,
    val active: Boolean = true,
    val startDate: String = ""       // formato: DD/MM/YYYY
)

enum class UserRole {
    MASTER,     // Walter — acesso total
    MANAGER,    // Gerente / dono
    EMPLOYEE,   // Funcionário de campo
    CLIENT      // Reservado para futuro
}
