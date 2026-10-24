package com.boyidapu.homestock.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.boyidapu.homestock.data.local.entity.ProductEntity
import com.boyidapu.homestock.data.local.entity.ProductWithStockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE categoryId = :categoryId AND isArchived = 0 ORDER BY name ASC")
    fun getProductsByCategory(categoryId: String): Flow<List<ProductEntity>>

    @Query("""
        SELECT 
            p.*,
            COALESCE(SUM(CASE WHEN t.type = 'ADD' THEN t.baseQuantity ELSE 0 END), 0) - 
            COALESCE(SUM(CASE WHEN t.type = 'USE' THEN t.baseQuantity ELSE 0 END), 0) AS stockBaseQuantity
        FROM products p
        LEFT JOIN transactions t ON p.id = t.productId
        WHERE p.categoryId = :categoryId AND p.isArchived = 0
        GROUP BY p.id
        ORDER BY p.name ASC
    """)
    fun getProductsWithStockByCategory(categoryId: String): Flow<List<ProductWithStockEntity>>

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    fun getProductById(productId: Long): Flow<ProductEntity?>

    @Query("SELECT * FROM products WHERE isArchived = 1 ORDER BY name ASC")
    fun getArchivedProducts(): Flow<List<ProductEntity>>

    @Query("""
        SELECT 
            p.*,
            COALESCE(SUM(CASE WHEN t.type = 'ADD' THEN t.baseQuantity ELSE 0 END), 0) - 
            COALESCE(SUM(CASE WHEN t.type = 'USE' THEN t.baseQuantity ELSE 0 END), 0) AS stockBaseQuantity
        FROM products p
        LEFT JOIN transactions t ON p.id = t.productId
        WHERE p.isArchived = 1
        GROUP BY p.id
        ORDER BY p.name ASC
    """)
    fun getArchivedProductsWithStock(): Flow<List<ProductWithStockEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertProduct(product: ProductEntity): Long

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("UPDATE products SET isArchived = 1 WHERE id = :productId")
    suspend fun archiveProduct(productId: Long)

    @Query("UPDATE products SET isArchived = 0 WHERE id = :productId")
    suspend fun restoreProduct(productId: Long)

    @Query("UPDATE products SET hasEverHadTransactions = 1 WHERE id = :productId")
    suspend fun markHasEverHadTransactions(productId: Long)

    @Query("DELETE FROM transactions WHERE productId = :productId")
    suspend fun deleteTransactionsForProduct(productId: Long)

    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun deleteProductById(productId: Long)

    @Query("SELECT COUNT(*) FROM transactions WHERE productId = :productId")
    suspend fun getTransactionCountForProduct(productId: Long): Int
}
