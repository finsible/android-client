package com.itsjeel01.finsiblefrontend.data.local.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.itsjeel01.finsiblefrontend.data.model.Account
import com.itsjeel01.finsiblefrontend.data.model.AccountGroup
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AccountLocalRepositoryTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var accountLocalRepository: AccountLocalRepository

    @Inject
    lateinit var accountGroupLocalRepository: AccountGroupLocalRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun queryAllAccountsReturnsAllInserted() {
        accountLocalRepository.addAll(
            listOf(
                Account(id = 1, name = "Wallet", description = "", balance = "1.00", currencyCode = "INR", icon = "", isActive = true, isSystemDefault = false),
                Account(id = 2, name = "Credit Card", description = "", balance = "2.00", currencyCode = "INR", icon = "", isActive = true, isSystemDefault = false),
                Account(id = 3, name = "Savings", description = "", balance = "3.00", currencyCode = "INR", icon = "", isActive = true, isSystemDefault = false),
            ),
            additionalInfo = null
        )

        val all = accountLocalRepository.getActiveAccounts()
        assertThat(all).hasSize(3)
    }

    @Test
    fun queryByIdReturnsNullForNonExistentId() {
        val loaded = accountLocalRepository.getAccountsForGroup(999)
        assertThat(loaded).isEmpty()
    }
}
