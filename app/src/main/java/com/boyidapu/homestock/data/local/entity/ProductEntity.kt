package com.boyidapu.homestock.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.boyidapu.homestock.domain.model.FixedCategory
import com.boyidapu.homestock.domain.model.MeasurementDimension
import com.boyidapu.homestock.domain.model.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val categoryId: String,
    val name: String,
    val dimension: String,
    val preferredUnit: String,
    val isPreset: Boolean,
    val isArchived: Boolean,
    val createdAtEpochMs: Long
) {
    fun toDomain(): Product = Product(
        id = id,
        category = FixedCategory.fromId(categoryId),
        name = name,
        dimension = MeasurementDimension.valueOf(dimension),
        isPreset = isPreset,
        isArchived = isArchived,
        createdAtEpochMs = createdAtEpochMs
    )

    companion object {
        fun fromDomain(product: Product): ProductEntity = ProductEntity(
            id = product.id,
            categoryId = product.category.id,
            name = product.name,
            dimension = product.dimension.name,
            preferredUnit = product.preferredUnit.symbol,
            isPreset = product.isPreset,
            isArchived = product.isArchived,
            createdAtEpochMs = product.createdAtEpochMs
        )
    }
}
