package com.boyidapu.homestock.core.quantity

import com.boyidapu.homestock.domain.model.MeasurementDimension
import com.boyidapu.homestock.domain.model.UnitType
import java.math.BigDecimal
import java.math.RoundingMode

object QuantityUtils {

    /**
     * Converts a user quantity input string in the given entry unit to an exact integer base quantity
     * (grams, millilitres, or pieces) stored as a Long.
     *
     * Precision rules:
     * - kg / L: allows up to 3 decimal places (e.g., "1.234" kg -> 1234 g, "1.5" L -> 1500 ml)
     * - g / ml / pieces: whole numbers only (e.g., "500" g -> 500 g)
     */
    fun toBaseQuantity(quantityInput: String, unit: UnitType): Long? {
        val trimmed = quantityInput.trim()
        if (trimmed.isEmpty()) return null

        val decimal = try {
            BigDecimal(trimmed)
        } catch (e: Exception) {
            return null
        }

        if (decimal <= BigDecimal.ZERO) return null

        return when (unit) {
            UnitType.KG, UnitType.L -> {
                if (decimal.scale() > 3) return null
                try {
                    decimal.multiply(BigDecimal(1000)).setScale(0, RoundingMode.UNNECESSARY).longValueExact()
                } catch (e: ArithmeticException) {
                    null
                }
            }
            UnitType.G, UnitType.ML, UnitType.PIECES -> {
                if (decimal.scale() > 0 && decimal.stripTrailingZeros().scale() > 0) return null
                try {
                    decimal.setScale(0, RoundingMode.UNNECESSARY).longValueExact()
                } catch (e: ArithmeticException) {
                    null
                }
            }
        }
    }

    /**
     * Formats an integer base unit quantity (grams, millilitres, pieces) into a clean display string
     * for the given display unit.
     */
    fun formatDisplayQuantity(baseQuantity: Long, displayUnit: UnitType): String {
        val baseDecimal = BigDecimal(baseQuantity)
        val valueString = when (displayUnit) {
            UnitType.KG, UnitType.L -> {
                baseDecimal.divide(BigDecimal(1000), 3, RoundingMode.HALF_UP)
                    .stripTrailingZeros()
                    .toPlainString()
            }
            UnitType.G, UnitType.ML, UnitType.PIECES -> {
                baseQuantity.toString()
            }
        }
        return "$valueString ${displayUnit.symbol}"
    }

    /**
     * Validates input quantity string for a given dimension and unit.
     */
    fun validateQuantityInput(
        input: String,
        dimension: MeasurementDimension,
        unit: UnitType
    ): Boolean {
        if (unit.dimension != dimension) return false
        return toBaseQuantity(input, unit) != null
    }
}
