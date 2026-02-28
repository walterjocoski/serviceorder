package com.rfpiscinas.serviceorder.data.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ServiceOrder(
    val id: Long,
    val clientId: Long,
    val clientName: String,
    val clientAddress: String,
    val employeeId: Long,
    val employeeName: String, // Primeiro nome do prestador
    val status: OrderStatus,
    val startDateTime: String, // formato: yyyy-MM-dd HH:mm
    val endDateTime: String? = null,
    val items: List<ServiceOrderItem> = emptyList(),
    val synced: Boolean = false
)

@Serializable
data class ServiceOrderItem(
    val id: Long = 0,
    val service: Service,
    val observations: String = "",
    val products: List<ServiceOrderProduct> = emptyList()
)

@Serializable
data class ServiceOrderProduct(
    val id: Long = 0,
    val product: Product,
    val quantity: Double
)

enum class OrderStatus(val displayName: String) {
    CREATED("Criada"),
    IN_PROGRESS("Em Andamento"),
    COMPLETED("Finalizada"),
    CANCELLED("Cancelada")
}
