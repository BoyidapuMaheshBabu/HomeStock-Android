package com.boyidapu.homestock.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.boyidapu.homestock.domain.model.InventoryTransaction
import com.boyidapu.homestock.domain.model.TransactionType
import com.boyidapu.homestock.domain.model.UnitType

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("productId"), Index("transactionEpochDay")]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Long,
    val type: String,
    val baseQuantity: Long,
    val entryUnit: String,
    val amountPaidPaise: Long?,
    val transactionEpochDay: Long
) {
    fun toDomain(): InventoryTransaction = InventoryTransaction(
        id = id,
        productId = productId,
        type = TransactionType.valueOf(type),
        baseQuantity = baseQuantity,
        entryUnit = UnitType.fromSymbol(entryUnit),
        amountPaidPaise = amountPaidPaise,
        transactionEpochDay = transactionEpochDay
    )

    companion object {
        fun fromDomain(transaction: InventoryTransaction): TransactionEntity = TransactionEntity(
            id = transaction.id,
            productId = transaction.productId,
            type = transaction.type.name,
            baseQuantity = transaction.baseQuantity,
            entryUnit = transaction.entryUnit.symbol,
            amountPaidPaise = transaction.amountPaidPaise,
            transactionEpochDay = transaction.transactionEpochDay
        )
    }
}
