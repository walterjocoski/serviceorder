package com.rfpiscinas.serviceorder.data.repository

import com.rfpiscinas.serviceorder.data.local.dao.UserDao
import com.rfpiscinas.serviceorder.data.local.entity.UserEntity
import com.rfpiscinas.serviceorder.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    fun getAll(): Flow<List<User>> = userDao.getAll().map { list -> list.map { it.toModel() } }

    fun getEmployees(): Flow<List<User>> = userDao.getEmployees().map { list -> list.map { it.toModel() } }

    fun getActiveEmployees(): Flow<List<User>> = userDao.getActiveEmployees().map { list -> list.map { it.toModel() } }

    suspend fun getById(id: Long): User? = userDao.getById(id)?.toModel()

    suspend fun getByEmail(email: String): User? = userDao.getByEmail(email)?.toModel()

    /** Retorna true se já existe outro usuário com esse e-mail (excluindo o próprio ao editar) */
    suspend fun isEmailTaken(email: String, excludeId: Long = -1L): Boolean {
        val existing = userDao.getByEmail(email.lowercase().trim()) ?: return false
        return existing.id != excludeId
    }

    suspend fun insert(user: User): Long = userDao.insert(UserEntity.fromModel(user))

    suspend fun update(user: User) = userDao.update(UserEntity.fromModel(user))

    suspend fun delete(user: User) = userDao.delete(UserEntity.fromModel(user))
}
