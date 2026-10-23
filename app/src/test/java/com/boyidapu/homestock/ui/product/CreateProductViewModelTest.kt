package com.boyidapu.homestock.ui.product

import com.boyidapu.homestock.domain.model.FixedCategory
import com.boyidapu.homestock.domain.model.MeasurementDimension
import com.boyidapu.homestock.domain.model.UnitType
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
class CreateProductViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeProductRepository: FakeProductRepository
    private lateinit var viewModel: CreateProductViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeProductRepository = FakeProductRepository()
        viewModel = CreateProductViewModel(
            categoryId = FixedCategory.FOOD_KITCHEN.id,
            productRepository = fakeProductRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun saveProduct_blankName_showsErrorMessage() {
        viewModel.onNameInputChanged("   ")
        viewModel.saveProduct()

        assertEquals("Please enter a product name", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isSaveSuccess)
    }

    @Test
    fun onNameInputChanged_over40Chars_truncatesTo40Chars() {
        val longName = "A".repeat(50)
        viewModel.onNameInputChanged(longName)

        assertEquals(40, viewModel.uiState.value.nameInput.length)
        assertEquals("A".repeat(40), viewModel.uiState.value.nameInput)
    }

    @Test
    fun onDimensionSelected_updatesDefaultUnit() {
        viewModel.onDimensionSelected(MeasurementDimension.VOLUME)
        assertEquals(MeasurementDimension.VOLUME, viewModel.uiState.value.selectedDimension)
        assertEquals(UnitType.L, viewModel.uiState.value.selectedUnit)

        viewModel.onDimensionSelected(MeasurementDimension.COUNT)
        assertEquals(MeasurementDimension.COUNT, viewModel.uiState.value.selectedDimension)
        assertEquals(UnitType.PIECES, viewModel.uiState.value.selectedUnit)
    }

    @Test
    fun saveProduct_validName_trimsAndSavesProduct(): Unit = runTest {
        viewModel.onNameInputChanged("  Basmati Rice  ")
        viewModel.onDimensionSelected(MeasurementDimension.MASS)

        viewModel.saveProduct()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isSaveSuccess)
    }
}
