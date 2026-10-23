package com.boyidapu.homestock.domain.model

enum class MeasurementDimension(val displayName: String) {
    MASS("Mass (kg/g)"),
    VOLUME("Volume (L/ml)"),
    COUNT("Count (pieces/units)");

    val validUnits: List<UnitType>
        get() = when (this) {
            MASS -> listOf(UnitType.KG, UnitType.G)
            VOLUME -> listOf(UnitType.L, UnitType.ML)
            COUNT -> listOf(UnitType.PIECES)
        }

    val defaultUnit: UnitType
        get() = validUnits.first()
}
