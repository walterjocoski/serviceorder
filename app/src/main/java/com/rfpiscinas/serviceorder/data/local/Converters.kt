package com.rfpiscinas.serviceorder.data.local

import androidx.room.TypeConverter
import com.rfpiscinas.serviceorder.data.model.OrderStatus
import com.rfpiscinas.serviceorder.data.model.UnitMeasure
import com.rfpiscinas.serviceorder.data.model.UserRole

class Converters {

    @TypeConverter
    fun fromOrderStatus(value: OrderStatus): String = value.name

    @TypeConverter
    fun toOrderStatus(value: String): OrderStatus = OrderStatus.valueOf(value)

    @TypeConverter
    fun fromUserRole(value: UserRole): String = value.name

    @TypeConverter
    fun toUserRole(value: String): UserRole = UserRole.valueOf(value)

    @TypeConverter
    fun fromUnitMeasure(value: UnitMeasure): String = value.name

    @TypeConverter
    fun toUnitMeasure(value: String): UnitMeasure = UnitMeasure.valueOf(value)
}
