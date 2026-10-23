package com.boyidapu.homestock.ui.archived

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boyidapu.homestock.domain.model.ProductWithStock
import com.boyidapu.homestock.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ArchivedProductsUiState(
    val archivedProducts: List<ProductWithStock> = emptyList(),
    val isLoading: Boolean = true
)

sealed interface ArchivedEvent {
    data class ShowMessage(val message: String) : ArchivedEvent
}

class ArchivedProductsViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<ArchivedEvent>()
    val eventFlow: SharedFlow<ArchivedEvent> = _eventFlow.asSharedFlow()

    val uiState: StateFlow<ArchivedProductsUiState> = productRepository
        .getArchivedProductsWithStock()
        .map { list ->
            ArchivedProductsUiState(
                archivedProducts = list,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ArchivedProductsUiState(isLoading = true)
        )

    fun restoreProduct(productId: Long) {
        viewModelScope.launch {
            productRepository.restoreProduct(productId)
            _eventFlow.emit(ArchivedEvent.ShowMessage("Product restored to active category."))
        }
    }
}
