package com.boyidapu.homestock.domain.model

import com.boyidapu.homestock.core.quantity.QuantityUtils

data class ProductWithStock(
    val product: Product,
    val stockBaseQuantity: Long
) {
    val formattedStock: String
        get() = QuantityUtils.autoFormatStock(stockBaseQuantity, product.dimension)
}
