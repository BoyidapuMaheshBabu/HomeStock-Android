package com.boyidapu.homestock.data.local.seed

import androidx.room.withTransaction
import com.boyidapu.homestock.data.local.AppDatabase
import com.boyidapu.homestock.data.local.entity.MetadataEntity
import com.boyidapu.homestock.data.local.entity.ProductEntity
import com.boyidapu.homestock.domain.model.FixedCategory
import com.boyidapu.homestock.domain.model.MeasurementDimension
import com.boyidapu.homestock.domain.model.UnitType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CatalogSeeder {

    const val KEY_STARTER_CATALOG_SEEDED = "starter_catalog_seeded_v1"

    val STARTER_PRODUCTS = listOf(
        // Food & Kitchen
        ProductEntity(
            categoryId = FixedCategory.FOOD_KITCHEN.id,
            name = "Rice",
            dimension = MeasurementDimension.MASS.name,
            preferredUnit = UnitType.KG.symbol,
            isPreset = true,
            isArchived = false,
            createdAtEpochMs = 1000L
        ),
        ProductEntity(
            categoryId = FixedCategory.FOOD_KITCHEN.id,
            name = "Dal",
            dimension = MeasurementDimension.MASS.name,
            preferredUnit = UnitType.KG.symbol,
            isPreset = true,
            isArchived = false,
            createdAtEpochMs = 1001L
        ),
        ProductEntity(
            categoryId = FixedCategory.FOOD_KITCHEN.id,
            name = "Flour",
            dimension = MeasurementDimension.MASS.name,
            preferredUnit = UnitType.KG.symbol,
            isPreset = true,
            isArchived = false,
            createdAtEpochMs = 1002L
        ),
        ProductEntity(
            categoryId = FixedCategory.FOOD_KITCHEN.id,
            name = "Oil",
            dimension = MeasurementDimension.VOLUME.name,
            preferredUnit = UnitType.L.symbol,
            isPreset = true,
            isArchived = false,
            createdAtEpochMs = 1003L
        ),
        ProductEntity(
            categoryId = FixedCategory.FOOD_KITCHEN.id,
            name = "Spices",
            dimension = MeasurementDimension.MASS.name,
            preferredUnit = UnitType.G.symbol,
            isPreset = true,
            isArchived = false,
            createdAtEpochMs = 1004L
        ),

        // Home Care & Cleaning
        ProductEntity(
            categoryId = FixedCategory.HOME_CARE_CLEANING.id,
            name = "Toilet Cleaner",
            dimension = MeasurementDimension.VOLUME.name,
            preferredUnit = UnitType.L.symbol,
            isPreset = true,
            isArchived = false,
            createdAtEpochMs = 2000L
        ),
        ProductEntity(
            categoryId = FixedCategory.HOME_CARE_CLEANING.id,
            name = "Floor Cleaner",
            dimension = MeasurementDimension.VOLUME.name,
            preferredUnit = UnitType.L.symbol,
            isPreset = true,
            isArchived = false,
            createdAtEpochMs = 2001L
        ),
        ProductEntity(
            categoryId = FixedCategory.HOME_CARE_CLEANING.id,
            name = "Dishwashing Liquid",
            dimension = MeasurementDimension.VOLUME.name,
            preferredUnit = UnitType.L.symbol,
            isPreset = true,
            isArchived = false,
            createdAtEpochMs = 2002L
        ),
        ProductEntity(
            categoryId = FixedCategory.HOME_CARE_CLEANING.id,
            name = "Detergent",
            dimension = MeasurementDimension.MASS.name,
            preferredUnit = UnitType.KG.symbol,
            isPreset = true,
            isArchived = false,
            createdAtEpochMs = 2003L
        ),
        ProductEntity(
            categoryId = FixedCategory.HOME_CARE_CLEANING.id,
            name = "Disinfectant",
            dimension = MeasurementDimension.VOLUME.name,
            preferredUnit = UnitType.L.symbol,
            isPreset = true,
            isArchived = false,
            createdAtEpochMs = 2004L
        ),

        // Personal Care & Cosmetics
        ProductEntity(
            categoryId = FixedCategory.PERSONAL_CARE_COSMETICS.id,
            name = "Soap",
            dimension = MeasurementDimension.COUNT.name,
            preferredUnit = UnitType.PIECES.symbol,
            isPreset = true,
            isArchived = false,
            createdAtEpochMs = 3000L
        ),
        ProductEntity(
            categoryId = FixedCategory.PERSONAL_CARE_COSMETICS.id,
            name = "Shampoo",
            dimension = MeasurementDimension.VOLUME.name,
            preferredUnit = UnitType.ML.symbol,
            isPreset = true,
            isArchived = false,
            createdAtEpochMs = 3001L
        ),
        ProductEntity(
            categoryId = FixedCategory.PERSONAL_CARE_COSMETICS.id,
            name = "Toothpaste",
            dimension = MeasurementDimension.COUNT.name,
            preferredUnit = UnitType.PIECES.symbol,
            isPreset = true,
            isArchived = false,
            createdAtEpochMs = 3002L
        ),
        ProductEntity(
            categoryId = FixedCategory.PERSONAL_CARE_COSMETICS.id,
            name = "Hair Oil",
            dimension = MeasurementDimension.VOLUME.name,
            preferredUnit = UnitType.ML.symbol,
            isPreset = true,
            isArchived = false,
            createdAtEpochMs = 3003L
        ),
        ProductEntity(
            categoryId = FixedCategory.PERSONAL_CARE_COSMETICS.id,
            name = "Cream",
            dimension = MeasurementDimension.COUNT.name,
            preferredUnit = UnitType.PIECES.symbol,
            isPreset = true,
            isArchived = false,
            createdAtEpochMs = 3004L
        )
    )

    suspend fun seedIfNeeded(database: AppDatabase) = withContext(Dispatchers.IO) {
        val metadataDao = database.metadataDao()
        val productDao = database.productDao()

        if (metadataDao.getValue(KEY_STARTER_CATALOG_SEEDED) == "true") {
            return@withContext
        }

        database.withTransaction {
            if (metadataDao.getValue(KEY_STARTER_CATALOG_SEEDED) == "true") {
                return@withTransaction
            }

            for (product in STARTER_PRODUCTS) {
                productDao.insertProduct(product)
            }

            metadataDao.setValue(
                MetadataEntity(
                    key = KEY_STARTER_CATALOG_SEEDED,
                    value = "true"
                )
            )
        }
    }
}
