package com.rfpiscinas.serviceorder.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    val name: String,
    val email: String,
    val phone: String = "",
    val address: String = "",
    val role: UserRole,
    val active: Boolean = true,
    val startDate: String = "" // formato: yyyy-MM-dd
)

enum class UserRole {
    ADMIN,
    MANAGER,
    EMPLOYEE,
    CLIENT
}
