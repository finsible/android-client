package com.itsjeel01.finsiblefrontend.data.model

import kotlinx.serialization.Serializable

enum class AccountType(val parent: AccountType? = null) {
    BANK,
    CASH,
    INVESTMENT,
    FD(INVESTMENT),
    MUTUAL_FUNDS(INVESTMENT),
    STOCKS(INVESTMENT),
    LOANS,
    CREDIT_CARD(LOANS),
    ASSET,
    DIGITAL_WALLET,
    OTHERS;
}

@Serializable
data class Account(
    val id: Long,
    val name: String,
    val balance: Double? = null,
    val accountType: AccountType,
    val isCustom: Boolean,
    val description: String? = null,

    val creditLimit: Double? = null,
    val availableCredit: Double? = null,
    val billingDate: Int? = null, // Day of month (1-31)
    val dueDate: Int? = null, // Day of month (1-31)

    val loanAmount: Double? = null,
    val remainingAmount: Double? = null,

    val assetType: String? = null, // Gold, Real Estate, Vehicle, etc.
)

fun Account.isCreditCard(): Boolean {
    return accountType == AccountType.CREDIT_CARD
}

fun Account.isLoan(): Boolean {
    return accountType == AccountType.LOANS || accountType.parent == AccountType.LOANS
}

fun Account.isInvestment(): Boolean {
    return accountType == AccountType.INVESTMENT || accountType.parent == AccountType.INVESTMENT
}

fun Account.isAsset(): Boolean {
    return accountType == AccountType.ASSET
}

fun Account.isDigitalWallet(): Boolean {
    return accountType == AccountType.DIGITAL_WALLET
}

fun Account.isBankAccount(): Boolean {
    return accountType == AccountType.BANK
}

fun Account.isCash(): Boolean {
    return accountType == AccountType.CASH
}

fun Account.isOther(): Boolean {
    return accountType == AccountType.OTHERS
}

fun Account.isFD(): Boolean {
    return accountType == AccountType.FD
}

fun Account.isMutualFunds(): Boolean {
    return accountType == AccountType.MUTUAL_FUNDS
}

fun Account.isStocks(): Boolean {
    return accountType == AccountType.STOCKS
}

