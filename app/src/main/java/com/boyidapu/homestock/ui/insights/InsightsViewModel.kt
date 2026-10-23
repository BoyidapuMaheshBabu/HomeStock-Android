package com.boyidapu.homestock.ui.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boyidapu.homestock.core.dates.DateUtils
import com.boyidapu.homestock.domain.model.SpendingPeriod
import com.boyidapu.homestock.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class InsightsUiState(
    val selectedPeriod: SpendingPeriod = SpendingPeriod.THIS_MONTH,
    val dateRangeText: String = "",
    val totalSpendingPaise: Long = 0L,
    val isLoading: Boolean = false
)

class InsightsViewModel(
    transactionRepository: TransactionRepository
) : ViewModel() {

    private val periodBoundaries = DateUtils.calculatePeriodBoundaries(SpendingPeriod.THIS_MONTH)
    private val thisMonthRangeText = DateUtils.formatPeriodRangeText(SpendingPeriod.THIS_MONTH)

    val uiState: StateFlow<InsightsUiState> = transactionRepository
        .getSpendingBetweenEpochDays(periodBoundaries.first, periodBoundaries.second)
        .map { totalPaise ->
            InsightsUiState(
                selectedPeriod = SpendingPeriod.THIS_MONTH,
                dateRangeText = thisMonthRangeText,
                totalSpendingPaise = totalPaise,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = InsightsUiState(
                selectedPeriod = SpendingPeriod.THIS_MONTH,
                dateRangeText = thisMonthRangeText,
                isLoading = true
            )
        )
}
