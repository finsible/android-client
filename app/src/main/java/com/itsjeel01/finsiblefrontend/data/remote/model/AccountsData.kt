package com.itsjeel01.finsiblefrontend.data.remote.model

import com.itsjeel01.finsiblefrontend.data.model.Account
import kotlinx.serialization.Serializable

@Serializable
data class AccountsData(
    val accounts: List<Account>
)