package com.boyidapu.homestock.domain.model

data class InventoryTransaction(
    val id: Long = 0,
    val productId: Long,
    val type: TransactionType,
    val baseQuantity: Long,
    val entryUnit: UnitType,
    val amountPaidPaise: Long? = null,
    val transactionEpochDay: Long
)
