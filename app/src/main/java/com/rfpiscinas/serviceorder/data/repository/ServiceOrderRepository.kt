package com.rfpiscinas.serviceorder.data.repository

import com.rfpiscinas.serviceorder.data.local.dao.ServiceOrderDao
import com.rfpiscinas.serviceorder.data.local.entity.ServiceOrderEntity
import com.rfpiscinas.serviceorder.data.local.entity.ServiceOrderItemEntity
import com.rfpiscinas.serviceorder.data.local.entity.ServiceOrderProductEntity
import com.rfpiscinas.serviceorder.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceOrderRepository @Inject constructor(
    private val serviceOrderDao: ServiceOrderDao
) {

    fun getAllOrders(): Flow<List<ServiceOrder>> = serviceOrderDao.getAll().map { entities ->
        entities.map { loadFullOrder(it) }
    }

    fun getOrdersByEmployee(employeeId: Long): Flow<List<ServiceOrder>> =
        serviceOrderDao.getByEmployee(employeeId).map { entities ->
            entities.map { loadFullOrder(it) }
        }

    fun getDistinctEmployeeNames(): Flow<List<String>> =
        serviceOrderDao.getDistinctEmployeeNames()

    suspend fun updateOrderStatus(orderId: Long, status: OrderStatus, endDateTime: String? = null) {
        serviceOrderDao.updateOrderStatus(orderId, status.name, endDateTime)
    }

    suspend fun finalizeOrder(orderId: Long, endDateTime: String, items: List<ServiceOrderItem>) {
        serviceOrderDao.deleteProductsByOrderId(orderId)
        serviceOrderDao.deleteItemsByOrderId(orderId)
        for (item in items) {
            val itemId = serviceOrderDao.insertItem(
                ServiceOrderItemEntity(
                    serviceOrderId = orderId,
                    serviceId = item.service.id,
                    serviceName = item.service.name,
                    serviceType = item.service.type,
                    serviceUsesProducts = item.service.usesProducts,
                    observations = item.observations
                )
            )
            for (product in item.products) {
                serviceOrderDao.insertProduct(
                    ServiceOrderProductEntity(
                        serviceOrderItemId = itemId,
                        productId = product.product.id,
                        productName = product.product.name,
                        productUnitMeasure = product.product.unitMeasure,
                        quantity = product.quantity
                    )
                )
            }
        }
        serviceOrderDao.updateOrderStatus(orderId, OrderStatus.COMPLETED.name, endDateTime)
    }

    suspend fun cancelOrder(orderId: Long) {
        serviceOrderDao.updateOrderStatus(orderId, OrderStatus.CANCELLED.name, null)
    }

    suspend fun getOrderById(id: Long): ServiceOrder? {
        val entity = serviceOrderDao.getById(id) ?: return null
        return loadFullOrder(entity)
    }

    suspend fun insertOrder(order: ServiceOrder): Long {
        val orderId = serviceOrderDao.insertOrder(
            ServiceOrderEntity(
                id = if (order.id == 0L) 0 else order.id,
                clientId = order.clientId,
                clientName = order.clientName,
                clientAddress = order.clientAddress,
                employeeId = order.employeeId,
                employeeName = order.employeeName,
                status = order.status,
                startDateTime = order.startDateTime,
                endDateTime = order.endDateTime,
                synced = order.synced
            )
        )

        for (item in order.items) {
            val itemId = serviceOrderDao.insertItem(
                ServiceOrderItemEntity(
                    serviceOrderId = orderId,
                    serviceId = item.service.id,
                    serviceName = item.service.name,
                    serviceType = item.service.type,
                    serviceUsesProducts = item.service.usesProducts,
                    observations = item.observations
                )
            )
            for (product in item.products) {
                serviceOrderDao.insertProduct(
                    ServiceOrderProductEntity(
                        serviceOrderItemId = itemId,
                        productId = product.product.id,
                        productName = product.product.name,
                        productUnitMeasure = product.product.unitMeasure,
                        quantity = product.quantity
                    )
                )
            }
        }

        return orderId
    }

    private suspend fun loadFullOrder(entity: ServiceOrderEntity): ServiceOrder {
        val items = serviceOrderDao.getItemsByOrderId(entity.id)
        val fullItems = items.map { itemEntity ->
            val products = serviceOrderDao.getProductsByItemId(itemEntity.id)
            ServiceOrderItem(
                id = itemEntity.id,
                service = Service(
                    id = itemEntity.serviceId,
                    name = itemEntity.serviceName,
                    type = itemEntity.serviceType,
                    usesProducts = itemEntity.serviceUsesProducts
                ),
                observations = itemEntity.observations,
                products = products.map { prodEntity ->
                    ServiceOrderProduct(
                        id = prodEntity.id,
                        product = Product(
                            id = prodEntity.productId,
                            name = prodEntity.productName,
                            unitMeasure = prodEntity.productUnitMeasure
                        ),
                        quantity = prodEntity.quantity
                    )
                }
            )
        }

        return ServiceOrder(
            id = entity.id,
            clientId = entity.clientId,
            clientName = entity.clientName,
            clientAddress = entity.clientAddress,
            employeeId = entity.employeeId,
            employeeName = entity.employeeName,
            status = entity.status,
            startDateTime = entity.startDateTime,
            endDateTime = entity.endDateTime,
            items = fullItems,
            synced = entity.synced
        )
    }
}