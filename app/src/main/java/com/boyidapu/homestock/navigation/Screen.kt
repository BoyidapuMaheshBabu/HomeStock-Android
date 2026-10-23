package com.boyidapu.homestock.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey {
    @Serializable
    data object Welcome : Screen

    @Serializable
    data object Home : Screen

    @Serializable
    data class CategoryProducts(val categoryId: String) : Screen

    @Serializable
    data class CreateProduct(val categoryId: String) : Screen

    @Serializable
    data class EditProduct(val productId: Long) : Screen

    @Serializable
    data class AddTransaction(val productId: Long) : Screen

    @Serializable
    data class UseTransaction(val productId: Long) : Screen

    @Serializable
    data class ProductHistory(val productId: Long) : Screen

    @Serializable
    data object Insights : Screen

    @Serializable
    data object Settings : Screen

    @Serializable
    data object ArchivedProducts : Screen
}
