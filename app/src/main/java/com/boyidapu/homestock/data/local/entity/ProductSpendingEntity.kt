package com.boyidapu.homestock.data.local.entity

data class ProductSpendingEntity(
    val productId: Long,
    val productName: String,
    val categoryId: String,
    val isArchived: Boolean,
    val totalSpendingPaise: Long
)
