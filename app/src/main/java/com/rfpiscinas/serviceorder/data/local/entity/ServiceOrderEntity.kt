package com.rfpiscinas.serviceorder.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rfpiscinas.serviceorder.data.model.OrderStatus

@Entity(
    tableName = "service_orders",
    foreignKeys = [
        ForeignKey(
            entity = ClientEntity::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["employeeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("clientId"),
        Index("employeeId")
    ]
)
data class ServiceOrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val clientId: Long,
    val clientName: String,
    val clientAddress: String,
    val employeeId: Long,
    val employeeName: String,
    val status: OrderStatus,
    val startDateTime: String,
    val endDateTime: String? = null,
    val synced: Boolean = false
)
