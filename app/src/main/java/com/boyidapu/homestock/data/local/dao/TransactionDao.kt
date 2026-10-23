package com.boyidapu.homestock.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.boyidapu.homestock.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE productId = :productId ORDER BY transactionEpochDay DESC, id DESC")
    fun getTransactionsForProduct(productId: Long): Flow<List<TransactionEntity>>

    @Query("""
        SELECT 
            COALESCE(SUM(CASE WHEN type = 'ADD' THEN baseQuantity ELSE 0 END), 0) - 
            COALESCE(SUM(CASE WHEN type = 'USE' THEN baseQuantity ELSE 0 END), 0)
        FROM transactions 
        WHERE productId = :productId
    """)
    fun getProductStockBaseQuantity(productId: Long): Flow<Long>

    @Query("""
        SELECT 
            COALESCE(SUM(CASE WHEN type = 'ADD' THEN baseQuantity ELSE 0 END), 0) - 
            COALESCE(SUM(CASE WHEN type = 'USE' THEN baseQuantity ELSE 0 END), 0)
        FROM transactions 
        WHERE productId = :productId
    """)
    suspend fun getProductStockBaseQuantitySync(productId: Long): Long

    @Query("SELECT COALESCE(SUM(amountPaidPaise), 0) FROM transactions WHERE type = 'ADD' AND transactionEpochDay BETWEEN :startEpochDay AND :endEpochDay")
    fun getSpendingBetweenEpochDays(startEpochDay: Long, endEpochDay: Long): Flow<Long>

    @Query("SELECT COALESCE(SUM(amountPaidPaise), 0) FROM transactions WHERE type = 'ADD'")
    fun getAllTimeSpending(): Flow<Long>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTransaction(transaction: TransactionEntity): Long
}
