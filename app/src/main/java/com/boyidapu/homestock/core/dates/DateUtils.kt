package com.boyidapu.homestock.core.dates

import com.boyidapu.homestock.domain.model.InventoryTransaction
import com.boyidapu.homestock.domain.model.SpendingPeriod
import com.boyidapu.homestock.domain.model.TrendBucket
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
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
            SpendingPeriod.ALL_TIME -> LocalDate.of(1970, 1, 1)
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

    fun calculateTrendBuckets(
        period: SpendingPeriod,
        startDate: LocalDate,
        endDate: LocalDate,
        addTransactions: List<InventoryTransaction>
    ): List<TrendBucket> {
        if (startDate.isAfter(endDate)) return emptyList()

        val buckets = mutableListOf<TrendBucket>()

        when (period) {
            SpendingPeriod.LAST_7_DAYS -> {
                var curr = startDate
                val dayFormatter = DateTimeFormatter.ofPattern("EEE", Locale.getDefault())
                while (!curr.isAfter(endDate)) {
                    val epoch = curr.toEpochDay()
                    val spend = addTransactions.filter { it.transactionEpochDay == epoch }.sumOf { it.amountPaidPaise ?: 0L }
                    buckets.add(
                        TrendBucket(
                            label = curr.format(dayFormatter),
                            totalSpendingPaise = spend,
                            startDate = curr,
                            endDate = curr
                        )
                    )
                    curr = curr.plusDays(1)
                }
            }
            SpendingPeriod.THIS_MONTH -> {
                var curr = startDate
                val dayFormatter = DateTimeFormatter.ofPattern("d", Locale.getDefault())
                while (!curr.isAfter(endDate)) {
                    val epoch = curr.toEpochDay()
                    val spend = addTransactions.filter { it.transactionEpochDay == epoch }.sumOf { it.amountPaidPaise ?: 0L }
                    buckets.add(
                        TrendBucket(
                            label = curr.format(dayFormatter),
                            totalSpendingPaise = spend,
                            startDate = curr,
                            endDate = curr
                        )
                    )
                    curr = curr.plusDays(1)
                }
            }
            SpendingPeriod.LAST_3_MONTHS -> {
                var currMonth = startDate.withDayOfMonth(1)
                val monthFormatter = DateTimeFormatter.ofPattern("MMM", Locale.getDefault())
                while (!currMonth.isAfter(endDate)) {
                    val monthEnd = currMonth.plusMonths(1).minusDays(1).let { if (it.isAfter(endDate)) endDate else it }
                    val startEpoch = currMonth.toEpochDay()
                    val endEpoch = monthEnd.toEpochDay()
                    val spend = addTransactions.filter { it.transactionEpochDay in startEpoch..endEpoch }.sumOf { it.amountPaidPaise ?: 0L }
                    buckets.add(
                        TrendBucket(
                            label = currMonth.format(monthFormatter),
                            totalSpendingPaise = spend,
                            startDate = currMonth,
                            endDate = monthEnd
                        )
                    )
                    currMonth = currMonth.plusMonths(1)
                }
            }
            SpendingPeriod.CUSTOM -> {
                val totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1
                if (totalDays <= 31) {
                    var curr = startDate
                    val dayFormatter = DateTimeFormatter.ofPattern("MMM d", Locale.getDefault())
                    while (!curr.isAfter(endDate)) {
                        val epoch = curr.toEpochDay()
                        val spend = addTransactions.filter { it.transactionEpochDay == epoch }.sumOf { it.amountPaidPaise ?: 0L }
                        buckets.add(
                            TrendBucket(
                                label = curr.format(dayFormatter),
                                totalSpendingPaise = spend,
                                startDate = curr,
                                endDate = curr
                            )
                        )
                        curr = curr.plusDays(1)
                    }
                } else if (totalDays <= 180) {
                    var curr = startDate
                    val labelFormatter = DateTimeFormatter.ofPattern("MMM d", Locale.getDefault())
                    while (!curr.isAfter(endDate)) {
                        val weekEnd = curr.with(DayOfWeek.SUNDAY).let { if (it.isAfter(endDate)) endDate else it }
                        val startEpoch = curr.toEpochDay()
                        val endEpoch = weekEnd.toEpochDay()
                        val spend = addTransactions.filter { it.transactionEpochDay in startEpoch..endEpoch }.sumOf { it.amountPaidPaise ?: 0L }
                        buckets.add(
                            TrendBucket(
                                label = curr.format(labelFormatter),
                                totalSpendingPaise = spend,
                                startDate = curr,
                                endDate = weekEnd
                            )
                        )
                        curr = weekEnd.plusDays(1)
                    }
                } else {
                    var currMonth = startDate.withDayOfMonth(1)
                    val monthFormatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.getDefault())
                    while (!currMonth.isAfter(endDate)) {
                        val monthEnd = currMonth.plusMonths(1).minusDays(1).let { if (it.isAfter(endDate)) endDate else it }
                        val startEpoch = currMonth.toEpochDay()
                        val endEpoch = monthEnd.toEpochDay()
                        val spend = addTransactions.filter { it.transactionEpochDay in startEpoch..endEpoch }.sumOf { it.amountPaidPaise ?: 0L }
                        buckets.add(
                            TrendBucket(
                                label = currMonth.format(monthFormatter),
                                totalSpendingPaise = spend,
                                startDate = currMonth,
                                endDate = monthEnd
                            )
                        )
                        currMonth = currMonth.plusMonths(1)
                    }
                }
            }
            SpendingPeriod.ALL_TIME -> {
                val firstTxDay = addTransactions.minOfOrNull { it.transactionEpochDay } ?: startDate.toEpochDay()
                val firstMonth = LocalDate.ofEpochDay(firstTxDay).withDayOfMonth(1)
                var currMonth = if (firstMonth.isBefore(startDate)) firstMonth else startDate.withDayOfMonth(1)
                val monthFormatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.getDefault())
                while (!currMonth.isAfter(endDate)) {
                    val monthEnd = currMonth.plusMonths(1).minusDays(1).let { if (it.isAfter(endDate)) endDate else it }
                    val startEpoch = currMonth.toEpochDay()
                    val endEpoch = monthEnd.toEpochDay()
                    val spend = addTransactions.filter { it.transactionEpochDay in startEpoch..endEpoch }.sumOf { it.amountPaidPaise ?: 0L }
                    buckets.add(
                        TrendBucket(
                            label = currMonth.format(monthFormatter),
                            totalSpendingPaise = spend,
                            startDate = currMonth,
                            endDate = monthEnd
                        )
                    )
                    currMonth = currMonth.plusMonths(1)
                }
            }
        }

        return buckets
    }
}
