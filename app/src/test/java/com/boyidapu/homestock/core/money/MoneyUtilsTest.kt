package com.boyidapu.homestock.core.money

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MoneyUtilsTest {

    @Test
    fun parseRupeesToPaise_validInputs() {
        assertEquals(50000L, MoneyUtils.parseRupeesToPaise("500"))
        assertEquals(50000L, MoneyUtils.parseRupeesToPaise("500.00"))
        assertEquals(5050L, MoneyUtils.parseRupeesToPaise("50.5"))
        assertEquals(505L, MoneyUtils.parseRupeesToPaise("5.05"))
        assertEquals(0L, MoneyUtils.parseRupeesToPaise("0"))
        assertEquals(0L, MoneyUtils.parseRupeesToPaise("0.00"))
    }

    @Test
    fun parseRupeesToPaise_invalidInputs() {
        assertNull(MoneyUtils.parseRupeesToPaise("-50"))
        assertNull(MoneyUtils.parseRupeesToPaise("500.123"))
        assertNull(MoneyUtils.parseRupeesToPaise("abc"))
        assertNull(MoneyUtils.parseRupeesToPaise(""))
    }

    @Test
    fun formatPaiseToFormattedAmount_correctRepresentation() {
        assertEquals("₹500", MoneyUtils.formatPaiseToFormattedAmount(50000L))
        assertEquals("₹5.05", MoneyUtils.formatPaiseToFormattedAmount(505L))
        assertEquals("₹0", MoneyUtils.formatPaiseToFormattedAmount(0L))
    }
}
