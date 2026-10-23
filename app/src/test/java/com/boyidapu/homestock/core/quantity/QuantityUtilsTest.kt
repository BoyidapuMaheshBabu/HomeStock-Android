package com.boyidapu.homestock.core.quantity

import com.boyidapu.homestock.domain.model.MeasurementDimension
import com.boyidapu.homestock.domain.model.UnitType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class QuantityUtilsTest {

    @Test
    fun toBaseQuantity_massKgToGrams_exactConversion() {
        assertEquals(10000L, QuantityUtils.toBaseQuantity("10", UnitType.KG))
        assertEquals(1500L, QuantityUtils.toBaseQuantity("1.5", UnitType.KG))
        assertEquals(1234L, QuantityUtils.toBaseQuantity("1.234", UnitType.KG))
        assertEquals(500L, QuantityUtils.toBaseQuantity("0.5", UnitType.KG))
    }

    @Test
    fun toBaseQuantity_massGrams_exactConversion() {
        assertEquals(500L, QuantityUtils.toBaseQuantity("500", UnitType.G))
        assertEquals(1L, QuantityUtils.toBaseQuantity("1", UnitType.G))
    }

    @Test
    fun toBaseQuantity_volumeLitresToMl_exactConversion() {
        assertEquals(1000L, QuantityUtils.toBaseQuantity("1", UnitType.L))
        assertEquals(1500L, QuantityUtils.toBaseQuantity("1.5", UnitType.L))
        assertEquals(200L, QuantityUtils.toBaseQuantity("0.2", UnitType.L))
        assertEquals(200L, QuantityUtils.toBaseQuantity("200", UnitType.ML))
    }

    @Test
    fun toBaseQuantity_countPieces_exactConversion() {
        assertEquals(5L, QuantityUtils.toBaseQuantity("5", UnitType.PIECES))
        assertEquals(1L, QuantityUtils.toBaseQuantity("1", UnitType.PIECES))
    }

    @Test
    fun toBaseQuantity_precisionLimitsAndInvalidInputs() {
        // Exceeds 3 decimals for kg/L
        assertNull(QuantityUtils.toBaseQuantity("1.2345", UnitType.KG))
        assertNull(QuantityUtils.toBaseQuantity("0.1234", UnitType.L))

        // Decimals on grams, ml, or count
        assertNull(QuantityUtils.toBaseQuantity("1.5", UnitType.G))
        assertNull(QuantityUtils.toBaseQuantity("200.5", UnitType.ML))
        assertNull(QuantityUtils.toBaseQuantity("2.5", UnitType.PIECES))

        // Negative or zero
        assertNull(QuantityUtils.toBaseQuantity("0", UnitType.KG))
        assertNull(QuantityUtils.toBaseQuantity("-5", UnitType.PIECES))

        // Invalid characters
        assertNull(QuantityUtils.toBaseQuantity("abc", UnitType.KG))
        assertNull(QuantityUtils.toBaseQuantity("", UnitType.G))
    }

    @Test
    fun formatDisplayQuantity_formatsCorrectly() {
        assertEquals("1.5 kg", QuantityUtils.formatDisplayQuantity(1500L, UnitType.KG))
        assertEquals("1500 g", QuantityUtils.formatDisplayQuantity(1500L, UnitType.G))
        assertEquals("1.234 kg", QuantityUtils.formatDisplayQuantity(1234L, UnitType.KG))
        assertEquals("0.2 L", QuantityUtils.formatDisplayQuantity(200L, UnitType.L))
        assertEquals("5 pieces", QuantityUtils.formatDisplayQuantity(5L, UnitType.PIECES))
    }

    @Test
    fun validateQuantityInput_dimensionMismatchAndValidation() {
        assertTrue(QuantityUtils.validateQuantityInput("1.5", MeasurementDimension.MASS, UnitType.KG))
        assertFalse(QuantityUtils.validateQuantityInput("1.5", MeasurementDimension.COUNT, UnitType.PIECES))
        assertFalse(QuantityUtils.validateQuantityInput("1.5", MeasurementDimension.MASS, UnitType.PIECES))
    }
}
