package com.boyidapu.homestock.core.money

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

object MoneyUtils {

    fun parseRupeesToPaise(rupeeInput: String): Long? {
        val trimmed = rupeeInput.trim()
        if (trimmed.isEmpty()) return null
        return try {
            val decimal = BigDecimal(trimmed)
            if (decimal < BigDecimal.ZERO) return null
            if (decimal.scale() > 2) return null
            decimal.multiply(BigDecimal(100)).setScale(0, RoundingMode.HALF_UP).longValueExact()
        } catch (e: Exception) {
            null
        }
    }

    fun formatPaiseToRupees(paise: Long): String {
        val rupees = BigDecimal(paise).divide(BigDecimal(100), 2, RoundingMode.HALF_UP)
        val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        format.minimumFractionDigits = if (paise % 100 == 0L) 0 else 2
        format.maximumFractionDigits = 2
        return format.format(rupees)
    }

    fun formatPaiseToFormattedAmount(paise: Long): String {
        val rupees = BigDecimal(paise).divide(BigDecimal(100), 2, RoundingMode.HALF_UP)
        return "₹" + NumberFormat.getNumberInstance(Locale("en", "IN")).apply {
            minimumFractionDigits = if (paise % 100 == 0L) 0 else 2
            maximumFractionDigits = 2
        }.format(rupees)
    }
}
