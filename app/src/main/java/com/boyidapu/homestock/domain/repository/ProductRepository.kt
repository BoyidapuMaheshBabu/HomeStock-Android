package com.boyidapu.homestock.domain.repository

import com.boyidapu.homestock.domain.model.FixedCategory
import com.boyidapu.homestock.domain.model.Product
import com.boyidapu.homestock.domain.model.ProductWithStock
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProductsByCategory(category: FixedCategory): Flow<List<Product>>
    fun getProductsWithStockByCategory(category: FixedCategory): Flow<List<ProductWithStock>>
    fun getProductById(productId: Long): Flow<Product?>
    fun getArchivedProducts(): Flow<List<Product>>
    fun getArchivedProductsWithStock(): Flow<List<ProductWithStock>>
    suspend fun insertProduct(product: Product): Long
    suspend fun updateProduct(product: Product)
    suspend fun archiveProduct(productId: Long): Boolean
    suspend fun restoreProduct(productId: Long)
    suspend fun deleteProductAtomically(productId: Long): Boolean
    suspend fun getTransactionCountForProduct(productId: Long): Int
    suspend fun ensureCatalogSeeded()
}
