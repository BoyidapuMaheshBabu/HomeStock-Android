package com.boyidapu.homestock.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boyidapu.homestock.domain.model.FixedCategory
import com.boyidapu.homestock.domain.model.MeasurementDimension
import com.boyidapu.homestock.domain.model.Product
import com.boyidapu.homestock.domain.model.UnitType
import com.boyidapu.homestock.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateProductUiState(
    val categoryId: String = FixedCategory.FOOD_KITCHEN.id,
    val nameInput: String = "",
    val selectedDimension: MeasurementDimension = MeasurementDimension.MASS,
    val selectedUnit: UnitType = UnitType.KG,
    val errorMessage: String? = null,
    val isSaving: Boolean = false,
    val isSaveSuccess: Boolean = false
)

class CreateProductViewModel(
    val categoryId: String,
    private val productRepository: ProductRepository
) : ViewModel() {

    companion object {
        const val MAX_PRODUCT_NAME_LENGTH = 40
    }

    private val _uiState = MutableStateFlow(
        CreateProductUiState(
            categoryId = categoryId,
            selectedDimension = MeasurementDimension.MASS,
            selectedUnit = UnitType.KG
        )
    )
    val uiState: StateFlow<CreateProductUiState> = _uiState.asStateFlow()

    fun onNameInputChanged(input: String) {
        val truncated = if (input.length > MAX_PRODUCT_NAME_LENGTH) input.take(MAX_PRODUCT_NAME_LENGTH) else input
        _uiState.update {
            it.copy(nameInput = truncated, errorMessage = null)
        }
    }

    fun onDimensionSelected(dimension: MeasurementDimension) {
        val defaultUnit = when (dimension) {
            MeasurementDimension.MASS -> UnitType.KG
            MeasurementDimension.VOLUME -> UnitType.L
            MeasurementDimension.COUNT -> UnitType.PIECES
        }
        _uiState.update {
            it.copy(
                selectedDimension = dimension,
                selectedUnit = defaultUnit
            )
        }
    }

    fun onUnitSelected(unit: UnitType) {
        if (unit.dimension == _uiState.value.selectedDimension) {
            _uiState.update { it.copy(selectedUnit = unit) }
        }
    }

    fun saveProduct() {
        val trimmedName = _uiState.value.nameInput.trim()
        if (trimmedName.isEmpty()) {
            _uiState.update {
                it.copy(errorMessage = "Please enter a product name")
            }
            return
        }

        if (trimmedName.length > MAX_PRODUCT_NAME_LENGTH) {
            _uiState.update {
                it.copy(errorMessage = "Product name must be at most $MAX_PRODUCT_NAME_LENGTH characters")
            }
            return
        }

        val category = FixedCategory.fromId(categoryId) ?: FixedCategory.FOOD_KITCHEN

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                val newProduct = Product(
                    category = category,
                    name = trimmedName.take(MAX_PRODUCT_NAME_LENGTH),
                    dimension = _uiState.value.selectedDimension,
                    isPreset = false,
                    isArchived = false,
                    createdAtEpochMs = System.currentTimeMillis()
                )
                productRepository.insertProduct(newProduct)
                _uiState.update {
                    it.copy(isSaving = false, isSaveSuccess = true)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = "Failed to save product"
                    )
                }
            }
        }
    }
}
