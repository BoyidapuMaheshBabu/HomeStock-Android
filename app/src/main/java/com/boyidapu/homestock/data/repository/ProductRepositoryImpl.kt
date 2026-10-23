package com.boyidapu.homestock.data.repository

import com.boyidapu.homestock.data.local.dao.ProductDao
import com.boyidapu.homestock.data.local.entity.ProductEntity
import com.boyidapu.homestock.domain.model.FixedCategory
import com.boyidapu.homestock.domain.model.Product
import com.boyidapu.homestock.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val productDao: ProductDao
) : ProductRepository {

    override fun getProductsByCategory(category: FixedCategory): Flow<List<Product>> {
        return productDao.getProductsByCategory(category.id).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getProductById(productId: Long): Flow<Product?> {
        return productDao.getProductById(productId).map { it?.toDomain() }
    }

    override fun getArchivedProducts(): Flow<List<Product>> {
        return productDao.getArchivedProducts().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun insertProduct(product: Product): Long {
        return productDao.insertProduct(ProductEntity.fromDomain(product))
    }

    override suspend fun updateProduct(product: Product) {
        productDao.updateProduct(ProductEntity.fromDomain(product))
    }

    override suspend fun archiveProduct(productId: Long) {
        productDao.archiveProduct(productId)
    }

    override suspend fun restoreProduct(productId: Long) {
        productDao.restoreProduct(productId)
    }

    override suspend fun deleteProduct(productId: Long) {
        productDao.deleteProductIfNoTransactions(productId)
    }
}
