package com.boyidapu.homestock.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.boyidapu.homestock.data.local.dao.MetadataDao
import com.boyidapu.homestock.data.local.dao.ProductDao
import com.boyidapu.homestock.data.local.dao.TransactionDao
import com.boyidapu.homestock.data.local.entity.MetadataEntity
import com.boyidapu.homestock.data.local.entity.ProductEntity
import com.boyidapu.homestock.data.local.entity.TransactionEntity

@Database(
    entities = [ProductEntity::class, TransactionEntity::class, MetadataEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun transactionDao(): TransactionDao
    abstract fun metadataDao(): MetadataDao
}
