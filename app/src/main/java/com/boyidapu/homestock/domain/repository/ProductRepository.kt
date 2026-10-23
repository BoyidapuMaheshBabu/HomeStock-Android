package com.boyidapu.homestock.domain.repository

import com.boyidapu.homestock.domain.model.FixedCategory
import com.boyidapu.homestock.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProductsByCategory(category: FixedCategory): Flow<List<Product>>
    fun getProductById(productId: Long): Flow<Product?>
    fun getArchivedProducts(): Flow<List<Product>>
    suspend fun insertProduct(product: Product): Long
    suspend fun updateProduct(product: Product)
    suspend fun archiveProduct(productId: Long)
    suspend fun restoreProduct(productId: Long)
    suspend fun deleteProduct(productId: Long)
}
