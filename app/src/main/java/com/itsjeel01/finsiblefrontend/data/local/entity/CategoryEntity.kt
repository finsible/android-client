package com.itsjeel01.finsiblefrontend.data.local.entity

import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.TransactionTypeConverter
import com.itsjeel01.finsiblefrontend.data.model.Category
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne

@Entity
data class CategoryEntity(
    @Id(assignable = true)
    override var id: Long = 0,

    @Convert(converter = TransactionTypeConverter::class, dbType = Int::class)
    var type: TransactionType = TransactionType.EXPENSE,

    var name: String = "",

    var icon: String = "",

    var readOnly: Boolean = false,

    var parentCategoryId: Long = 0L,
) : BaseEntity() {

    lateinit var parentCategory: ToOne<CategoryEntity>

    @Backlink(to = "parentCategory")
    lateinit var subCategories: ToMany<CategoryEntity>

    fun isParent(): Boolean {
        return subCategories.isNotEmpty()
    }
}

fun CategoryEntity.toDTO(): Category {
    return Category(
        id = id,
        name = name,
        icon = icon,
        readOnly = readOnly,
        parentCategory = if (parentCategoryId != 0L) parentCategoryId else null,
        subCategory = parentCategoryId != 0L
    )
}
