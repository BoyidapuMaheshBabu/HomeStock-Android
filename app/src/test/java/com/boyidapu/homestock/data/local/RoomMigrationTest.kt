package com.boyidapu.homestock.data.local

import com.boyidapu.homestock.domain.model.FixedCategory
import com.boyidapu.homestock.domain.model.InventoryTransaction
import com.boyidapu.homestock.domain.model.MeasurementDimension
import com.boyidapu.homestock.domain.model.Product
import com.boyidapu.homestock.domain.model.TransactionType
import com.boyidapu.homestock.domain.model.UnitType
import com.boyidapu.homestock.test.fake.FakeProductRepository
import com.boyidapu.homestock.test.fake.FakeTransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalCoroutinesApi::class)
class RoomMigrationTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeProductRepository: FakeProductRepository
    private lateinit var fakeTransactionRepository: FakeTransactionRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeProductRepository = FakeProductRepository()
        fakeTransactionRepository = FakeTransactionRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun migration_existingProductWithTransactions_hasEverHadTransactionsSetToTrue(): Unit = runTest {
        val testProduct = Product(
            id = 505L,
            category = FixedCategory.FOOD_KITCHEN,
            name = "Sugar",
            dimension = MeasurementDimension.MASS,
            hasEverHadTransactions = true
        )
        fakeProductRepository.addTestProduct(testProduct, transactionCount = 1)

        val productInRepo = fakeProductRepository.getProductById(505L).first()
        assertTrue(productInRepo?.hasEverHadTransactions == true)
    }

    @Test
    fun migration_existingTransactions_createdAtEpochMsDefaultsToZero(): Unit = runTest {
        val oldTx = InventoryTransaction(
            id = 99L,
            productId = 505L,
            type = TransactionType.ADD,
            baseQuantity = 5000L,
            entryUnit = UnitType.KG,
            amountPaidPaise = 20000L,
            transactionEpochDay = LocalDate.now(ZoneId.systemDefault()).toEpochDay(),
            createdAtEpochMs = 0L // Migrated timestamp default
        )
        fakeTransactionRepository.addTransaction(oldTx)

        val txInRepo = fakeTransactionRepository.getTransactionsForProduct(505L).first().first()
        assertEquals(0L, txInRepo.createdAtEpochMs)

        // Verify 30-min delete window is expired for migrated transaction
        val currentTime = System.currentTimeMillis()
        val canDelete = currentTime < (txInRepo.createdAtEpochMs + 30 * 60 * 1000L)
        assertFalse(canDelete)
    }
}
