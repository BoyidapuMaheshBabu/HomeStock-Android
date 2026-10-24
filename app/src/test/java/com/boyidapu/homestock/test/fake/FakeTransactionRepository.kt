package com.boyidapu.homestock.test.fake

import com.boyidapu.homestock.domain.model.InventoryTransaction
import com.boyidapu.homestock.domain.model.TransactionEditAudit
import com.boyidapu.homestock.domain.model.TransactionType
import com.boyidapu.homestock.domain.model.UnitType
import com.boyidapu.homestock.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId

class FakeTransactionRepository : TransactionRepository {

    val transactions = MutableStateFlow<List<InventoryTransaction>>(emptyList())
    val audits = MutableStateFlow<List<TransactionEditAudit>>(emptyList())

    fun setSpendingPaise(paise: Long) {
        val todayEpochDay = LocalDate.now(ZoneId.systemDefault()).toEpochDay()
        val dummyTx = InventoryTransaction(
            productId = 1L,
            type = TransactionType.ADD,
            baseQuantity = 1000L,
            entryUnit = UnitType.KG,
            amountPaidPaise = paise,
            transactionEpochDay = todayEpochDay
        )
        transactions.value = transactions.value + dummyTx
    }

    override fun getTransactionsForProduct(productId: Long): Flow<List<InventoryTransaction>> {
        return transactions.map { list -> list.filter { it.productId == productId } }
    }

    override fun getTransactionById(transactionId: Long): Flow<InventoryTransaction?> {
        return transactions.map { list -> list.firstOrNull { it.id == transactionId } }
    }

    override fun getProductStockBaseQuantity(productId: Long): Flow<Long> {
        return transactions.map { list ->
            val productTxs = list.filter { it.productId == productId }
            val addSum = productTxs.filter { it.type == TransactionType.ADD }.sumOf { it.baseQuantity }
            val useSum = productTxs.filter { it.type == TransactionType.USE }.sumOf { it.baseQuantity }
            addSum - useSum
        }
    }

    override fun getProductTotalSpending(productId: Long): Flow<Long> {
        return transactions.map { list ->
            list.filter {
                it.productId == productId && it.type == TransactionType.ADD && it.amountPaidPaise != null
            }.sumOf { it.amountPaidPaise!! }
        }
    }

    override fun getSpendingBetweenEpochDays(startEpochDay: Long, endEpochDay: Long): Flow<Long> {
        return transactions.map { list ->
            list.filter {
                it.type == TransactionType.ADD &&
                it.amountPaidPaise != null &&
                it.transactionEpochDay >= startEpochDay &&
                it.transactionEpochDay <= endEpochDay
            }.sumOf { it.amountPaidPaise!! }
        }
    }

    override fun getAllTimeSpending(): Flow<Long> {
        return transactions.map { list ->
            list.filter {
                it.type == TransactionType.ADD && it.amountPaidPaise != null
            }.sumOf { it.amountPaidPaise!! }
        }
    }

    override fun getAuditsForTransaction(transactionId: Long): Flow<List<TransactionEditAudit>> {
        return audits.map { list -> list.filter { it.transactionId == transactionId } }
    }

    override suspend fun addTransaction(transaction: InventoryTransaction): Long {
        val newId = (transactions.value.maxOfOrNull { it.id } ?: 0L) + 1L
        val newTx = transaction.copy(id = newId)
        transactions.value = transactions.value + newTx
        return newId
    }

    override suspend fun useTransactionAtomic(transaction: InventoryTransaction): Boolean {
        require(transaction.type == TransactionType.USE)
        val productTxs = transactions.value.filter { it.productId == transaction.productId }
        val addSum = productTxs.filter { it.type == TransactionType.ADD }.sumOf { it.baseQuantity }
        val useSum = productTxs.filter { it.type == TransactionType.USE }.sumOf { it.baseQuantity }
        val currentStock = addSum - useSum

        if (transaction.baseQuantity > currentStock) {
            return false
        }
        val newId = (transactions.value.maxOfOrNull { it.id } ?: 0L) + 1L
        val newTx = transaction.copy(id = newId)
        transactions.value = transactions.value + newTx
        return true
    }

    override suspend fun editTransactionAtomic(
        transactionId: Long,
        newBaseQuantity: Long,
        newEntryUnitSymbol: String,
        newAmountPaidPaise: Long?,
        newTransactionEpochDay: Long,
        editedAtEpochMs: Long
    ): Boolean {
        val existing = transactions.value.firstOrNull { it.id == transactionId } ?: return false
        val productTxs = transactions.value.filter { it.productId == existing.productId }
        val addSum = productTxs.filter { it.type == TransactionType.ADD && it.id != transactionId }
            .sumOf { it.baseQuantity } + (if (existing.type == TransactionType.ADD) newBaseQuantity else 0L)
        val useSum = productTxs.filter { it.type == TransactionType.USE && it.id != transactionId }
            .sumOf { it.baseQuantity } + (if (existing.type == TransactionType.USE) newBaseQuantity else 0L)
        val proposedStock = addSum - useSum

        if (proposedStock < 0L) {
            return false
        }

        val audit = TransactionEditAudit(
            id = (audits.value.maxOfOrNull { it.id } ?: 0L) + 1L,
            transactionId = transactionId,
            editedAtEpochMs = editedAtEpochMs,
            oldBaseQuantity = existing.baseQuantity,
            oldEntryUnit = existing.entryUnit,
            oldAmountPaidPaise = existing.amountPaidPaise,
            oldTransactionEpochDay = existing.transactionEpochDay,
            newBaseQuantity = newBaseQuantity,
            newEntryUnit = UnitType.fromSymbol(newEntryUnitSymbol),
            newAmountPaidPaise = newAmountPaidPaise,
            newTransactionEpochDay = newTransactionEpochDay
        )
        audits.value = audits.value + audit

        val updated = existing.copy(
            baseQuantity = newBaseQuantity,
            entryUnit = UnitType.fromSymbol(newEntryUnitSymbol),
            amountPaidPaise = newAmountPaidPaise,
            transactionEpochDay = newTransactionEpochDay
        )
        transactions.value = transactions.value.map { if (it.id == transactionId) updated else it }
        return true
    }

    override suspend fun deleteTransactionAtomic(
        transactionId: Long,
        currentTimeMs: Long
    ): Boolean {
        val existing = transactions.value.firstOrNull { it.id == transactionId } ?: return false
        val deadline = existing.createdAtEpochMs + (30 * 60 * 1000L)
        if (currentTimeMs >= deadline) {
            return false
        }

        val remainingTxs = transactions.value.filterNot { it.id == transactionId }
        val productTxs = remainingTxs.filter { it.productId == existing.productId }
        val addSum = productTxs.filter { it.type == TransactionType.ADD }.sumOf { it.baseQuantity }
        val useSum = productTxs.filter { it.type == TransactionType.USE }.sumOf { it.baseQuantity }
        val proposedStock = addSum - useSum

        if (proposedStock < 0L) {
            return false
        }

        transactions.value = remainingTxs
        return true
    }
}
