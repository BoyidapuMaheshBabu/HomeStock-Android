package com.boyidapu.homestock.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boyidapu.homestock.core.dates.DateUtils
import com.boyidapu.homestock.domain.model.SpendingPeriod
import com.boyidapu.homestock.domain.repository.TransactionRepository
import com.boyidapu.homestock.domain.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(
    val greeting: String = "",
    val thisMonthSpendingPaise: Long = 0L,
    val thisMonthDateRange: String = "",
    val isLoading: Boolean = false
)

class HomeViewModel(
    userRepository: UserRepository,
    transactionRepository: TransactionRepository
) : ViewModel() {

    private val periodBoundaries = DateUtils.calculatePeriodBoundaries(SpendingPeriod.THIS_MONTH)
    private val thisMonthRangeText = DateUtils.formatPeriodRangeText(SpendingPeriod.THIS_MONTH)

    val uiState: StateFlow<HomeUiState> = combine(
        userRepository.userName,
        transactionRepository.getSpendingBetweenEpochDays(periodBoundaries.first, periodBoundaries.second)
    ) { name, spendingPaise ->
        HomeUiState(
            greeting = GreetingUtils.formatGreeting(name),
            thisMonthSpendingPaise = spendingPaise,
            thisMonthDateRange = thisMonthRangeText,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(
            thisMonthDateRange = thisMonthRangeText,
            isLoading = true
        )
    )
}
