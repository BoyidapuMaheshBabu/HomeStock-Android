package com.boyidapu.homestock.app

import android.content.Context
import androidx.room.Room
import com.boyidapu.homestock.data.local.AppDatabase
import com.boyidapu.homestock.data.preferences.UserPreferences
import com.boyidapu.homestock.data.repository.ProductRepositoryImpl
import com.boyidapu.homestock.data.repository.TransactionRepositoryImpl
import com.boyidapu.homestock.data.repository.UserRepositoryImpl
import com.boyidapu.homestock.domain.repository.ProductRepository
import com.boyidapu.homestock.domain.repository.TransactionRepository
import com.boyidapu.homestock.domain.repository.UserRepository

class AppContainer(private val context: Context) {

    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "homestock_db"
        ).build()
    }

    val userPreferences: UserPreferences by lazy {
        UserPreferences(context)
    }

    val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl(
            database = database,
            productDao = database.productDao(),
            transactionDao = database.transactionDao()
        )
    }

    val transactionRepository: TransactionRepository by lazy {
        TransactionRepositoryImpl(database, database.transactionDao())
    }

    val userRepository: UserRepository by lazy {
        UserRepositoryImpl(userPreferences)
    }
}
