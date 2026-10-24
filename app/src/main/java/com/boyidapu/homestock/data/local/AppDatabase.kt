package com.boyidapu.homestock.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.boyidapu.homestock.data.local.dao.MetadataDao
import com.boyidapu.homestock.data.local.dao.ProductDao
import com.boyidapu.homestock.data.local.dao.TransactionAuditDao
import com.boyidapu.homestock.data.local.dao.TransactionDao
import com.boyidapu.homestock.data.local.entity.MetadataEntity
import com.boyidapu.homestock.data.local.entity.ProductEntity
import com.boyidapu.homestock.data.local.entity.TransactionEntity
import com.boyidapu.homestock.data.local.entity.TransactionEditAuditEntity

@Database(
    entities = [
        ProductEntity::class,
        TransactionEntity::class,
        MetadataEntity::class,
        TransactionEditAuditEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun transactionDao(): TransactionDao
    abstract fun metadataDao(): MetadataDao
    abstract fun transactionAuditDao(): TransactionAuditDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE products ADD COLUMN hasEverHadTransactions INTEGER NOT NULL DEFAULT 0")
                db.execSQL("UPDATE products SET hasEverHadTransactions = 1 WHERE id IN (SELECT DISTINCT productId FROM transactions)")
                db.execSQL("ALTER TABLE transactions ADD COLUMN createdAtEpochMs INTEGER NOT NULL DEFAULT 0")
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `transaction_edit_audit` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `transactionId` INTEGER NOT NULL,
                        `editedAtEpochMs` INTEGER NOT NULL,
                        `oldBaseQuantity` INTEGER NOT NULL,
                        `oldEntryUnit` TEXT NOT NULL,
                        `oldAmountPaidPaise` INTEGER,
                        `oldTransactionEpochDay` INTEGER NOT NULL,
                        `newBaseQuantity` INTEGER NOT NULL,
                        `newEntryUnit` TEXT NOT NULL,
                        `newAmountPaidPaise` INTEGER,
                        `newTransactionEpochDay` INTEGER NOT NULL,
                        FOREIGN KEY(`transactionId`) REFERENCES `transactions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                """.trimIndent())
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_transaction_edit_audit_transactionId` ON `transaction_edit_audit` (`transactionId`)")
            }
        }
    }
}
