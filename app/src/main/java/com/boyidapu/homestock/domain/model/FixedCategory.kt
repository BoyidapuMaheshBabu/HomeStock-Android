package com.boyidapu.homestock.domain.model

enum class FixedCategory(val id: String, val displayName: String) {
    FOOD_KITCHEN("FOOD_KITCHEN", "Food & Kitchen"),
    HOME_CARE_CLEANING("HOME_CARE_CLEANING", "Home Care & Cleaning"),
    PERSONAL_CARE_COSMETICS("PERSONAL_CARE_COSMETICS", "Personal Care & Cosmetics");

    companion object {
        fun fromId(id: String): FixedCategory {
            return entries.firstOrNull { it.id == id } ?: FOOD_KITCHEN
        }
    }
}
