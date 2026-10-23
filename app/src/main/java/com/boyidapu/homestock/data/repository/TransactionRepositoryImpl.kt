package com.boyidapu.homestock.data.repository

import androidx.room.withTransaction
import com.boyidapu.homestock.data.local.AppDatabase
import com.boyidapu.homestock.data.local.dao.TransactionDao
import com.boyidapu.homestock.data.local.entity.TransactionEntity
import com.boyidapu.homestock.domain.model.InventoryTransaction
import com.boyidapu.homestock.domain.model.TransactionType
import com.boyidapu.homestock.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl(
    private val database: AppDatabase,
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override fun getTransactionsForProduct(productId: Long): Flow<List<InventoryTransaction>> {
        return transactionDao.getTransactionsForProduct(productId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getProductStockBaseQuantity(productId: Long): Flow<Long> {
        return transactionDao.getProductStockBaseQuantity(productId)
    }

    override fun getSpendingBetweenEpochDays(startEpochDay: Long, endEpochDay: Long): Flow<Long> {
        return transactionDao.getSpendingBetweenEpochDays(startEpochDay, endEpochDay)
    }

    override fun getAllTimeSpending(): Flow<Long> {
        return transactionDao.getAllTimeSpending()
    }

    override suspend fun addTransaction(transaction: InventoryTransaction): Long {
        require(transaction.type == TransactionType.ADD) { "Must be ADD transaction" }
        return transactionDao.insertTransaction(TransactionEntity.fromDomain(transaction))
    }

    override suspend fun useTransactionAtomic(transaction: InventoryTransaction): Boolean {
        require(transaction.type == TransactionType.USE) { "Must be USE transaction" }
        return database.withTransaction {
            val currentStock = transactionDao.getProductStockBaseQuantitySync(transaction.productId)
            if (transaction.baseQuantity > currentStock) {
                false
            } else {
                transactionDao.insertTransaction(TransactionEntity.fromDomain(transaction))
                true
            }
        }
    }
}
