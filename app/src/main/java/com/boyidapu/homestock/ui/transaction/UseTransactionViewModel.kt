package com.boyidapu.homestock.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boyidapu.homestock.core.quantity.QuantityUtils
import com.boyidapu.homestock.domain.model.InventoryTransaction
import com.boyidapu.homestock.domain.model.Product
import com.boyidapu.homestock.domain.model.TransactionType
import com.boyidapu.homestock.domain.model.UnitType
import com.boyidapu.homestock.domain.repository.ProductRepository
import com.boyidapu.homestock.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

data class UseTransactionUiState(
    val product: Product? = null,
    val stockBaseQuantity: Long = 0L,
    val formattedCurrentStock: String = "0",
    val quantityInput: String = "",
    val selectedUnit: UnitType = UnitType.KG,
    val usageDate: LocalDate = LocalDate.now(ZoneId.systemDefault()),
    val quantityError: String? = null,
    val dateError: String? = null,
    val isSaving: Boolean = false,
    val canSave: Boolean = false
)

sealed interface UseTransactionEvent {
    data object SaveSuccess : UseTransactionEvent
    data class ShowError(val message: String) : UseTransactionEvent
}

class UseTransactionViewModel(
    val productId: Long,
    private val productRepository: ProductRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UseTransactionUiState())
    val uiState: StateFlow<UseTransactionUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UseTransactionEvent>()
    val eventFlow: SharedFlow<UseTransactionEvent> = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            val product = productRepository.getProductById(productId).firstOrNull()
            if (product != null) {
                combine(
                    productRepository.getProductById(productId),
                    transactionRepository.getProductStockBaseQuantity(productId)
                ) { p, stock ->
                    if (p != null) {
                        val formattedStock = QuantityUtils.autoFormatStock(stock, p.dimension)
                        _uiState.update { state ->
                            val newState = state.copy(
                                product = p,
                                stockBaseQuantity = stock,
                                formattedCurrentStock = formattedStock,
                                selectedUnit = if (state.quantityInput.isEmpty()) p.preferredUnit else state.selectedUnit
                            )
                            newState.copy(canSave = validateForm(newState))
                        }
                    }
                }.collect {}
            }
        }
    }

    fun onQuantityInputChanged(input: String) {
        _uiState.update { state ->
            var error: String? = null
            if (input.trim().isNotEmpty()) {
                val baseQty = QuantityUtils.toBaseQuantity(input, state.selectedUnit)
                if (baseQty == null || baseQty <= 0) {
                    error = if (state.selectedUnit == UnitType.KG || state.selectedUnit == UnitType.L) {
                        "Enter valid quantity (max 3 decimals)"
                    } else {
                        "Enter valid whole number quantity"
                    }
                } else if (baseQty > state.stockBaseQuantity) {
                    error = "Requested quantity exceeds available stock (${state.formattedCurrentStock})"
                }
            }
            val newState = state.copy(
                quantityInput = input,
                quantityError = error
            )
            newState.copy(canSave = validateForm(newState))
        }
    }

    fun onUnitSelected(unit: UnitType) {
        val product = _uiState.value.product ?: return
        if (unit.dimension == product.dimension) {
            _uiState.update { state ->
                var error: String? = null
                val baseQty = QuantityUtils.toBaseQuantity(state.quantityInput, unit)
                if (state.quantityInput.trim().isNotEmpty()) {
                    if (baseQty == null || baseQty <= 0) {
                        error = "Invalid quantity for unit ${unit.symbol}"
                    } else if (baseQty > state.stockBaseQuantity) {
                        error = "Requested quantity exceeds available stock (${state.formattedCurrentStock})"
                    }
                }
                val newState = state.copy(
                    selectedUnit = unit,
                    quantityError = error
                )
                newState.copy(canSave = validateForm(newState))
            }
        }
    }

    fun onDateSelected(date: LocalDate) {
        val today = LocalDate.now(ZoneId.systemDefault())
        var error: String? = null
        if (date.isAfter(today)) {
            error = "Future dates are not allowed"
        }
        _uiState.update { state ->
            val newState = state.copy(
                usageDate = date,
                dateError = error
            )
            newState.copy(canSave = validateForm(newState))
        }
    }

    private fun validateForm(state: UseTransactionUiState): Boolean {
        if (state.product == null) return false
        if (state.isSaving) return false
        if (state.stockBaseQuantity <= 0L) return false

        val qtyBase = QuantityUtils.toBaseQuantity(state.quantityInput, state.selectedUnit)
        if (qtyBase == null || qtyBase <= 0L) return false
        if (qtyBase > state.stockBaseQuantity) return false

        val today = LocalDate.now(ZoneId.systemDefault())
        if (state.usageDate.isAfter(today)) return false

        return state.quantityError == null && state.dateError == null
    }

    fun saveUseTransaction() {
        val currentState = _uiState.value
        if (!currentState.canSave) return

        val baseQuantity = QuantityUtils.toBaseQuantity(currentState.quantityInput, currentState.selectedUnit) ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, canSave = false) }
            try {
                val transaction = InventoryTransaction(
                    productId = productId,
                    type = TransactionType.USE,
                    baseQuantity = baseQuantity,
                    entryUnit = currentState.selectedUnit,
                    amountPaidPaise = null,
                    transactionEpochDay = currentState.usageDate.toEpochDay()
                )
                val success = transactionRepository.useTransactionAtomic(transaction)
                if (success) {
                    _uiState.update { it.copy(isSaving = false) }
                    _eventFlow.emit(UseTransactionEvent.SaveSuccess)
                } else {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            canSave = false,
                            quantityError = "Stock changed in background. Requested quantity exceeds available stock."
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        canSave = true,
                        quantityError = "Failed to record usage"
                    )
                }
            }
        }
    }
}
