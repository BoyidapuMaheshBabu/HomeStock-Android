package com.boyidapu.homestock.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boyidapu.homestock.core.dates.DateUtils
import com.boyidapu.homestock.core.money.MoneyUtils
import com.boyidapu.homestock.core.quantity.QuantityUtils
import com.boyidapu.homestock.domain.model.InventoryTransaction
import com.boyidapu.homestock.domain.model.Product
import com.boyidapu.homestock.domain.model.TransactionEditAudit
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

data class DateGroupedTransactions(
    val dateLabel: String,
    val epochDay: Long,
    val transactions: List<InventoryTransaction>
)

data class ProductHistoryUiState(
    val product: Product? = null,
    val stockBaseQuantity: Long = 0L,
    val formattedCurrentStock: String = "0",
    val totalSpendingPaise: Long = 0L,
    val formattedTotalSpending: String = "₹0",
    val dateGroupedTransactions: List<DateGroupedTransactions> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

sealed interface ProductHistoryEvent {
    data class ShowMessage(val message: String) : ProductHistoryEvent
}

class ProductHistoryViewModel(
    val productId: Long,
    private val productRepository: ProductRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductHistoryUiState())
    val uiState: StateFlow<ProductHistoryUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<ProductHistoryEvent>()
    val eventFlow: SharedFlow<ProductHistoryEvent> = _eventFlow.asSharedFlow()

    init {
        loadHistoryData()
    }

    fun loadHistoryData() {
        viewModelScope.launch {
            combine(
                productRepository.getProductById(productId),
                transactionRepository.getProductStockBaseQuantity(productId),
                transactionRepository.getProductTotalSpending(productId),
                transactionRepository.getTransactionsForProduct(productId)
            ) { p, stock, spendingPaise, txList ->
                if (p != null) {
                    val formattedStock = QuantityUtils.autoFormatStock(stock, p.dimension)
                    val formattedSpending = MoneyUtils.formatPaiseToRupees(spendingPaise)

                    // Group by date
                    val grouped = txList
                        .groupBy { it.transactionEpochDay }
                        .entries
                        .sortedByDescending { it.key }
                        .map { (epochDay, list) ->
                            val sortedList = list.sortedWith(
                                compareByDescending<InventoryTransaction> { it.createdAtEpochMs }
                                    .thenByDescending { it.id }
                            )
                            DateGroupedTransactions(
                                dateLabel = DateUtils.formatEpochDay(epochDay),
                                epochDay = epochDay,
                                transactions = sortedList
                            )
                        }

                    _uiState.update {
                        it.copy(
                            product = p,
                            stockBaseQuantity = stock,
                            formattedCurrentStock = formattedStock,
                            totalSpendingPaise = spendingPaise,
                            formattedTotalSpending = formattedSpending,
                            dateGroupedTransactions = grouped,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }.collect {}
        }
    }

    fun getAuditsForTransaction(transactionId: Long) =
        transactionRepository.getAuditsForTransaction(transactionId)

    fun canDeleteTransaction(tx: InventoryTransaction, currentTimeMs: Long = System.currentTimeMillis()): Boolean {
        val deadline = tx.createdAtEpochMs + (30 * 60 * 1000L)
        return currentTimeMs < deadline
    }

    fun editTransaction(
        tx: InventoryTransaction,
        quantityInput: String,
        selectedUnit: UnitType,
        amountPaidInput: String,
        transactionDate: LocalDate,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val product = _uiState.value.product ?: return
        val baseQty = QuantityUtils.toBaseQuantity(quantityInput, selectedUnit)
        if (baseQty == null || baseQty <= 0L) {
            onComplete(false, "Invalid quantity")
            return
        }

        var amountPaise: Long? = null
        if (tx.type == TransactionType.ADD) {
            amountPaise = MoneyUtils.parseRupeesToPaise(amountPaidInput)
            if (amountPaise == null || amountPaise < 0L) {
                onComplete(false, "Invalid amount in ₹")
                return
            }
        }

        val today = LocalDate.now(ZoneId.systemDefault())
        if (transactionDate.isAfter(today)) {
            onComplete(false, "Future dates are not allowed")
            return
        }

        viewModelScope.launch {
            val success = transactionRepository.editTransactionAtomic(
                transactionId = tx.id,
                newBaseQuantity = baseQty,
                newEntryUnitSymbol = selectedUnit.symbol,
                newAmountPaidPaise = amountPaise,
                newTransactionEpochDay = transactionDate.toEpochDay()
            )
            if (success) {
                _eventFlow.emit(ProductHistoryEvent.ShowMessage("Transaction updated."))
                onComplete(true, null)
            } else {
                onComplete(false, "Failed to edit. Resulting stock would be negative.")
            }
        }
    }

    fun deleteTransaction(
        tx: InventoryTransaction,
        currentTimeMs: Long = System.currentTimeMillis(),
        onComplete: (Boolean, String?) -> Unit
    ) {
        if (!canDeleteTransaction(tx, currentTimeMs)) {
            onComplete(false, "30-minute delete window has expired.")
            return
        }

        viewModelScope.launch {
            val success = transactionRepository.deleteTransactionAtomic(tx.id, currentTimeMs)
            if (success) {
                _eventFlow.emit(ProductHistoryEvent.ShowMessage("Transaction deleted."))
                onComplete(true, null)
            } else {
                onComplete(false, "Failed to delete. 30-min window expired or stock would become negative.")
            }
        }
    }
}
