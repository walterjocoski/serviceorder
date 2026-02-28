package com.rfpiscinas.serviceorder.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Client(
    val id: Long,
    val name: String,
    val cpfCnpj: String,
    val address: String,
    val phone: String,
    val email: String,
    val active: Boolean = true
)
