package com.rfpiscinas.serviceorder.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rfpiscinas.serviceorder.data.model.User
import com.rfpiscinas.serviceorder.data.model.UserRole

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val phone: String = "",
    val address: String = "",
    val role: UserRole,
    val active: Boolean = true,
    val startDate: String = ""
) {
    fun toModel() = User(
        id = id,
        name = name,
        email = email,
        phone = phone,
        address = address,
        role = role,
        active = active,
        startDate = startDate
    )

    companion object {
        fun fromModel(model: User) = UserEntity(
            id = model.id,
            name = model.name,
            email = model.email,
            phone = model.phone,
            address = model.address,
            role = model.role,
            active = model.active,
            startDate = model.startDate
        )
    }
}
