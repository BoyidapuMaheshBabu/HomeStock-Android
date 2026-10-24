package com.boyidapu.homestock.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.boyidapu.homestock.data.local.entity.TransactionEditAuditEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionAuditDao {
    @Query("SELECT * FROM transaction_edit_audit WHERE transactionId = :transactionId ORDER BY editedAtEpochMs ASC")
    fun getAuditsForTransaction(transactionId: Long): Flow<List<TransactionEditAuditEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAudit(audit: TransactionEditAuditEntity): Long
}
