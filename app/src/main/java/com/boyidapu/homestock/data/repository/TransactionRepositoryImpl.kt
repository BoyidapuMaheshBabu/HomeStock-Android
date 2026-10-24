package com.boyidapu.homestock.data.repository

import androidx.room.withTransaction
import com.boyidapu.homestock.data.local.AppDatabase
import com.boyidapu.homestock.data.local.dao.ProductDao
import com.boyidapu.homestock.data.local.dao.TransactionAuditDao
import com.boyidapu.homestock.data.local.dao.TransactionDao
import com.boyidapu.homestock.data.local.entity.TransactionEditAuditEntity
import com.boyidapu.homestock.data.local.entity.TransactionEntity
import com.boyidapu.homestock.domain.model.InventoryTransaction
import com.boyidapu.homestock.domain.model.TransactionEditAudit
import com.boyidapu.homestock.domain.model.TransactionType
import com.boyidapu.homestock.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl(
    private val database: AppDatabase,
    private val transactionDao: TransactionDao,
    private val transactionAuditDao: TransactionAuditDao,
    private val productDao: ProductDao
) : TransactionRepository {

    override fun getTransactionsForProduct(productId: Long): Flow<List<InventoryTransaction>> {
        return transactionDao.getTransactionsForProduct(productId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getTransactionById(transactionId: Long): Flow<InventoryTransaction?> {
        return transactionDao.getTransactionById(transactionId).map { it?.toDomain() }
    }

    override fun getProductStockBaseQuantity(productId: Long): Flow<Long> {
        return transactionDao.getProductStockBaseQuantity(productId)
    }

    override fun getProductTotalSpending(productId: Long): Flow<Long> {
        return transactionDao.getProductTotalSpending(productId)
    }

    override fun getSpendingBetweenEpochDays(startEpochDay: Long, endEpochDay: Long): Flow<Long> {
        return transactionDao.getSpendingBetweenEpochDays(startEpochDay, endEpochDay)
    }

    override fun getAllTimeSpending(): Flow<Long> {
        return transactionDao.getAllTimeSpending()
    }

    override fun getAuditsForTransaction(transactionId: Long): Flow<List<TransactionEditAudit>> {
        return transactionAuditDao.getAuditsForTransaction(transactionId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun addTransaction(transaction: InventoryTransaction): Long {
        return database.withTransaction {
            val id = transactionDao.insertTransaction(TransactionEntity.fromDomain(transaction))
            productDao.markHasEverHadTransactions(transaction.productId)
            id
        }
    }

    override suspend fun useTransactionAtomic(transaction: InventoryTransaction): Boolean {
        require(transaction.type == TransactionType.USE) { "Must be USE transaction" }
        return database.withTransaction {
            val currentStock = transactionDao.getProductStockBaseQuantitySync(transaction.productId)
            if (transaction.baseQuantity > currentStock) {
                false
            } else {
                transactionDao.insertTransaction(TransactionEntity.fromDomain(transaction))
                productDao.markHasEverHadTransactions(transaction.productId)
                true
            }
        }
    }

    override suspend fun editTransactionAtomic(
        transactionId: Long,
        newBaseQuantity: Long,
        newEntryUnitSymbol: String,
        newAmountPaidPaise: Long?,
        newTransactionEpochDay: Long,
        editedAtEpochMs: Long
    ): Boolean {
        return database.withTransaction {
            val existingEntity = transactionDao.getTransactionByIdSync(transactionId) ?: return@withTransaction false
            val currentStock = transactionDao.getProductStockBaseQuantitySync(existingEntity.productId)

            // Net stock change calculation
            val isAdd = existingEntity.type == TransactionType.ADD.name
            val oldQty = existingEntity.baseQuantity
            val netStockChange = if (isAdd) (newBaseQuantity - oldQty) else (oldQty - newBaseQuantity)
            val proposedStock = currentStock + netStockChange

            if (proposedStock < 0L) {
                return@withTransaction false
            }

            // Record Audit Trail
            val auditEntity = TransactionEditAuditEntity(
                transactionId = transactionId,
                editedAtEpochMs = editedAtEpochMs,
                oldBaseQuantity = existingEntity.baseQuantity,
                oldEntryUnit = existingEntity.entryUnit,
                oldAmountPaidPaise = existingEntity.amountPaidPaise,
                oldTransactionEpochDay = existingEntity.transactionEpochDay,
                newBaseQuantity = newBaseQuantity,
                newEntryUnit = newEntryUnitSymbol,
                newAmountPaidPaise = newAmountPaidPaise,
                newTransactionEpochDay = newTransactionEpochDay
            )
            transactionAuditDao.insertAudit(auditEntity)

            // Update Transaction
            val updatedEntity = existingEntity.copy(
                baseQuantity = newBaseQuantity,
                entryUnit = newEntryUnitSymbol,
                amountPaidPaise = newAmountPaidPaise,
                transactionEpochDay = newTransactionEpochDay
            )
            transactionDao.updateTransaction(updatedEntity)
            true
        }
    }

    override suspend fun deleteTransactionAtomic(
        transactionId: Long,
        currentTimeMs: Long
    ): Boolean {
        return database.withTransaction {
            val existingEntity = transactionDao.getTransactionByIdSync(transactionId) ?: return@withTransaction false

            // Check 30-minute deadline rule
            val deadlineMs = existingEntity.createdAtEpochMs + (30 * 60 * 1000L)
            if (currentTimeMs >= deadlineMs) {
                return@withTransaction false
            }

            val currentStock = transactionDao.getProductStockBaseQuantitySync(existingEntity.productId)
            val isAdd = existingEntity.type == TransactionType.ADD.name
            val proposedStock = if (isAdd) (currentStock - existingEntity.baseQuantity) else (currentStock + existingEntity.baseQuantity)

            if (proposedStock < 0L) {
                return@withTransaction false
            }

            transactionDao.deleteTransactionById(transactionId)
            true
        }
    }
}
