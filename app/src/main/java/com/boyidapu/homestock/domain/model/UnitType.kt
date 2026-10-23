package com.boyidapu.homestock.domain.model

enum class UnitType(
    val symbol: String,
    val dimension: MeasurementDimension,
    val isBaseUnit: Boolean
) {
    KG("kg", MeasurementDimension.MASS, false),
    G("g", MeasurementDimension.MASS, true),
    L("L", MeasurementDimension.VOLUME, false),
    ML("ml", MeasurementDimension.VOLUME, true),
    PIECES("pieces", MeasurementDimension.COUNT, true);

    companion object {
        fun fromSymbol(symbol: String): UnitType {
            return entries.firstOrNull { it.symbol.equals(symbol, ignoreCase = true) } ?: G
        }
    }
}
