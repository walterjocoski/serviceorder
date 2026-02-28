package com.rfpiscinas.serviceorder.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rfpiscinas.serviceorder.data.model.Service

@Entity(tableName = "services")
data class ServiceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val type: String,
    val usesProducts: Boolean,
    val active: Boolean = true
) {
    fun toModel() = Service(
        id = id,
        name = name,
        type = type,
        usesProducts = usesProducts,
        active = active
    )

    companion object {
        fun fromModel(model: Service) = ServiceEntity(
            id = model.id,
            name = model.name,
            type = model.type,
            usesProducts = model.usesProducts,
            active = model.active
        )
    }
}
