package com.rfpiscinas.serviceorder.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "service_order_items",
    foreignKeys = [
        ForeignKey(
            entity = ServiceOrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["serviceOrderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ServiceEntity::class,
            parentColumns = ["id"],
            childColumns = ["serviceId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("serviceOrderId"),
        Index("serviceId")
    ]
)
data class ServiceOrderItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val serviceOrderId: Long,
    val serviceId: Long,
    val serviceName: String,
    val serviceType: String,
    val serviceUsesProducts: Boolean,
    val observations: String = ""
)
