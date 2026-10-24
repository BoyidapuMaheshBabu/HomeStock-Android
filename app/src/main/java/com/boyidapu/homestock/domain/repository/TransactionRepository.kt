package com.boyidapu.homestock.domain.repository

import com.boyidapu.homestock.domain.model.CategorySpending
import com.boyidapu.homestock.domain.model.InventoryTransaction
import com.boyidapu.homestock.domain.model.ProductSpending
import com.boyidapu.homestock.domain.model.TransactionEditAudit
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getTransactionsForProduct(productId: Long): Flow<List<InventoryTransaction>>
    fun getTransactionById(transactionId: Long): Flow<InventoryTransaction?>
    fun getProductStockBaseQuantity(productId: Long): Flow<Long>
    fun getProductTotalSpending(productId: Long): Flow<Long>
    fun getSpendingBetweenEpochDays(startEpochDay: Long, endEpochDay: Long): Flow<Long>
    fun getAllTimeSpending(): Flow<Long>
    fun getAddTransactionCountBetweenEpochDays(startEpochDay: Long, endEpochDay: Long): Flow<Int>
    fun getAllTimeAddTransactionCount(): Flow<Int>
    fun getAddTransactionsBetweenEpochDays(startEpochDay: Long, endEpochDay: Long): Flow<List<InventoryTransaction>>
    fun getAllTimeAddTransactions(): Flow<List<InventoryTransaction>>
    fun getCategorySpendingBetweenEpochDays(startEpochDay: Long, endEpochDay: Long): Flow<List<CategorySpending>>
    fun getAllTimeCategorySpending(): Flow<List<CategorySpending>>
    fun getTopProductSpendingBetweenEpochDays(startEpochDay: Long, endEpochDay: Long): Flow<List<ProductSpending>>
    fun getAllTimeTopProductSpending(): Flow<List<ProductSpending>>
    fun getAuditsForTransaction(transactionId: Long): Flow<List<TransactionEditAudit>>

    suspend fun addTransaction(transaction: InventoryTransaction): Long
    suspend fun useTransactionAtomic(transaction: InventoryTransaction): Boolean
    suspend fun editTransactionAtomic(
        transactionId: Long,
        newBaseQuantity: Long,
        newEntryUnitSymbol: String,
        newAmountPaidPaise: Long?,
        newTransactionEpochDay: Long,
        editedAtEpochMs: Long = System.currentTimeMillis()
    ): Boolean
    suspend fun deleteTransactionAtomic(
        transactionId: Long,
        currentTimeMs: Long = System.currentTimeMillis()
    ): Boolean
}
