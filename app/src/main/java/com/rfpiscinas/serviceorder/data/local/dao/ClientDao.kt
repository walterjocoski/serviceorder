package com.rfpiscinas.serviceorder.data.local.dao

import androidx.room.*
import com.rfpiscinas.serviceorder.data.local.entity.ClientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {

    @Query("SELECT * FROM clients ORDER BY name ASC")
    fun getAll(): Flow<List<ClientEntity>>

    @Query("SELECT * FROM clients WHERE active = 1 ORDER BY name ASC")
    fun getActiveClients(): Flow<List<ClientEntity>>

    @Query("SELECT * FROM clients WHERE id = :id")
    suspend fun getById(id: Long): ClientEntity?

    @Query("SELECT * FROM clients WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    suspend fun getByEmail(email: String): ClientEntity?

    @Query("SELECT * FROM clients WHERE REPLACE(REPLACE(REPLACE(REPLACE(cpfCnpj,'.',\'\'),'-',\'\'),'/',\'\'),' ',\'\') = :digits LIMIT 1")
    suspend fun getByCpfCnpj(digits: String): ClientEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(client: ClientEntity): Long

    @Update
    suspend fun update(client: ClientEntity)

    @Delete
    suspend fun delete(client: ClientEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(clients: List<ClientEntity>)
}