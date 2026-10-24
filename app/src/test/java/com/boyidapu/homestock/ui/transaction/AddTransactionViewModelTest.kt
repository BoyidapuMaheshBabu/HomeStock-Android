package com.boyidapu.homestock.ui.transaction

import com.boyidapu.homestock.domain.model.FixedCategory
import com.boyidapu.homestock.domain.model.MeasurementDimension
import com.boyidapu.homestock.domain.model.Product
import com.boyidapu.homestock.domain.model.TransactionType
import com.boyidapu.homestock.domain.model.UnitType
import com.boyidapu.homestock.test.fake.FakeProductRepository
import com.boyidapu.homestock.test.fake.FakeTransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class AddTransactionViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeProductRepository: FakeProductRepository
    private lateinit var fakeTransactionRepository: FakeTransactionRepository
    private lateinit var viewModel: AddTransactionViewModel

    private val testProduct = Product(
        id = 101L,
        category = FixedCategory.FOOD_KITCHEN,
        name = "Basmati Rice",
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

        viewModel = AddTransactionViewModel(
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
    fun saveAddTransaction_validInput_insertsAddTransactionAndEmitsSuccess(): Unit = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onQuantityInputChanged("10")
        viewModel.onUnitSelected(UnitType.KG)
        viewModel.onAmountPaidInputChanged("500")

        val state = viewModel.uiState.value
        assertTrue(state.canSave)

        viewModel.saveAddTransaction()
        testDispatcher.scheduler.advanceUntilIdle()

        val savedTxs = fakeTransactionRepository.transactions.value
        assertEquals(1, savedTxs.size)

        val tx = savedTxs.first()
        assertEquals(testProduct.id, tx.productId)
        assertEquals(TransactionType.ADD, tx.type)
        assertEquals(10000L, tx.baseQuantity) // 10 kg = 10000 g
        assertEquals(UnitType.KG, tx.entryUnit)
        assertEquals(50000L, tx.amountPaidPaise) // ₹500 = 50000 paise
    }

    @Test
    fun saveAddTransaction_freePurchaseZeroRupees_insertsStockAndZeroSpending(): Unit = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onQuantityInputChanged("2.5")
        viewModel.onUnitSelected(UnitType.KG)
        viewModel.onAmountPaidInputChanged("0")

        assertTrue(viewModel.uiState.value.canSave)

        viewModel.saveAddTransaction()
        testDispatcher.scheduler.advanceUntilIdle()

        val savedTxs = fakeTransactionRepository.transactions.value
        assertEquals(1, savedTxs.size)

        val tx = savedTxs.first()
        assertEquals(2500L, tx.baseQuantity) // 2.5 kg = 2500 g
        assertEquals(0L, tx.amountPaidPaise) // ₹0.00 = 0 paise
    }

    @Test
    fun onQuantityInputChanged_invalidQuantityOrNegativeAmount_disablesSave(): Unit = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        // Invalid decimal count for kg (>3 decimals)
        viewModel.onQuantityInputChanged("1.2345")
        viewModel.onAmountPaidInputChanged("100")

        assertFalse(viewModel.uiState.value.canSave)

        // Invalid negative amount
        viewModel.onQuantityInputChanged("2")
        viewModel.onAmountPaidInputChanged("-50")

        assertFalse(viewModel.uiState.value.canSave)
    }

    @Test
    fun onDateSelected_futureDate_disablesSave(): Unit = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onQuantityInputChanged("5")
        viewModel.onAmountPaidInputChanged("200")

        val tomorrow = LocalDate.now(ZoneId.systemDefault()).plusDays(1)
        viewModel.onDateSelected(tomorrow)

        assertFalse(viewModel.uiState.value.canSave)
        assertEquals("Future dates are not allowed", viewModel.uiState.value.dateError)
    }
}
