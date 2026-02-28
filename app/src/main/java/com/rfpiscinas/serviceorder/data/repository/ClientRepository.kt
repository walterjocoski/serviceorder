package com.rfpiscinas.serviceorder.data.repository

import com.rfpiscinas.serviceorder.data.local.dao.ClientDao
import com.rfpiscinas.serviceorder.data.local.entity.ClientEntity
import com.rfpiscinas.serviceorder.data.model.Client
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientRepository @Inject constructor(
    private val clientDao: ClientDao
) {
    fun getAll(): Flow<List<Client>> = clientDao.getAll().map { list ->
        list.map { it.toModel() }
    }

    fun getActiveClients(): Flow<List<Client>> = clientDao.getActiveClients().map { list ->
        list.map { it.toModel() }
    }

    suspend fun getById(id: Long): Client? = clientDao.getById(id)?.toModel()

    suspend fun insert(client: Client): Long = clientDao.insert(ClientEntity.fromModel(client))

    suspend fun update(client: Client) = clientDao.update(ClientEntity.fromModel(client))

    suspend fun delete(client: Client) = clientDao.delete(ClientEntity.fromModel(client))
}
