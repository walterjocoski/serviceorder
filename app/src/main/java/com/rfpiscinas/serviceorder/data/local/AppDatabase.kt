package com.rfpiscinas.serviceorder.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rfpiscinas.serviceorder.data.local.dao.*
import com.rfpiscinas.serviceorder.data.local.entity.*
import com.rfpiscinas.serviceorder.data.mock.MockData
import com.rfpiscinas.serviceorder.data.model.OrderStatus
import com.rfpiscinas.serviceorder.data.model.UnitMeasure
import com.rfpiscinas.serviceorder.data.model.UserRole
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ClientEntity::class,
        ServiceEntity::class,
        ProductEntity::class,
        UserEntity::class,
        ServiceOrderEntity::class,
        ServiceOrderItemEntity::class,
        ServiceOrderProductEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun clientDao(): ClientDao
    abstract fun serviceDao(): ServiceDao
    abstract fun productDao(): ProductDao
    abstract fun userDao(): UserDao
    abstract fun serviceOrderDao(): ServiceOrderDao

    companion object {
        const val DATABASE_NAME = "rf_piscinas_db"

        fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .addCallback(SeedDatabaseCallback())
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    private class SeedDatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Será populado no DatabaseModule via coroutine
        }
    }
}
