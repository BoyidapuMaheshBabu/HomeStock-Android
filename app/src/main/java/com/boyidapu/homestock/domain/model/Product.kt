package com.boyidapu.homestock.domain.model

data class Product(
    val id: Long = 0,
    val category: FixedCategory,
    val name: String,
    val dimension: MeasurementDimension,
    val preferredUnit: UnitType,
    val isPreset: Boolean = false,
    val isArchived: Boolean = false,
    val createdAtEpochMs: Long = System.currentTimeMillis()
)
