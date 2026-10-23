package com.boyidapu.homestock.domain.model

enum class UnitType(
    val symbol: String,
    val displayName: String,
    val dimension: MeasurementDimension,
    val isBaseUnit: Boolean
) {
    KG("kg", "Kilogram", MeasurementDimension.MASS, false),
    G("g", "Gram", MeasurementDimension.MASS, true),
    L("L", "Litre", MeasurementDimension.VOLUME, false),
    ML("ml", "Millilitre", MeasurementDimension.VOLUME, true),
    PIECES("pieces", "Pieces/Units", MeasurementDimension.COUNT, true);

    companion object {
        fun fromSymbol(symbol: String): UnitType {
            return entries.firstOrNull { it.symbol.equals(symbol, ignoreCase = true) } ?: G
        }

        fun forDimension(dimension: MeasurementDimension): List<UnitType> {
            return entries.filter { it.dimension == dimension }
        }
    }
}
