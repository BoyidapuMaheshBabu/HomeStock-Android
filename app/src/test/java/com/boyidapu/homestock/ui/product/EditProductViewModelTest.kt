package com.boyidapu.homestock.ui.product

import com.boyidapu.homestock.domain.model.FixedCategory
import com.boyidapu.homestock.domain.model.MeasurementDimension
import com.boyidapu.homestock.domain.model.Product
import com.boyidapu.homestock.test.fake.FakeProductRepository
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

@OptIn(ExperimentalCoroutinesApi::class)
class EditProductViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeProductRepository: FakeProductRepository
    private lateinit var viewModel: EditProductViewModel
    private val testProduct = Product(
        id = 100L,
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
        fakeProductRepository.addTestProduct(testProduct, transactionCount = 0)
        viewModel = EditProductViewModel(
            productId = testProduct.id,
            productRepository = fakeProductRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadProduct_initiallyUnchanged_canSaveIsFalse(): Unit = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Basmati Rice", state.nameInput)
        assertFalse(state.isChanged)
        assertFalse(state.canSave)
    }

    @Test
    fun scenarioA_changeTypeAndSave_thenReopenAndRestoreType(): Unit = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        // 1. Initial state: MASS ("Mass")
        assertEquals(MeasurementDimension.MASS, viewModel.uiState.value.selectedDimension)

        // 2. Change type to VOLUME ("Liquid")
        viewModel.onDimensionSelected(MeasurementDimension.VOLUME)
        assertTrue(viewModel.uiState.value.isChanged)
        assertTrue(viewModel.uiState.value.canSave)

        // 3. Save
        viewModel.saveProduct()
        testDispatcher.scheduler.advanceUntilIdle()

        // 4. Re-open / loadProduct in next session
        viewModel.loadProduct()
        testDispatcher.scheduler.advanceUntilIdle()

        val stateSession2 = viewModel.uiState.value
        assertEquals(MeasurementDimension.VOLUME, stateSession2.selectedDimension)
        assertEquals(MeasurementDimension.VOLUME, stateSession2.originalDimension)
        assertFalse(stateSession2.canSave)

        // 5. Change VOLUME back to MASS
        viewModel.onDimensionSelected(MeasurementDimension.MASS)
        assertTrue(viewModel.uiState.value.isChanged)
        assertTrue(viewModel.uiState.value.canSave)

        // 6. Save back
        viewModel.saveProduct()
        testDispatcher.scheduler.advanceUntilIdle()

        // 7. Re-open again
        viewModel.loadProduct()
        testDispatcher.scheduler.advanceUntilIdle()

        val stateSession3 = viewModel.uiState.value
        assertEquals(MeasurementDimension.MASS, stateSession3.selectedDimension)
        assertEquals(MeasurementDimension.MASS, stateSession3.originalDimension)
        assertFalse(stateSession3.canSave)
    }

    @Test
    fun scenarioB_changeAndRevertBeforeSaving_disablesSave(): Unit = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        // Initial: MASS
        assertEquals(MeasurementDimension.MASS, viewModel.uiState.value.selectedDimension)
        assertFalse(viewModel.uiState.value.canSave)

        // Select VOLUME
        viewModel.onDimensionSelected(MeasurementDimension.VOLUME)
        assertTrue(viewModel.uiState.value.canSave)

        // Revert to MASS without saving
        viewModel.onDimensionSelected(MeasurementDimension.MASS)
        assertFalse(viewModel.uiState.value.isChanged)
        assertFalse(viewModel.uiState.value.canSave)
    }

    @Test
    fun scenarioC_repeatTypeTransitionsAcrossSessions(): Unit = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        // Session 1: MASS -> VOLUME
        viewModel.onDimensionSelected(MeasurementDimension.VOLUME)
        viewModel.saveProduct()
        testDispatcher.scheduler.advanceUntilIdle()

        // Session 2: VOLUME -> COUNT
        viewModel.loadProduct()
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(MeasurementDimension.VOLUME, viewModel.uiState.value.selectedDimension)

        viewModel.onDimensionSelected(MeasurementDimension.COUNT)
        assertTrue(viewModel.uiState.value.canSave)
        viewModel.saveProduct()
        testDispatcher.scheduler.advanceUntilIdle()

        // Session 3: COUNT -> MASS
        viewModel.loadProduct()
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(MeasurementDimension.COUNT, viewModel.uiState.value.selectedDimension)

        viewModel.onDimensionSelected(MeasurementDimension.MASS)
        assertTrue(viewModel.uiState.value.canSave)
        viewModel.saveProduct()
        testDispatcher.scheduler.advanceUntilIdle()

        // Session 4: Verified MASS
        viewModel.loadProduct()
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(MeasurementDimension.MASS, viewModel.uiState.value.selectedDimension)
    }

    @Test
    fun typeLocking_whenTransactionsExist_dimensionChangeIsBlocked(): Unit = runTest {
        testDispatcher.scheduler.advanceUntilIdle()

        // Add product with transaction count > 0
        val historyProduct = Product(
            id = 200L,
            category = FixedCategory.FOOD_KITCHEN,
            name = "Oil",
            dimension = MeasurementDimension.VOLUME,
            isPreset = false,
            isArchived = false
        )
        fakeProductRepository.addTestProduct(historyProduct, transactionCount = 1)

        val historyViewModel = EditProductViewModel(200L, fakeProductRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(historyViewModel.uiState.value.hasTransactions)

        // Attempting to change type is blocked
        historyViewModel.onDimensionSelected(MeasurementDimension.MASS)
        assertEquals(MeasurementDimension.VOLUME, historyViewModel.uiState.value.selectedDimension)
        assertFalse(historyViewModel.uiState.value.canSave)
    }
}
