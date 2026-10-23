package com.boyidapu.homestock.test.fake

import com.boyidapu.homestock.domain.model.FixedCategory
import com.boyidapu.homestock.domain.model.Product
import com.boyidapu.homestock.domain.model.ProductWithStock
import com.boyidapu.homestock.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeProductRepository : ProductRepository {

    private val products = MutableStateFlow<List<Product>>(emptyList())
    private val transactions = MutableStateFlow<Map<Long, Int>>(emptyMap())
    private val stocks = MutableStateFlow<Map<Long, Long>>(emptyMap())

    var isSeeded = false

    fun addTestProduct(product: Product, transactionCount: Int = 0, stock: Long = 0L) {
        val currentList = products.value.filterNot { it.id == product.id }
        products.value = currentList + product
        transactions.value = transactions.value + (product.id to transactionCount)
        stocks.value = stocks.value + (product.id to stock)
    }

    override fun getProductsByCategory(category: FixedCategory): Flow<List<Product>> {
        return products.map { list ->
            list.filter { it.category == category && !it.isArchived }
        }
    }

    override fun getProductsWithStockByCategory(category: FixedCategory): Flow<List<ProductWithStock>> {
        return products.map { list ->
            list.filter { it.category == category && !it.isArchived }
                .map { p -> ProductWithStock(p, stocks.value[p.id] ?: 0L) }
        }
    }

    override fun getProductById(productId: Long): Flow<Product?> {
        return products.map { list -> list.firstOrNull { it.id == productId } }
    }

    override fun getArchivedProducts(): Flow<List<Product>> {
        return products.map { list -> list.filter { it.isArchived } }
    }

    override fun getArchivedProductsWithStock(): Flow<List<ProductWithStock>> {
        return products.map { list ->
            list.filter { it.isArchived }
                .map { p -> ProductWithStock(p, stocks.value[p.id] ?: 0L) }
        }
    }

    override suspend fun insertProduct(product: Product): Long {
        val newId = if (product.id == 0L) (products.value.maxOfOrNull { it.id } ?: 0L) + 1L else product.id
        val newProduct = product.copy(id = newId)
        products.value = products.value + newProduct
        return newId
    }

    override suspend fun updateProduct(product: Product) {
        products.value = products.value.map { if (it.id == product.id) product else it }
    }

    override suspend fun archiveProduct(productId: Long): Boolean {
        products.value = products.value.map { if (it.id == productId) it.copy(isArchived = true) else it }
        return true
    }

    override suspend fun restoreProduct(productId: Long) {
        products.value = products.value.map { if (it.id == productId) it.copy(isArchived = false) else it }
    }

    override suspend fun deleteProductAtomically(productId: Long): Boolean {
        products.value = products.value.filterNot { it.id == productId }
        return true
    }

    override suspend fun getTransactionCountForProduct(productId: Long): Int {
        return transactions.value[productId] ?: 0
    }

    override suspend fun ensureCatalogSeeded() {
        isSeeded = true
    }
}
