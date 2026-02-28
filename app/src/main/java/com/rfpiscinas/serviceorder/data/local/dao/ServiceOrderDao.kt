package com.rfpiscinas.serviceorder.data.local.dao

import androidx.room.*
import com.rfpiscinas.serviceorder.data.local.entity.ServiceOrderEntity
import com.rfpiscinas.serviceorder.data.local.entity.ServiceOrderItemEntity
import com.rfpiscinas.serviceorder.data.local.entity.ServiceOrderProductEntity
import com.rfpiscinas.serviceorder.data.model.OrderStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceOrderDao {

    @Query("SELECT * FROM service_orders ORDER BY id DESC")
    fun getAll(): Flow<List<ServiceOrderEntity>>

    @Query("SELECT * FROM service_orders WHERE employeeId = :employeeId ORDER BY id DESC")
    fun getByEmployee(employeeId: Long): Flow<List<ServiceOrderEntity>>

    @Query("SELECT * FROM service_orders WHERE id = :id")
    suspend fun getById(id: Long): ServiceOrderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: ServiceOrderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ServiceOrderItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ServiceOrderProductEntity): Long

    @Update
    suspend fun updateOrder(order: ServiceOrderEntity)

    @Delete
    suspend fun deleteOrder(order: ServiceOrderEntity)

    @Query("SELECT * FROM service_order_items WHERE serviceOrderId = :orderId")
    suspend fun getItemsByOrderId(orderId: Long): List<ServiceOrderItemEntity>

    @Query("SELECT * FROM service_order_products WHERE serviceOrderItemId = :itemId")
    suspend fun getProductsByItemId(itemId: Long): List<ServiceOrderProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllOrders(orders: List<ServiceOrderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllItems(items: List<ServiceOrderItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProducts(products: List<ServiceOrderProductEntity>)

    @Query("SELECT DISTINCT employeeName FROM service_orders ORDER BY employeeName ASC")
    fun getDistinctEmployeeNames(): Flow<List<String>>

    @Query("UPDATE service_orders SET status = :statusStr, endDateTime = :endDateTime WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Long, statusStr: String, endDateTime: String?)

    @Query("DELETE FROM service_order_items WHERE serviceOrderId = :orderId")
    suspend fun deleteItemsByOrderId(orderId: Long)

    @Query("DELETE FROM service_order_products WHERE serviceOrderItemId IN (SELECT id FROM service_order_items WHERE serviceOrderId = :orderId)")
    suspend fun deleteProductsByOrderId(orderId: Long)
}
