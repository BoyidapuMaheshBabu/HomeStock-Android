package com.boyidapu.homestock.test.fake

import com.boyidapu.homestock.domain.model.InventoryTransaction
import com.boyidapu.homestock.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeTransactionRepository : TransactionRepository {

    private val _spendingPaise = MutableStateFlow(0L)

    fun setSpendingPaise(paise: Long) {
        _spendingPaise.value = paise
    }

    override fun getTransactionsForProduct(productId: Long): Flow<List<InventoryTransaction>> {
        return MutableStateFlow(emptyList())
    }

    override fun getProductStockBaseQuantity(productId: Long): Flow<Long> {
        return MutableStateFlow(0L)
    }

    override fun getSpendingBetweenEpochDays(startEpochDay: Long, endEpochDay: Long): Flow<Long> {
        return _spendingPaise.asStateFlow()
    }

    override fun getAllTimeSpending(): Flow<Long> {
        return _spendingPaise.asStateFlow()
    }

    override suspend fun addTransaction(transaction: InventoryTransaction): Long {
        return 1L
    }

    override suspend fun useTransactionAtomic(transaction: InventoryTransaction): Boolean {
        return true
    }
}
