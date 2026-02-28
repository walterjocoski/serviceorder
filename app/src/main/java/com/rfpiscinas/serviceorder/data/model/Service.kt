package com.rfpiscinas.serviceorder.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Service(
    val id: Long = 0,
    val name: String,
    val type: String,
    val usesProducts: Boolean,
    val active: Boolean = true
)
