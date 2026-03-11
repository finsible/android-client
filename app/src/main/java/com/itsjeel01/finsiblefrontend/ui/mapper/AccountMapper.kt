package com.itsjeel01.finsiblefrontend.ui.mapper

import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.common.centisToFormattedCurrency
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountGroupEntity
import com.itsjeel01.finsiblefrontend.ui.model.item.AccountGroupUIModel
import com.itsjeel01.finsiblefrontend.ui.model.item.AccountUIModel

/** Map an [AccountEntity] to a stable [AccountUIModel] for Compose. */
fun AccountEntity.toUiModel(currencyFormatter: CurrencyFormatter): AccountUIModel = AccountUIModel(
    id = id,
    name = name,
    description = description,
    icon = icon,
    formattedBalance = balanceCentis.centisToFormattedCurrency(currencyFormatter),
    groupColor = accountGroup.target?.color,
    isPositiveBalance = balanceCentis >= 0L,
)

/** Map an [AccountGroupEntity] to a stable [AccountGroupUIModel] for Compose. */
fun AccountGroupEntity.toUiModel(): AccountGroupUIModel = AccountGroupUIModel(
    id = id,
    name = name,
    color = color,
)

