package com.itsjeel01.finsiblefrontend.ui.mapper

import com.google.common.truth.Truth.assertThat
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountGroupEntity
import io.mockk.every
import io.mockk.mockk
import io.objectbox.relation.ToOne
import org.junit.Test

class AccountMapperTest {

    private val currencyFormatter: CurrencyFormatter = mockk {
        every { format(any<Long>(), any(), any()) } returns "₹1,234.56"
    }

    @Test
    fun `maps all fields from entity to UI model`() {
        val accountGroup = AccountGroupEntity(
            id = 5L,
            name = "Savings",
            color = "blue",
        )
        val toOne: ToOne<AccountGroupEntity> = mockk {
            every { target } returns accountGroup
        }
        val entity = AccountEntity(
            id = 1L,
            name = "HDFC Savings",
            description = "Main savings account",
            balanceCentis = 123_456L,
            currencyCode = "INR",
            icon = "account_balance",
            isActive = true,
            isSystemDefault = false,
            usageCount = 25L,
            lastUsedAt = 1_700_000_000_000L,
        ).also { it.accountGroup = toOne }

        val uiModel = entity.toUiModel(currencyFormatter)

        assertThat(uiModel.id).isEqualTo(1L)
        assertThat(uiModel.name).isEqualTo("HDFC Savings")
        assertThat(uiModel.description).isEqualTo("Main savings account")
        assertThat(uiModel.icon).isEqualTo("account_balance")
        assertThat(uiModel.currencyCode).isEqualTo("INR")
        assertThat(uiModel.formattedBalance).isEqualTo("₹1,234.56")
        assertThat(uiModel.groupColor).isEqualTo("blue")
        assertThat(uiModel.groupName).isEqualTo("Savings")
        assertThat(uiModel.isPositiveBalance).isTrue()
        assertThat(uiModel.usageCount).isEqualTo(25L)
        assertThat(uiModel.lastUsedAt).isEqualTo(1_700_000_000_000L)
    }

    @Test
    fun `negative balance is detected`() {
        val accountGroup: ToOne<AccountGroupEntity> = mockk {
            every { target } returns AccountGroupEntity(id = 1L, name = "Test", color = "red")
        }
        val entity = AccountEntity(
            id = 2L,
            name = "Credit Card",
            balanceCentis = -50_000L,
            currencyCode = "INR",
        ).also { it.accountGroup = accountGroup }

        val uiModel = entity.toUiModel(currencyFormatter)

        assertThat(uiModel.isPositiveBalance).isFalse()
    }

    @Test
    fun `zero balance does not crash`() {
        val accountGroup: ToOne<AccountGroupEntity> = mockk {
            every { target } returns AccountGroupEntity(id = 1L, name = "Test", color = "red")
        }
        val entity = AccountEntity(
            id = 3L,
            name = "Wallet",
            balanceCentis = 0L,
            currencyCode = "INR",
        ).also { it.accountGroup = accountGroup }

        val uiModel = entity.toUiModel(currencyFormatter)

        assertThat(uiModel.isPositiveBalance).isTrue()
        assertThat(uiModel.formattedBalance).isEqualTo("₹1,234.56")
    }

    @Test
    fun `null account group fields do not crash`() {
        val accountGroup: ToOne<AccountGroupEntity> = mockk {
            every { target } returns AccountGroupEntity(id = 1L)
        }
        val entity = AccountEntity(
            id = 4L,
            name = "Cash",
            balanceCentis = 100L,
            currencyCode = "USD",
        ).also { it.accountGroup = accountGroup }

        val uiModel = entity.toUiModel(currencyFormatter)

        assertThat(uiModel.groupColor).isEqualTo("neutral")
        assertThat(uiModel.groupName).isEmpty()
    }

    @Test
    fun `null lastUsedAt does not crash`() {
        val accountGroup: ToOne<AccountGroupEntity> = mockk {
            every { target } returns AccountGroupEntity(id = 1L, name = "Test", color = "blue")
        }
        val entity = AccountEntity(
            id = 5L,
            name = "No Recent Use",
            balanceCentis = 500L,
            currencyCode = "INR",
            lastUsedAt = null,
        ).also { it.accountGroup = accountGroup }

        val uiModel = entity.toUiModel(currencyFormatter)

        assertThat(uiModel.lastUsedAt).isNull()
    }
}
