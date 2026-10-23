package com.boyidapu.homestock.core.dates

import com.boyidapu.homestock.domain.model.SpendingPeriod
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DateUtilsTest {

    @Test
    fun calculatePeriodBoundaries_thisMonth() {
        val testDate = LocalDate.of(2026, 10, 23)
        val (start, end) = DateUtils.calculatePeriodBoundaries(SpendingPeriod.THIS_MONTH, testDate)

        assertEquals(LocalDate.of(2026, 10, 1).toEpochDay(), start)
        assertEquals(LocalDate.of(2026, 10, 23).toEpochDay(), end)
    }

    @Test
    fun calculatePeriodBoundaries_last7Days() {
        val testDate = LocalDate.of(2026, 10, 23)
        val (start, end) = DateUtils.calculatePeriodBoundaries(SpendingPeriod.LAST_7_DAYS, testDate)

        assertEquals(LocalDate.of(2026, 10, 17).toEpochDay(), start)
        assertEquals(LocalDate.of(2026, 10, 23).toEpochDay(), end)
    }

    @Test
    fun calculatePeriodBoundaries_last3Months() {
        val testDate = LocalDate.of(2026, 10, 23)
        val (start, end) = DateUtils.calculatePeriodBoundaries(SpendingPeriod.LAST_3_MONTHS, testDate)

        assertEquals(LocalDate.of(2026, 8, 1).toEpochDay(), start)
        assertEquals(LocalDate.of(2026, 10, 23).toEpochDay(), end)
    }
}
