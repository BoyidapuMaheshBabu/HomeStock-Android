package com.boyidapu.homestock.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.boyidapu.homestock.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE categoryId = :categoryId AND isArchived = 0 ORDER BY name ASC")
    fun getProductsByCategory(categoryId: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    fun getProductById(productId: Long): Flow<ProductEntity?>

    @Query("SELECT * FROM products WHERE isArchived = 1 ORDER BY name ASC")
    fun getArchivedProducts(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("UPDATE products SET isArchived = 1 WHERE id = :productId")
    suspend fun archiveProduct(productId: Long)

    @Query("UPDATE products SET isArchived = 0 WHERE id = :productId")
    suspend fun restoreProduct(productId: Long)

    @Query("DELETE FROM products WHERE id = :productId AND id NOT IN (SELECT DISTINCT productId FROM transactions)")
    suspend fun deleteProductIfNoTransactions(productId: Long): Int
}
