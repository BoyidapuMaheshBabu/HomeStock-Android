package com.boyidapu.homestock.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.boyidapu.homestock.domain.model.TransactionEditAudit
import com.boyidapu.homestock.domain.model.UnitType

@Entity(
    tableName = "transaction_edit_audit",
    foreignKeys = [
        ForeignKey(
            entity = TransactionEntity::class,
            parentColumns = ["id"],
            childColumns = ["transactionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("transactionId")]
)
data class TransactionEditAuditEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val transactionId: Long,
    val editedAtEpochMs: Long,
    val oldBaseQuantity: Long,
    val oldEntryUnit: String,
    val oldAmountPaidPaise: Long?,
    val oldTransactionEpochDay: Long,
    val newBaseQuantity: Long,
    val newEntryUnit: String,
    val newAmountPaidPaise: Long?,
    val newTransactionEpochDay: Long
) {
    fun toDomain(): TransactionEditAudit = TransactionEditAudit(
        id = id,
        transactionId = transactionId,
        editedAtEpochMs = editedAtEpochMs,
        oldBaseQuantity = oldBaseQuantity,
        oldEntryUnit = UnitType.fromSymbol(oldEntryUnit),
        oldAmountPaidPaise = oldAmountPaidPaise,
        oldTransactionEpochDay = oldTransactionEpochDay,
        newBaseQuantity = newBaseQuantity,
        newEntryUnit = UnitType.fromSymbol(newEntryUnit),
        newAmountPaidPaise = newAmountPaidPaise,
        newTransactionEpochDay = newTransactionEpochDay
    )

    companion object {
        fun fromDomain(audit: TransactionEditAudit): TransactionEditAuditEntity = TransactionEditAuditEntity(
            id = audit.id,
            transactionId = audit.transactionId,
            editedAtEpochMs = audit.editedAtEpochMs,
            oldBaseQuantity = audit.oldBaseQuantity,
            oldEntryUnit = audit.oldEntryUnit.symbol,
            oldAmountPaidPaise = audit.oldAmountPaidPaise,
            oldTransactionEpochDay = audit.oldTransactionEpochDay,
            newBaseQuantity = audit.newBaseQuantity,
            newEntryUnit = audit.newEntryUnit.symbol,
            newAmountPaidPaise = audit.newAmountPaidPaise,
            newTransactionEpochDay = audit.newTransactionEpochDay
        )
    }
}
