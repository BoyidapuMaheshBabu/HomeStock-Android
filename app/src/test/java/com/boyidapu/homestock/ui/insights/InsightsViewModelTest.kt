package com.boyidapu.homestock.ui.insights

import com.boyidapu.homestock.domain.model.InventoryTransaction
import com.boyidapu.homestock.domain.model.SpendingPeriod
import com.boyidapu.homestock.domain.model.TransactionType
import com.boyidapu.homestock.domain.model.UnitType
import com.boyidapu.homestock.test.fake.FakeTransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
class InsightsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeTransactionRepository: FakeTransactionRepository
    private lateinit var viewModel: InsightsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeTransactionRepository = FakeTransactionRepository()
        viewModel = InsightsViewModel(fakeTransactionRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun insights_defaultState_selectsThisMonth(): Unit = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        val state = viewModel.uiState.value
        assertEquals(SpendingPeriod.THIS_MONTH, state.selectedPeriod)
        assertFalse(state.isLoading)
        assertEquals(0L, state.totalSpendingPaise)
    }

    @Test
    fun selectPeriod_last7Days_filtersSpendingCorrectly(): Unit = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        val today = LocalDate.now(ZoneId.systemDefault())
        val date10DaysAgo = today.minusDays(10)

        // Purchase today: ₹500 (50000 paise)
        fakeTransactionRepository.addTransaction(
            InventoryTransaction(
                productId = 1L,
                type = TransactionType.ADD,
                baseQuantity = 10000L,
                entryUnit = UnitType.KG,
                amountPaidPaise = 50000L,
                transactionEpochDay = today.toEpochDay()
            )
        )

        // Purchase 10 days ago: ₹300 (30000 paise)
        fakeTransactionRepository.addTransaction(
            InventoryTransaction(
                productId = 2L,
                type = TransactionType.ADD,
                baseQuantity = 5000L,
                entryUnit = UnitType.KG,
                amountPaidPaise = 30000L,
                transactionEpochDay = date10DaysAgo.toEpochDay()
            )
        )

        // Select Last 7 Days
        viewModel.selectPeriod(SpendingPeriod.LAST_7_DAYS)

        val state = viewModel.uiState.value
        assertEquals(SpendingPeriod.LAST_7_DAYS, state.selectedPeriod)
        assertEquals(50000L, state.totalSpendingPaise) // Only today's purchase

        // Select All Time
        viewModel.selectPeriod(SpendingPeriod.ALL_TIME)
        assertEquals(80000L, viewModel.uiState.value.totalSpendingPaise) // Both purchases
    }

    @Test
    fun onCustomDatesSelected_validRange_calculatesCustomSpending(): Unit = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        val today = LocalDate.now(ZoneId.systemDefault())
        val date5DaysAgo = today.minusDays(5)
        val date2DaysAgo = today.minusDays(2)

        fakeTransactionRepository.addTransaction(
            InventoryTransaction(
                productId = 1L,
                type = TransactionType.ADD,
                baseQuantity = 2000L,
                entryUnit = UnitType.KG,
                amountPaidPaise = 25000L, // ₹250
                transactionEpochDay = date3DaysAgo(today)
            )
        )

        viewModel.onCustomDatesSelected(date5DaysAgo, date2DaysAgo)

        val state = viewModel.uiState.value
        assertEquals(SpendingPeriod.CUSTOM, state.selectedPeriod)
        assertEquals(25000L, state.totalSpendingPaise)
        assertNull(state.customRangeError)
    }

    private fun date3DaysAgo(today: LocalDate): Long {
        return today.minusDays(3).toEpochDay()
    }

    @Test
    fun onCustomDatesSelected_startDateAfterEndDate_showsError(): Unit = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        val today = LocalDate.now(ZoneId.systemDefault())
        val yesterday = today.minusDays(1)

        viewModel.onCustomDatesSelected(today, yesterday)

        val state = viewModel.uiState.value
        assertEquals("Start date cannot be after end date.", state.customRangeError)
    }

    @Test
    fun useTransactions_doNotAffectSpending(): Unit = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        val today = LocalDate.now(ZoneId.systemDefault())

        // Add ₹500 purchase
        fakeTransactionRepository.addTransaction(
            InventoryTransaction(
                productId = 1L,
                type = TransactionType.ADD,
                baseQuantity = 10000L,
                entryUnit = UnitType.KG,
                amountPaidPaise = 50000L,
                transactionEpochDay = today.toEpochDay()
            )
        )

        // Add USE transaction
        fakeTransactionRepository.addTransaction(
            InventoryTransaction(
                productId = 1L,
                type = TransactionType.USE,
                baseQuantity = 2000L,
                entryUnit = UnitType.KG,
                amountPaidPaise = null,
                transactionEpochDay = today.toEpochDay()
            )
        )

        viewModel.selectPeriod(SpendingPeriod.THIS_MONTH)

        val state = viewModel.uiState.value
        assertEquals(50000L, state.totalSpendingPaise) // Spending remains ₹500
    }
}
