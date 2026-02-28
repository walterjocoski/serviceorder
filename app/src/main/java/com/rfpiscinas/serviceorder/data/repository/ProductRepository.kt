package com.rfpiscinas.serviceorder.data.repository

import com.rfpiscinas.serviceorder.data.local.dao.ProductDao
import com.rfpiscinas.serviceorder.data.local.entity.ProductEntity
import com.rfpiscinas.serviceorder.data.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val productDao: ProductDao
) {
    fun getAll(): Flow<List<Product>> = productDao.getAll().map { list ->
        list.map { it.toModel() }
    }

    fun getActiveProducts(): Flow<List<Product>> = productDao.getActiveProducts().map { list ->
        list.map { it.toModel() }
    }

    suspend fun getById(id: Long): Product? = productDao.getById(id)?.toModel()

    suspend fun insert(product: Product): Long = productDao.insert(ProductEntity.fromModel(product))

    suspend fun update(product: Product) = productDao.update(ProductEntity.fromModel(product))

    suspend fun delete(product: Product) = productDao.delete(ProductEntity.fromModel(product))
}
