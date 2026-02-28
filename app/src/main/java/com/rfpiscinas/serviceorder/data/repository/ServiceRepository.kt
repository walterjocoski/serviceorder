package com.rfpiscinas.serviceorder.data.repository

import com.rfpiscinas.serviceorder.data.local.dao.ServiceDao
import com.rfpiscinas.serviceorder.data.local.entity.ServiceEntity
import com.rfpiscinas.serviceorder.data.model.Service
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceRepository @Inject constructor(
    private val serviceDao: ServiceDao
) {
    fun getAll(): Flow<List<Service>> = serviceDao.getAll().map { list ->
        list.map { it.toModel() }
    }

    fun getActiveServices(): Flow<List<Service>> = serviceDao.getActiveServices().map { list ->
        list.map { it.toModel() }
    }

    suspend fun getById(id: Long): Service? = serviceDao.getById(id)?.toModel()

    suspend fun insert(service: Service): Long = serviceDao.insert(ServiceEntity.fromModel(service))

    suspend fun update(service: Service) = serviceDao.update(ServiceEntity.fromModel(service))

    suspend fun delete(service: Service) = serviceDao.delete(ServiceEntity.fromModel(service))
}
