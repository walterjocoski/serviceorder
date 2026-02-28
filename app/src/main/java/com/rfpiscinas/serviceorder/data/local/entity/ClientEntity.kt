package com.rfpiscinas.serviceorder.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rfpiscinas.serviceorder.data.model.Client

@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val cpfCnpj: String,
    val address: String,
    val phone: String,
    val email: String,
    val active: Boolean = true
) {
    fun toModel() = Client(
        id = id,
        name = name,
        cpfCnpj = cpfCnpj,
        address = address,
        phone = phone,
        email = email,
        active = active
    )

    companion object {
        fun fromModel(model: Client) = ClientEntity(
            id = model.id,
            name = model.name,
            cpfCnpj = model.cpfCnpj,
            address = model.address,
            phone = model.phone,
            email = model.email,
            active = model.active
        )
    }
}
