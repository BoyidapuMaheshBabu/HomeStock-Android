package com.boyidapu.homestock.data.local.seed

import com.boyidapu.homestock.domain.model.FixedCategory
import com.boyidapu.homestock.domain.model.MeasurementDimension
import com.boyidapu.homestock.domain.model.UnitType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogSeederTest {

    @Test
    fun starterProducts_contains15ProductsAcrossThreeCategories() {
        val products = CatalogSeeder.STARTER_PRODUCTS
        assertEquals(15, products.size)

        val foodProducts = products.filter { it.categoryId == FixedCategory.FOOD_KITCHEN.id }
        val homeCareProducts = products.filter { it.categoryId == FixedCategory.HOME_CARE_CLEANING.id }
        val personalCareProducts = products.filter { it.categoryId == FixedCategory.PERSONAL_CARE_COSMETICS.id }

        assertEquals(5, foodProducts.size)
        assertEquals(5, homeCareProducts.size)
        assertEquals(5, personalCareProducts.size)
    }

    @Test
    fun starterProducts_allHavePresetTrueAndArchivedFalse() {
        CatalogSeeder.STARTER_PRODUCTS.forEach { product ->
            assertTrue(product.isPreset)
            assertFalse(product.isArchived)
        }
    }

    @Test
    fun starterProducts_haveValidDimensionAndUnitPairs() {
        val rice = CatalogSeeder.STARTER_PRODUCTS.first { it.name == "Rice" }
        assertEquals(MeasurementDimension.MASS.name, rice.dimension)
        assertEquals(UnitType.KG.symbol, rice.preferredUnit)

        val oil = CatalogSeeder.STARTER_PRODUCTS.first { it.name == "Oil" }
        assertEquals(MeasurementDimension.VOLUME.name, oil.dimension)
        assertEquals(UnitType.L.symbol, oil.preferredUnit)

        val soap = CatalogSeeder.STARTER_PRODUCTS.first { it.name == "Soap" }
        assertEquals(MeasurementDimension.COUNT.name, soap.dimension)
        assertEquals(UnitType.PIECES.symbol, soap.preferredUnit)

        val shampoo = CatalogSeeder.STARTER_PRODUCTS.first { it.name == "Shampoo" }
        assertEquals(MeasurementDimension.VOLUME.name, shampoo.dimension)
        assertEquals(UnitType.ML.symbol, shampoo.preferredUnit)
    }
}
