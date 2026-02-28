package com.rfpiscinas.serviceorder.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rfpiscinas.serviceorder.data.model.UnitMeasure

@Entity(
    tableName = "service_order_products",
    foreignKeys = [
        ForeignKey(
            entity = ServiceOrderItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["serviceOrderItemId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("serviceOrderItemId"),
        Index("productId")
    ]
)
data class ServiceOrderProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val serviceOrderItemId: Long,
    val productId: Long,
    val productName: String,
    val productUnitMeasure: UnitMeasure,
    val quantity: Double
)
