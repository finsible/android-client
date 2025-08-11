package com.itsjeel01.finsiblefrontend.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Long,
    val name: String,
    val color: String,
    val isCustom: Boolean = false,
    val domain: CategoryDomain = CategoryDomain.OTHERS
)

enum class CategoryDomain {
    // Expense categories domains
    ESSENTIALS,
    LIFESTYLE,
    SOCIAL,

    // Income categories domains
    PRIMARY,
    PASSIVE,
    IRREGULAR,

    // Transfer categories domains
    ACCOUNT_MANAGEMENT,
    INVESTMENT,
    ASSET_ACQUISITION,
    ASSET_DISPOSAL,

    OTHERS, // Catch-all for any other categories
}