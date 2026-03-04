package com.rfpiscinas.serviceorder.di

import android.content.Context
import com.rfpiscinas.serviceorder.data.local.AppDatabase
import com.rfpiscinas.serviceorder.data.local.dao.*
import com.rfpiscinas.serviceorder.data.local.entity.*
import com.rfpiscinas.serviceorder.data.mock.MockData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        val db = AppDatabase.buildDatabase(context)
        // Seed database on first creation
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            seedIfEmpty(db)
        }
        return db
    }

    private suspend fun seedIfEmpty(db: AppDatabase) {
        val existingUsers = db.userDao().getByEmail("gerente@rfpiscinas.com")
        if (existingUsers != null) return // Already seeded

        // Insert users (employees + manager + master/Walter)
        db.userDao().insertAll(MockData.getAllSeedUsers().map { UserEntity.fromModel(it) })

        // Insert clients
        db.clientDao().insertAll(MockData.clients.map { ClientEntity.fromModel(it) })

        // Insert services
        db.serviceDao().insertAll(MockData.services.map { ServiceEntity.fromModel(it) })

        // Insert products
        db.productDao().insertAll(MockData.products.map { ProductEntity.fromModel(it) })

        // Insert service orders with items and products
        for (order in MockData.completedOrders) {
            val orderId = db.serviceOrderDao().insertOrder(
                ServiceOrderEntity(
                    id = order.id,
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
                val itemId = db.serviceOrderDao().insertItem(
                    ServiceOrderItemEntity(
                        id = item.id,
                        serviceOrderId = orderId,
                        serviceId = item.service.id,
                        serviceName = item.service.name,
                        serviceType = item.service.type,
                        serviceUsesProducts = item.service.usesProducts,
                        observations = item.observations
                    )
                )
                for (product in item.products) {
                    db.serviceOrderDao().insertProduct(
                        ServiceOrderProductEntity(
                            id = product.id,
                            serviceOrderItemId = itemId,
                            productId = product.product.id,
                            productName = product.product.name,
                            productUnitMeasure = product.product.unitMeasure,
                            quantity = product.quantity
                        )
                    )
                }
            }
        }
    }

    @Provides
    fun provideClientDao(db: AppDatabase): ClientDao = db.clientDao()

    @Provides
    fun provideServiceDao(db: AppDatabase): ServiceDao = db.serviceDao()

    @Provides
    fun provideProductDao(db: AppDatabase): ProductDao = db.productDao()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    fun provideServiceOrderDao(db: AppDatabase): ServiceOrderDao = db.serviceOrderDao()
}
