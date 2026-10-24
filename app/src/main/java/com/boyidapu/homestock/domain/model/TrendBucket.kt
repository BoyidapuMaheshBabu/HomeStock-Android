package com.boyidapu.homestock.domain.model

import java.time.LocalDate

data class TrendBucket(
    val label: String,
    val totalSpendingPaise: Long,
    val startDate: LocalDate,
    val endDate: LocalDate
)
