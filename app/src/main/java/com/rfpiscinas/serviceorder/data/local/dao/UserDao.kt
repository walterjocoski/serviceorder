package com.rfpiscinas.serviceorder.data.local.dao

import androidx.room.*
import com.rfpiscinas.serviceorder.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users ORDER BY name ASC")
    fun getAll(): Flow<List<UserEntity>>

    // Usa literal string para evitar MissingType com enum em @Query
    @Query("SELECT * FROM users WHERE role = 'EMPLOYEE' ORDER BY name ASC")
    fun getEmployees(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE role = 'EMPLOYEE' AND active = 1 ORDER BY name ASC")
    fun getActiveEmployees(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE role = 'MANAGER' ORDER BY name ASC")
    fun getManagers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getById(id: Long): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity): Long

    @Update
    suspend fun update(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)
}
