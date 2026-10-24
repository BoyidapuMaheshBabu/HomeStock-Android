package com.boyidapu.homestock.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.boyidapu.homestock.data.local.entity.CategorySpendingEntity
import com.boyidapu.homestock.data.local.entity.ProductSpendingEntity
import com.boyidapu.homestock.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE productId = :productId ORDER BY transactionEpochDay DESC, createdAtEpochMs DESC, id DESC")
    fun getTransactionsForProduct(productId: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :transactionId LIMIT 1")
    fun getTransactionById(transactionId: Long): Flow<TransactionEntity?>

    @Query("SELECT * FROM transactions WHERE id = :transactionId LIMIT 1")
    suspend fun getTransactionByIdSync(transactionId: Long): TransactionEntity?

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

    @Query("SELECT COALESCE(SUM(amountPaidPaise), 0) FROM transactions WHERE productId = :productId AND type = 'ADD'")
    fun getProductTotalSpending(productId: Long): Flow<Long>

    @Query("SELECT COALESCE(SUM(amountPaidPaise), 0) FROM transactions WHERE type = 'ADD' AND transactionEpochDay BETWEEN :startEpochDay AND :endEpochDay")
    fun getSpendingBetweenEpochDays(startEpochDay: Long, endEpochDay: Long): Flow<Long>

    @Query("SELECT COALESCE(SUM(amountPaidPaise), 0) FROM transactions WHERE type = 'ADD'")
    fun getAllTimeSpending(): Flow<Long>

    @Query("SELECT COUNT(*) FROM transactions WHERE type = 'ADD' AND transactionEpochDay BETWEEN :startEpochDay AND :endEpochDay")
    fun getAddTransactionCountBetweenEpochDays(startEpochDay: Long, endEpochDay: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM transactions WHERE type = 'ADD'")
    fun getAllTimeAddTransactionCount(): Flow<Int>

    @Query("SELECT * FROM transactions WHERE type = 'ADD' AND transactionEpochDay BETWEEN :startEpochDay AND :endEpochDay ORDER BY transactionEpochDay ASC")
    fun getAddTransactionsBetweenEpochDays(startEpochDay: Long, endEpochDay: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE type = 'ADD' ORDER BY transactionEpochDay ASC")
    fun getAllTimeAddTransactions(): Flow<List<TransactionEntity>>

    @Query("""
        SELECT 
            p.categoryId AS categoryId,
            COALESCE(SUM(t.amountPaidPaise), 0) AS totalSpendingPaise
        FROM products p
        JOIN transactions t ON p.id = t.productId
        WHERE t.type = 'ADD' AND t.transactionEpochDay BETWEEN :startEpochDay AND :endEpochDay
        GROUP BY p.categoryId
    """)
    fun getCategorySpendingBetweenEpochDays(startEpochDay: Long, endEpochDay: Long): Flow<List<CategorySpendingEntity>>

    @Query("""
        SELECT 
            p.categoryId AS categoryId,
            COALESCE(SUM(t.amountPaidPaise), 0) AS totalSpendingPaise
        FROM products p
        JOIN transactions t ON p.id = t.productId
        WHERE t.type = 'ADD'
        GROUP BY p.categoryId
    """)
    fun getAllTimeCategorySpending(): Flow<List<CategorySpendingEntity>>

    @Query("""
        SELECT 
            p.id AS productId,
            p.name AS productName,
            p.categoryId AS categoryId,
            p.isArchived AS isArchived,
            COALESCE(SUM(t.amountPaidPaise), 0) AS totalSpendingPaise
        FROM products p
        JOIN transactions t ON p.id = t.productId
        WHERE t.type = 'ADD' AND t.transactionEpochDay BETWEEN :startEpochDay AND :endEpochDay
        GROUP BY p.id
        HAVING totalSpendingPaise > 0
        ORDER BY totalSpendingPaise DESC, p.name ASC
        LIMIT 5
    """)
    fun getTopProductSpendingBetweenEpochDays(startEpochDay: Long, endEpochDay: Long): Flow<List<ProductSpendingEntity>>

    @Query("""
        SELECT 
            p.id AS productId,
            p.name AS productName,
            p.categoryId AS categoryId,
            p.isArchived AS isArchived,
            COALESCE(SUM(t.amountPaidPaise), 0) AS totalSpendingPaise
        FROM products p
        JOIN transactions t ON p.id = t.productId
        WHERE t.type = 'ADD'
        GROUP BY p.id
        HAVING totalSpendingPaise > 0
        ORDER BY totalSpendingPaise DESC, p.name ASC
        LIMIT 5
    """)
    fun getAllTimeTopProductSpending(): Flow<List<ProductSpendingEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :transactionId")
    suspend fun deleteTransactionById(transactionId: Long)
}
