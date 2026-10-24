package com.boyidapu.homestock.domain.model

data class CategorySpending(
    val category: FixedCategory,
    val totalSpendingPaise: Long,
    val percentageShare: Float
)
