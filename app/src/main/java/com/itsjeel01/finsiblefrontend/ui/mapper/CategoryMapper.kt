package com.itsjeel01.finsiblefrontend.ui.mapper

import com.itsjeel01.finsiblefrontend.data.local.entity.CategoryEntity
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.CategoryUIModel

/** Map a [CategoryEntity] to a stable [CategoryUIModel] for Compose. */
fun CategoryEntity.toUiModel(): CategoryUIModel = CategoryUIModel(
    id = id,
    name = name,
    icon = icon,
)

