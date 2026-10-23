package com.boyidapu.homestock.core.dates

import com.boyidapu.homestock.domain.model.SpendingPeriod
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {

    fun todayEpochDay(): Long = LocalDate.now(ZoneId.systemDefault()).toEpochDay()

    fun epochDayToLocalDate(epochDay: Long): LocalDate = LocalDate.ofEpochDay(epochDay)

    fun formatEpochDay(epochDay: Long): String {
        val date = LocalDate.ofEpochDay(epochDay)
        val today = LocalDate.now(ZoneId.systemDefault())
        return when (date) {
            today -> "Today"
            today.minusDays(1) -> "Yesterday"
            else -> date.format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.getDefault()))
        }
    }

    fun calculatePeriodBoundaries(
        period: SpendingPeriod,
        referenceDate: LocalDate = LocalDate.now(ZoneId.systemDefault()),
        customStart: LocalDate? = null,
        customEnd: LocalDate? = null
    ): Pair<Long, Long> {
        val end = referenceDate
        val start = when (period) {
            SpendingPeriod.LAST_7_DAYS -> referenceDate.minusDays(6)
            SpendingPeriod.THIS_MONTH -> referenceDate.withDayOfMonth(1)
            SpendingPeriod.LAST_3_MONTHS -> referenceDate.minusMonths(2).withDayOfMonth(1)
            SpendingPeriod.CUSTOM -> customStart ?: referenceDate
            SpendingPeriod.ALL_TIME -> LocalDate.of(2000, 1, 1)
        }

        val actualEnd = if (period == SpendingPeriod.CUSTOM && customEnd != null) customEnd else end
        return Pair(start.toEpochDay(), actualEnd.toEpochDay())
    }

    fun formatPeriodRangeText(
        period: SpendingPeriod,
        referenceDate: LocalDate = LocalDate.now(ZoneId.systemDefault()),
        customStart: LocalDate? = null,
        customEnd: LocalDate? = null
    ): String {
        val (startEpoch, endEpoch) = calculatePeriodBoundaries(period, referenceDate, customStart, customEnd)
        val startDate = LocalDate.ofEpochDay(startEpoch)
        val endDate = LocalDate.ofEpochDay(endEpoch)
        val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.getDefault())

        return when (period) {
            SpendingPeriod.ALL_TIME -> "All time"
            else -> "${startDate.format(formatter)} – ${endDate.format(formatter)}"
        }
    }
}
