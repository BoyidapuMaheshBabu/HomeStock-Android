package com.boyidapu.homestock.ui.history

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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalCoroutinesApi::class)
class ProductHistoryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeProductRepository: FakeProductRepository
    private lateinit var fakeTransactionRepository: FakeTransactionRepository
    private lateinit var viewModel: ProductHistoryViewModel

    private val testProduct = Product(
        id = 303L,
        category = FixedCategory.FOOD_KITCHEN,
        name = "Ponni Rice",
        dimension = MeasurementDimension.MASS,
        isPreset = false,
        isArchived = false
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeProductRepository = FakeProductRepository()
        fakeTransactionRepository = FakeTransactionRepository()

        fakeProductRepository.addTestProduct(testProduct, transactionCount = 0)

        viewModel = ProductHistoryViewModel(
            productId = testProduct.id,
            productRepository = fakeProductRepository,
            transactionRepository = fakeTransactionRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadHistoryData_groupsTransactionsByDate_newestDatesFirst(): Unit = runTest {
        val today = LocalDate.now(ZoneId.systemDefault()).toEpochDay()
        val yesterday = today - 1

        val txToday = InventoryTransaction(
            id = 1L,
            productId = testProduct.id,
            type = TransactionType.ADD,
            baseQuantity = 10000L, // 10 kg
            entryUnit = UnitType.KG,
            amountPaidPaise = 50000L, // ₹500
            transactionEpochDay = today,
            createdAtEpochMs = System.currentTimeMillis()
        )

        val txYesterday = InventoryTransaction(
            id = 2L,
            productId = testProduct.id,
            type = TransactionType.USE,
            baseQuantity = 2000L, // 2 kg
            entryUnit = UnitType.KG,
            amountPaidPaise = null,
            transactionEpochDay = yesterday,
            createdAtEpochMs = System.currentTimeMillis() - 86400000L
        )

        fakeTransactionRepository.addTransaction(txYesterday)
        fakeTransactionRepository.addTransaction(txToday)
        testDispatcher.scheduler.advanceUntilIdle()

        val testViewModel = ProductHistoryViewModel(
            productId = testProduct.id,
            productRepository = fakeProductRepository,
            transactionRepository = fakeTransactionRepository
        )

        testDispatcher.scheduler.advanceUntilIdle()

        val state = testViewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("8 kg", state.formattedCurrentStock)
        assertEquals("₹500", state.formattedTotalSpending)
        assertEquals(2, state.dateGroupedTransactions.size)

        // Verify newest date group first
        assertEquals(today, state.dateGroupedTransactions.first().epochDay)
        assertEquals(yesterday, state.dateGroupedTransactions.last().epochDay)
    }

    @Test
    fun editTransaction_validQuantityAndAmount_updatesTransactionAndAuditTrail(): Unit = runTest {
        val today = LocalDate.now(ZoneId.systemDefault())
        val tx = InventoryTransaction(
            id = 1L,
            productId = testProduct.id,
            type = TransactionType.ADD,
            baseQuantity = 10000L, // 10 kg
            entryUnit = UnitType.KG,
            amountPaidPaise = 50000L, // ₹500
            transactionEpochDay = today.toEpochDay(),
            createdAtEpochMs = System.currentTimeMillis()
        )

        fakeTransactionRepository.addTransaction(tx)
        testDispatcher.scheduler.advanceUntilIdle()

        var completed = false
        var errMsg: String? = null

        viewModel.editTransaction(
            tx = tx,
            quantityInput = "12",
            selectedUnit = UnitType.KG,
            amountPaidInput = "600",
            transactionDate = today
        ) { success, err ->
            completed = success
            errMsg = err
        }

        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(completed)
        assertNull(errMsg)

        // Verify updated transaction in repo
        val updatedTx = fakeTransactionRepository.getTransactionsForProduct(testProduct.id).first().first()
        assertEquals(12000L, updatedTx.baseQuantity) // 12 kg = 12000 g
        assertEquals(60000L, updatedTx.amountPaidPaise) // ₹600 = 60000 paise
    }

    @Test
    fun deleteTransaction_within30Mins_deletesTransaction(): Unit = runTest {
        val recordedTime = System.currentTimeMillis()
        val tx = InventoryTransaction(
            id = 1L,
            productId = testProduct.id,
            type = TransactionType.ADD,
            baseQuantity = 5000L, // 5 kg
            entryUnit = UnitType.KG,
            amountPaidPaise = 25000L,
            transactionEpochDay = LocalDate.now(ZoneId.systemDefault()).toEpochDay(),
            createdAtEpochMs = recordedTime
        )

        fakeTransactionRepository.addTransaction(tx)
        testDispatcher.scheduler.advanceUntilIdle()

        // 5 minutes after recording
        val time5MinsLater = recordedTime + (5 * 60 * 1000L)
        assertTrue(viewModel.canDeleteTransaction(tx, time5MinsLater))

        var completed = false
        viewModel.deleteTransaction(tx, time5MinsLater) { success, _ ->
            completed = success
        }

        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(completed)
        val remainingTxs = fakeTransactionRepository.getTransactionsForProduct(testProduct.id).first()
        assertTrue(remainingTxs.isEmpty())
    }

    @Test
    fun deleteTransaction_after30Mins_rejectsDeletion(): Unit = runTest {
        val recordedTime = System.currentTimeMillis()
        val tx = InventoryTransaction(
            id = 1L,
            productId = testProduct.id,
            type = TransactionType.ADD,
            baseQuantity = 5000L, // 5 kg
            entryUnit = UnitType.KG,
            amountPaidPaise = 25000L,
            transactionEpochDay = LocalDate.now(ZoneId.systemDefault()).toEpochDay(),
            createdAtEpochMs = recordedTime
        )

        fakeTransactionRepository.addTransaction(tx)
        testDispatcher.scheduler.advanceUntilIdle()

        // 35 minutes after recording
        val time35MinsLater = recordedTime + (35 * 60 * 1000L)
        assertFalse(viewModel.canDeleteTransaction(tx, time35MinsLater))

        var completed = false
        var errMsg: String? = null
        viewModel.deleteTransaction(tx, time35MinsLater) { success, err ->
            completed = success
            errMsg = err
        }

        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(completed)
        assertEquals("30-minute delete window has expired.", errMsg)

        // Verify transaction still exists
        val remainingTxs = fakeTransactionRepository.getTransactionsForProduct(testProduct.id).first()
        assertEquals(1, remainingTxs.size)
    }
}
