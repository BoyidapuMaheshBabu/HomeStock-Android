package com.boyidapu.homestock.ui.transaction

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
class UseTransactionViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeProductRepository: FakeProductRepository
    private lateinit var fakeTransactionRepository: FakeTransactionRepository
    private lateinit var viewModel: UseTransactionViewModel

    private val testProduct = Product(
        id = 202L,
        category = FixedCategory.FOOD_KITCHEN,
        name = "Sona Masoori Rice",
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

        viewModel = UseTransactionViewModel(
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
    fun saveUseTransaction_validQuantity_reducesStockAndDoesNotChangeSpending(): Unit = runTest {
        // Add 10 kg purchase for ₹500
        fakeTransactionRepository.addTransaction(
            InventoryTransaction(
                productId = testProduct.id,
                type = TransactionType.ADD,
                baseQuantity = 10000L, // 10 kg
                entryUnit = UnitType.KG,
                amountPaidPaise = 50000L, // ₹500
                transactionEpochDay = LocalDate.now(ZoneId.systemDefault()).toEpochDay()
            )
        )

        testDispatcher.scheduler.advanceUntilIdle()

        // Use 1 kg
        viewModel.onQuantityInputChanged("1")
        viewModel.onUnitSelected(UnitType.KG)

        val state = viewModel.uiState.value
        assertTrue(state.canSave)

        viewModel.saveUseTransaction()
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify USE transaction created
        val allTxs = fakeTransactionRepository.transactions.value
        assertEquals(2, allTxs.size)

        val useTx = allTxs.last()
        assertEquals(TransactionType.USE, useTx.type)
        assertEquals(1000L, useTx.baseQuantity) // 1 kg = 1000 g
        assertNull(useTx.amountPaidPaise) // USE has no amount paid

        // Verify remaining stock is 9000 g (9 kg)
        val remainingStock = fakeTransactionRepository.getProductStockBaseQuantity(testProduct.id).first()
        assertEquals(9000L, remainingStock)

        // Verify spending remains ₹500 (50000 paise)
        val spending = fakeTransactionRepository.getAllTimeSpending().first()
        assertEquals(50000L, spending)
    }

    @Test
    fun saveUseTransaction_requestedQuantityExceedsAvailableStock_disablesSaveAndBlocksTransaction(): Unit = runTest {
        // Add 2 kg purchase
        fakeTransactionRepository.addTransaction(
            InventoryTransaction(
                productId = testProduct.id,
                type = TransactionType.ADD,
                baseQuantity = 2000L, // 2 kg
                entryUnit = UnitType.KG,
                amountPaidPaise = 10000L,
                transactionEpochDay = LocalDate.now(ZoneId.systemDefault()).toEpochDay()
            )
        )

        testDispatcher.scheduler.advanceUntilIdle()

        // Try to use 5 kg
        viewModel.onQuantityInputChanged("5")
        viewModel.onUnitSelected(UnitType.KG)

        val state = viewModel.uiState.value
        assertFalse(state.canSave)
        assertTrue(state.quantityError?.contains("exceeds available stock") == true)

        // Verify stock remains 2000 g
        val remainingStock = fakeTransactionRepository.getProductStockBaseQuantity(testProduct.id).first()
        assertEquals(2000L, remainingStock)
    }

    @Test
    fun zeroStockProduct_disablesUseTransaction(): Unit = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onQuantityInputChanged("1")
        val state = viewModel.uiState.value

        assertFalse(state.canSave)
        assertEquals("0 kg", state.formattedCurrentStock)
    }
}
