package com.itsjeel01.finsiblefrontend.ui.mapper

import com.google.common.truth.Truth.assertThat
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.entity.CategoryEntity
import org.junit.Test

class CategoryMapperTest {

    @Test
    fun `maps all fields from entity to UI model`() {
        val entity = CategoryEntity(
            id = 42L,
            type = TransactionType.EXPENSE,
            name = "Groceries",
            icon = "shopping_cart",
            readOnly = false,
            parentCategoryId = 0L,
            usageCount = 15L,
            lastUsedAt = 1_700_000_000_000L,
        )

        val uiModel = entity.toUiModel()

        assertThat(uiModel.id).isEqualTo(42L)
        assertThat(uiModel.name).isEqualTo("Groceries")
        assertThat(uiModel.icon).isEqualTo("shopping_cart")
        assertThat(uiModel.isParent).isTrue()
        assertThat(uiModel.usageCount).isEqualTo(15L)
        assertThat(uiModel.lastUsedAt).isEqualTo(1_700_000_000_000L)
    }

    @Test
    fun `subcategory is not marked as parent`() {
        val entity = CategoryEntity(
            id = 7L,
            type = TransactionType.EXPENSE,
            name = "Dairy",
            icon = "egg",
            parentCategoryId = 42L,
        )

        val uiModel = entity.toUiModel()

        assertThat(uiModel.isParent).isFalse()
    }

    @Test
    fun `null lastUsedAt does not crash`() {
        val entity = CategoryEntity(
            id = 1L,
            type = TransactionType.EXPENSE,
            name = "Test",
            icon = "test",
            lastUsedAt = null,
        )

        val uiModel = entity.toUiModel()

        assertThat(uiModel.lastUsedAt).isNull()
    }

    @Test
    fun `zero usage count maps correctly`() {
        val entity = CategoryEntity(
            id = 2L,
            type = TransactionType.EXPENSE,
            name = "Never Used",
            icon = "empty",
            usageCount = 0L,
        )

        val uiModel = entity.toUiModel()

        assertThat(uiModel.usageCount).isEqualTo(0L)
    }

    @Test
    fun `empty name and icon do not crash`() {
        val entity = CategoryEntity(
            id = 99L,
            type = TransactionType.INCOME,
            name = "",
            icon = "",
        )

        val uiModel = entity.toUiModel()

        assertThat(uiModel.name).isEmpty()
        assertThat(uiModel.icon).isEmpty()
    }
}
