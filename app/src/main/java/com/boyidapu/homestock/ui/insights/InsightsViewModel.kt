package com.boyidapu.homestock.ui.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boyidapu.homestock.core.dates.DateUtils
import com.boyidapu.homestock.core.money.MoneyUtils
import com.boyidapu.homestock.domain.model.CategorySpending
import com.boyidapu.homestock.domain.model.ProductSpending
import com.boyidapu.homestock.domain.model.SpendingPeriod
import com.boyidapu.homestock.domain.model.TrendBucket
import com.boyidapu.homestock.domain.repository.TransactionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.ZoneId

data class InsightsUiState(
    val selectedPeriod: SpendingPeriod = SpendingPeriod.THIS_MONTH,
    val dateRangeText: String = "",
    val totalSpendingPaise: Long = 0L,
    val purchaseCount: Int = 0,
    val averagePerPurchasePaise: Long? = null,
    val formattedAveragePerPurchase: String = "—",
    val trendBuckets: List<TrendBucket> = emptyList(),
    val categorySpendings: List<CategorySpending> = emptyList(),
    val topProductSpendings: List<ProductSpending> = emptyList(),
    val customStart: LocalDate = LocalDate.now(ZoneId.systemDefault()).withDayOfMonth(1),
    val customEnd: LocalDate = LocalDate.now(ZoneId.systemDefault()),
    val customRangeError: String? = null,
    val isLoading: Boolean = false
)

class InsightsViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        InsightsUiState(
            selectedPeriod = SpendingPeriod.THIS_MONTH,
            dateRangeText = DateUtils.formatPeriodRangeText(SpendingPeriod.THIS_MONTH)
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<InsightsUiState> = _uiState
        .flatMapLatest { state ->
            val period = state.selectedPeriod
            val (startEpoch, endEpoch) = DateUtils.calculatePeriodBoundaries(
                period = period,
                customStart = state.customStart,
                customEnd = state.customEnd
            )
            val rangeText = DateUtils.formatPeriodRangeText(
                period = period,
                customStart = state.customStart,
                customEnd = state.customEnd
            )

            val isAllTime = period == SpendingPeriod.ALL_TIME

            val spendingFlow = if (isAllTime) transactionRepository.getAllTimeSpending()
                else transactionRepository.getSpendingBetweenEpochDays(startEpoch, endEpoch)

            val countFlow = if (isAllTime) transactionRepository.getAllTimeAddTransactionCount()
                else transactionRepository.getAddTransactionCountBetweenEpochDays(startEpoch, endEpoch)

            val txsFlow = if (isAllTime) transactionRepository.getAllTimeAddTransactions()
                else transactionRepository.getAddTransactionsBetweenEpochDays(startEpoch, endEpoch)

            val catFlow = if (isAllTime) transactionRepository.getAllTimeCategorySpending()
                else transactionRepository.getCategorySpendingBetweenEpochDays(startEpoch, endEpoch)

            val topProdFlow = if (isAllTime) transactionRepository.getAllTimeTopProductSpending()
                else transactionRepository.getTopProductSpendingBetweenEpochDays(startEpoch, endEpoch)

            combine(spendingFlow, countFlow, txsFlow, catFlow, topProdFlow) { totalPaise, count, txs, categories, topProducts ->
                val avgPaise = if (count > 0) totalPaise / count else null
                val formattedAvg = if (avgPaise != null) MoneyUtils.formatPaiseToFormattedAmount(avgPaise) else "—"

                val startDate = LocalDate.ofEpochDay(startEpoch)
                val endDate = LocalDate.ofEpochDay(endEpoch)
                val buckets = DateUtils.calculateTrendBuckets(period, startDate, endDate, txs)

                state.copy(
                    dateRangeText = rangeText,
                    totalSpendingPaise = totalPaise,
                    purchaseCount = count,
                    averagePerPurchasePaise = avgPaise,
                    formattedAveragePerPurchase = formattedAvg,
                    trendBuckets = buckets,
                    categorySpendings = categories,
                    topProductSpendings = topProducts,
                    isLoading = false
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _uiState.value
        )

    fun selectPeriod(period: SpendingPeriod) {
        _uiState.update {
            it.copy(
                selectedPeriod = period,
                customRangeError = null
            )
        }
    }

    fun onCustomDatesSelected(start: LocalDate, end: LocalDate) {
        val today = LocalDate.now(ZoneId.systemDefault())
        if (start.isAfter(end)) {
            _uiState.update { it.copy(customRangeError = "Start date cannot be after end date.") }
            return
        }
        if (end.isAfter(today)) {
            _uiState.update { it.copy(customRangeError = "Future dates are not allowed.") }
            return
        }
        _uiState.update {
            it.copy(
                selectedPeriod = SpendingPeriod.CUSTOM,
                customStart = start,
                customEnd = end,
                customRangeError = null
            )
        }
    }
}
