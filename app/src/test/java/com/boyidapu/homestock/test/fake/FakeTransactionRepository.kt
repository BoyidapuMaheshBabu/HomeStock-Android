package com.boyidapu.homestock.test.fake

import com.boyidapu.homestock.domain.model.InventoryTransaction
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

    override fun getProductStockBaseQuantity(productId: Long): Flow<Long> {
        return transactions.map { list ->
            val productTxs = list.filter { it.productId == productId }
            val addSum = productTxs.filter { it.type == TransactionType.ADD }.sumOf { it.baseQuantity }
            val useSum = productTxs.filter { it.type == TransactionType.USE }.sumOf { it.baseQuantity }
            addSum - useSum
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
}
