package com.rfpiscinas.serviceorder.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Long = 0,
    val name: String,
    val unitMeasure: UnitMeasure,
    val active: Boolean = true
)

enum class UnitMeasure(val displayName: String, val abbreviation: String) {
    GRAMS("Gramas", "g"),
    KG("Quilogramas", "kg"),
    ML("Mililitros", "ml"),
    LITERS("Litros", "L"),
    UNITS("Unidades", "un")
}
