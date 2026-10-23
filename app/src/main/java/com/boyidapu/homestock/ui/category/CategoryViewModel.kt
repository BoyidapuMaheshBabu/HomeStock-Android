package com.boyidapu.homestock.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boyidapu.homestock.domain.model.FixedCategory
import com.boyidapu.homestock.domain.model.ProductWithStock
import com.boyidapu.homestock.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CategoryUiState(
    val category: FixedCategory? = null,
    val productsWithStock: List<ProductWithStock> = emptyList(),
    val isLoading: Boolean = true
)

sealed interface CategoryEvent {
    data class ShowMessage(val message: String) : CategoryEvent
}

class CategoryViewModel(
    val categoryId: String,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val category = FixedCategory.fromId(categoryId)

    private val _eventFlow = MutableSharedFlow<CategoryEvent>()
    val eventFlow: SharedFlow<CategoryEvent> = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            productRepository.ensureCatalogSeeded()
        }
    }

    val uiState: StateFlow<CategoryUiState> = if (category != null) {
        productRepository.getProductsWithStockByCategory(category)
            .map { list ->
                CategoryUiState(
                    category = category,
                    productsWithStock = list,
                    isLoading = false
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = CategoryUiState(category = category, isLoading = true)
            )
    } else {
        MutableStateFlow(CategoryUiState(isLoading = false))
    }

    fun deleteProduct(productId: Long) {
        viewModelScope.launch {
            val success = productRepository.deleteProductAtomically(productId)
            if (success) {
                _eventFlow.emit(CategoryEvent.ShowMessage("Product deleted."))
            } else {
                _eventFlow.emit(CategoryEvent.ShowMessage("Failed to delete product."))
            }
        }
    }

    fun archiveProduct(productId: Long) {
        viewModelScope.launch {
            val success = productRepository.archiveProduct(productId)
            if (success) {
                _eventFlow.emit(CategoryEvent.ShowMessage("Product archived."))
            } else {
                _eventFlow.emit(CategoryEvent.ShowMessage("Failed to archive product."))
            }
        }
    }

    fun onPlusClicked(productName: String) {
        viewModelScope.launch {
            _eventFlow.emit(CategoryEvent.ShowMessage("Purchase and usage recording coming in Phase 04."))
        }
    }
}
