package com.boyidapu.homestock.domain.repository

import com.boyidapu.homestock.domain.model.InventoryTransaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getTransactionsForProduct(productId: Long): Flow<List<InventoryTransaction>>
    fun getProductStockBaseQuantity(productId: Long): Flow<Long>
    fun getSpendingBetweenEpochDays(startEpochDay: Long, endEpochDay: Long): Flow<Long>
    fun getAllTimeSpending(): Flow<Long>
    suspend fun addTransaction(transaction: InventoryTransaction): Long
    suspend fun useTransactionAtomic(transaction: InventoryTransaction): Boolean
}
