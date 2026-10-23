package com.boyidapu.homestock.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boyidapu.homestock.domain.model.MeasurementDimension
import com.boyidapu.homestock.domain.model.Product
import com.boyidapu.homestock.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditProductUiState(
    val product: Product? = null,
    val originalName: String = "",
    val originalDimension: MeasurementDimension = MeasurementDimension.MASS,
    val nameInput: String = "",
    val selectedDimension: MeasurementDimension = MeasurementDimension.MASS,
    val hasTransactions: Boolean = false,
    val isChanged: Boolean = false,
    val canSave: Boolean = false,
    val errorMessage: String? = null,
    val isSaving: Boolean = false
)

sealed interface EditProductEvent {
    data object SaveSuccess : EditProductEvent
    data class ShowError(val message: String) : EditProductEvent
}

class EditProductViewModel(
    val productId: Long,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProductUiState())
    val uiState: StateFlow<EditProductUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<EditProductEvent>()
    val eventFlow: SharedFlow<EditProductEvent> = _eventFlow.asSharedFlow()

    init {
        loadProduct()
    }

    fun loadProduct() {
        viewModelScope.launch {
            val product = productRepository.getProductById(productId).firstOrNull()
            if (product != null) {
                val count = productRepository.getTransactionCountForProduct(productId)
                _uiState.update {
                    it.copy(
                        product = product,
                        originalName = product.name,
                        originalDimension = product.dimension,
                        nameInput = product.name,
                        selectedDimension = product.dimension,
                        hasTransactions = count > 0,
                        isChanged = false,
                        canSave = false,
                        errorMessage = null,
                        isSaving = false
                    )
                }
            }
        }
    }

    fun onNameInputChanged(input: String) {
        val truncated = if (input.length > CreateProductViewModel.MAX_PRODUCT_NAME_LENGTH) {
            input.take(CreateProductViewModel.MAX_PRODUCT_NAME_LENGTH)
        } else {
            input
        }
        _uiState.update { state ->
            val isNameChanged = truncated.trim() != state.originalName.trim()
            val isDimensionChanged = state.selectedDimension != state.originalDimension
            val isChanged = isNameChanged || isDimensionChanged
            val isValid = truncated.trim().isNotEmpty() && truncated.trim().length <= CreateProductViewModel.MAX_PRODUCT_NAME_LENGTH
            state.copy(
                nameInput = truncated,
                isChanged = isChanged,
                canSave = isChanged && isValid && !state.isSaving,
                errorMessage = null
            )
        }
    }

    fun onDimensionSelected(dimension: MeasurementDimension) {
        if (!_uiState.value.hasTransactions) {
            _uiState.update { state ->
                val isNameChanged = state.nameInput.trim() != state.originalName.trim()
                val isDimensionChanged = dimension != state.originalDimension
                val isChanged = isNameChanged || isDimensionChanged
                val isValid = state.nameInput.trim().isNotEmpty() && state.nameInput.trim().length <= CreateProductViewModel.MAX_PRODUCT_NAME_LENGTH
                state.copy(
                    selectedDimension = dimension,
                    isChanged = isChanged,
                    canSave = isChanged && isValid && !state.isSaving
                )
            }
        }
    }

    fun saveProduct() {
        val currentProduct = _uiState.value.product ?: return
        val currentState = _uiState.value
        if (!currentState.canSave) return

        val trimmedName = currentState.nameInput.trim()
        if (trimmedName.isEmpty()) {
            _uiState.update {
                it.copy(errorMessage = "Please enter a product name", canSave = false)
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, canSave = false) }
            try {
                val newDimension = currentState.selectedDimension
                val updatedProduct = currentProduct.copy(
                    name = trimmedName.take(CreateProductViewModel.MAX_PRODUCT_NAME_LENGTH),
                    dimension = newDimension
                )
                productRepository.updateProduct(updatedProduct)
                _uiState.update {
                    it.copy(
                        product = updatedProduct,
                        originalName = updatedProduct.name,
                        originalDimension = updatedProduct.dimension,
                        nameInput = updatedProduct.name,
                        selectedDimension = updatedProduct.dimension,
                        isSaving = false,
                        isChanged = false,
                        canSave = false
                    )
                }
                _eventFlow.emit(EditProductEvent.SaveSuccess)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        canSave = true,
                        errorMessage = "Failed to update product"
                    )
                }
            }
        }
    }
}
