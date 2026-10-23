package com.boyidapu.homestock.data.local.entity

import androidx.room.Embedded
import com.boyidapu.homestock.domain.model.ProductWithStock

data class ProductWithStockEntity(
    @Embedded val product: ProductEntity,
    val stockBaseQuantity: Long
) {
    fun toDomain(): ProductWithStock = ProductWithStock(
        product = product.toDomain(),
        stockBaseQuantity = stockBaseQuantity
    )
}
