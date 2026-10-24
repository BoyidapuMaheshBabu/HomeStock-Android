package com.boyidapu.homestock.domain.model

data class TransactionEditAudit(
    val id: Long = 0,
    val transactionId: Long,
    val editedAtEpochMs: Long,
    val oldBaseQuantity: Long,
    val oldEntryUnit: UnitType,
    val oldAmountPaidPaise: Long?,
    val oldTransactionEpochDay: Long,
    val newBaseQuantity: Long,
    val newEntryUnit: UnitType,
    val newAmountPaidPaise: Long?,
    val newTransactionEpochDay: Long
)
