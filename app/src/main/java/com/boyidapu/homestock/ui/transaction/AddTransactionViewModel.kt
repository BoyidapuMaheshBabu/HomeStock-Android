package com.boyidapu.homestock.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boyidapu.homestock.core.dates.DateUtils
import com.boyidapu.homestock.core.money.MoneyUtils
import com.boyidapu.homestock.core.quantity.QuantityUtils
import com.boyidapu.homestock.domain.model.InventoryTransaction
import com.boyidapu.homestock.domain.model.MeasurementDimension
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

data class AddTransactionUiState(
    val product: Product? = null,
    val quantityInput: String = "",
    val selectedUnit: UnitType = UnitType.KG,
    val amountPaidInput: String = "",
    val transactionDate: LocalDate = LocalDate.now(ZoneId.systemDefault()),
    val quantityError: String? = null,
    val amountError: String? = null,
    val dateError: String? = null,
    val isSaving: Boolean = false,
    val canSave: Boolean = false
)

sealed interface AddTransactionEvent {
    data object SaveSuccess : AddTransactionEvent
    data class ShowError(val message: String) : AddTransactionEvent
}

class AddTransactionViewModel(
    val productId: Long,
    private val productRepository: ProductRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<AddTransactionEvent>()
    val eventFlow: SharedFlow<AddTransactionEvent> = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            val product = productRepository.getProductById(productId).firstOrNull()
            if (product != null) {
                _uiState.update {
                    it.copy(
                        product = product,
                        selectedUnit = product.preferredUnit
                    )
                }
            }
        }
    }

    fun onQuantityInputChanged(input: String) {
        _uiState.update { state ->
            val product = state.product
            var error: String? = null
            if (input.trim().isNotEmpty()) {
                val baseQty = QuantityUtils.toBaseQuantity(input, state.selectedUnit)
                if (baseQty == null || baseQty <= 0) {
                    error = if (state.selectedUnit == UnitType.KG || state.selectedUnit == UnitType.L) {
                        "Enter valid quantity (max 3 decimals)"
                    } else {
                        "Enter valid whole number quantity"
                    }
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
                val newState = state.copy(selectedUnit = unit)
                val baseQty = QuantityUtils.toBaseQuantity(newState.quantityInput, unit)
                var error: String? = null
                if (newState.quantityInput.trim().isNotEmpty() && (baseQty == null || baseQty <= 0)) {
                    error = "Invalid quantity for unit ${unit.symbol}"
                }
                val validatedState = newState.copy(quantityError = error)
                validatedState.copy(canSave = validateForm(validatedState))
            }
        }
    }

    fun onAmountPaidInputChanged(input: String) {
        _uiState.update { state ->
            var error: String? = null
            if (input.trim().isNotEmpty()) {
                val paise = MoneyUtils.parseRupeesToPaise(input)
                if (paise == null || paise < 0) {
                    error = "Enter valid amount in ₹ (max 2 decimals)"
                }
            }
            val newState = state.copy(
                amountPaidInput = input,
                amountError = error
            )
            newState.copy(canSave = validateForm(newState))
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
                transactionDate = date,
                dateError = error
            )
            newState.copy(canSave = validateForm(newState))
        }
    }

    private fun validateForm(state: AddTransactionUiState): Boolean {
        if (state.product == null) return false
        if (state.isSaving) return false

        val qtyBase = QuantityUtils.toBaseQuantity(state.quantityInput, state.selectedUnit)
        if (qtyBase == null || qtyBase <= 0L) return false

        val paise = MoneyUtils.parseRupeesToPaise(state.amountPaidInput)
        if (paise == null || paise < 0L) return false

        val today = LocalDate.now(ZoneId.systemDefault())
        if (state.transactionDate.isAfter(today)) return false

        return state.quantityError == null && state.amountError == null && state.dateError == null
    }

    fun saveAddTransaction() {
        val currentState = _uiState.value
        if (!currentState.canSave) return

        val baseQuantity = QuantityUtils.toBaseQuantity(currentState.quantityInput, currentState.selectedUnit) ?: return
        val amountPaidPaise = MoneyUtils.parseRupeesToPaise(currentState.amountPaidInput) ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, canSave = false) }
            try {
                val transaction = InventoryTransaction(
                    productId = productId,
                    type = TransactionType.ADD,
                    baseQuantity = baseQuantity,
                    entryUnit = currentState.selectedUnit,
                    amountPaidPaise = amountPaidPaise,
                    transactionEpochDay = currentState.transactionDate.toEpochDay()
                )
                transactionRepository.addTransaction(transaction)
                _uiState.update { it.copy(isSaving = false) }
                _eventFlow.emit(AddTransactionEvent.SaveSuccess)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        canSave = true,
                        amountError = "Failed to record purchase"
                    )
                }
            }
        }
    }
}
