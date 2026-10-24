package com.boyidapu.homestock.domain.model

data class InventoryTransaction(
    val id: Long = 0,
    val productId: Long,
    val type: TransactionType,
    val baseQuantity: Long,
    val entryUnit: UnitType,
    val amountPaidPaise: Long?,
    val transactionEpochDay: Long,
    val createdAtEpochMs: Long = System.currentTimeMillis()
)
