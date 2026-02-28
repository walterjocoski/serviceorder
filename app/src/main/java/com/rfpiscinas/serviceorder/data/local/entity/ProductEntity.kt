package com.rfpiscinas.serviceorder.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rfpiscinas.serviceorder.data.model.Product
import com.rfpiscinas.serviceorder.data.model.UnitMeasure

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val unitMeasure: UnitMeasure,
    val active: Boolean = true
) {
    fun toModel() = Product(
        id = id,
        name = name,
        unitMeasure = unitMeasure,
        active = active
    )

    companion object {
        fun fromModel(model: Product) = ProductEntity(
            id = model.id,
            name = model.name,
            unitMeasure = model.unitMeasure,
            active = model.active
        )
    }
}
