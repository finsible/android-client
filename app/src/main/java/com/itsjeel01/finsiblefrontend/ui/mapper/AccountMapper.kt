package com.itsjeel01.finsiblefrontend.ui.mapper

import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountGroupEntity
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.AccountGroupUIModel
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.AccountUIModel

/** Map an [AccountEntity] to a stable [AccountUIModel] for Compose. */
fun AccountEntity.toUiModel(currencyFormatter: CurrencyFormatter): AccountUIModel = AccountUIModel(
    id = id,
    name = name,
    description = description,
    icon = icon,
    currencyCode = currencyCode,
    formattedBalance = currencyFormatter.format(
        centis = balanceCentis,
        currencyCode = currencyCode
    ),
    groupColor = accountGroup.target?.color,
    groupName = accountGroup.target?.name,
    isPositiveBalance = balanceCentis >= 0L,
    usageCount = usageCount,
    lastUsedAt = lastUsedAt,
)

/** Map an [AccountGroupEntity] to a stable [AccountGroupUIModel] for Compose. */
fun AccountGroupEntity.toUiModel(): AccountGroupUIModel = AccountGroupUIModel(
    id = id,
    name = name,
    color = color,
)

