package com.boyidapu.homestock.domain.model

data class ProductSpending(
    val productId: Long,
    val productName: String,
    val category: FixedCategory,
    val isArchived: Boolean,
    val totalSpendingPaise: Long
)
