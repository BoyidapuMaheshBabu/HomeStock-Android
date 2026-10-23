package com.boyidapu.homestock.domain.model

enum class SpendingPeriod(val displayName: String) {
    LAST_7_DAYS("Last 7 days"),
    THIS_MONTH("This month"),
    LAST_3_MONTHS("Last 3 months"),
    CUSTOM("Custom range"),
    ALL_TIME("All time")
}
