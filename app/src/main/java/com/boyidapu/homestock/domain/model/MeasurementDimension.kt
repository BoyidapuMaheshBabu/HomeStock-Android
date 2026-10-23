package com.boyidapu.homestock.domain.model

enum class MeasurementDimension(val displayName: String) {
    MASS("Mass"),
    VOLUME("Liquid"),
    COUNT("Units");

    val validUnits: List<UnitType>
        get() = when (this) {
            MASS -> listOf(UnitType.KG, UnitType.G)
            VOLUME -> listOf(UnitType.L, UnitType.ML)
            COUNT -> listOf(UnitType.PIECES)
        }

    val defaultUnit: UnitType
        get() = validUnits.first()
}
