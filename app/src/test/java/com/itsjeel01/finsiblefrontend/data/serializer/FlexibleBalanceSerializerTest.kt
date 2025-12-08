package com.itsjeel01.finsiblefrontend.data.serializer

import com.itsjeel01.finsiblefrontend.data.model.Account
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

/** Unit tests for FlexibleBalanceSerializer to verify it handles both String and Double. */
class FlexibleBalanceSerializerTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun deserialize_stringBalance_returnsString() {
        val jsonString = """
            {
                "id": 1,
                "name": "Test Account",
                "description": "Test",
                "accountGroupId": 1,
                "balance": "1234.56",
                "currencyCode": "INR",
                "icon": "test_icon",
                "isActive": true,
                "isSystemDefault": false
            }
        """.trimIndent()

        val account = json.decodeFromString<Account>(jsonString)
        assertEquals("1234.56", account.balance)
    }

    @Test
    fun deserialize_doubleBalance_convertsToString() {
        val jsonString = """
            {
                "id": 1,
                "name": "Test Account",
                "description": "Test",
                "accountGroupId": 1,
                "balance": 1234.56,
                "currencyCode": "INR",
                "icon": "test_icon",
                "isActive": true,
                "isSystemDefault": false
            }
        """.trimIndent()

        val account = json.decodeFromString<Account>(jsonString)
        assertEquals("1234.56", account.balance)
    }

    @Test
    fun deserialize_integerBalance_convertsToString() {
        val jsonString = """
            {
                "id": 1,
                "name": "Test Account",
                "description": "Test",
                "accountGroupId": 1,
                "balance": 1234,
                "currencyCode": "INR",
                "icon": "test_icon",
                "isActive": true,
                "isSystemDefault": false
            }
        """.trimIndent()

        val account = json.decodeFromString<Account>(jsonString)
        assertEquals("1234.0", account.balance)
    }

    @Test
    fun deserialize_negativeBalance_preservesSign() {
        val jsonString = """
            {
                "id": 1,
                "name": "Test Account",
                "description": "Test",
                "accountGroupId": 1,
                "balance": "-5000.75",
                "currencyCode": "INR",
                "icon": "test_icon",
                "isActive": true,
                "isSystemDefault": false
            }
        """.trimIndent()

        val account = json.decodeFromString<Account>(jsonString)
        assertEquals("-5000.75", account.balance)
    }

    @Test
    fun deserialize_nullAccountGroupId_handlesCorrectly() {
        val jsonString = """
            {
                "id": 1,
                "name": "Test Account",
                "description": "Test",
                "accountGroupId": null,
                "balance": "100.00",
                "currencyCode": "INR",
                "icon": "test_icon",
                "isActive": true,
                "isSystemDefault": false
            }
        """.trimIndent()

        val account = json.decodeFromString<Account>(jsonString)
        assertEquals(null, account.accountGroupId)
        assertEquals("100.00", account.balance)
    }

    @Test
    fun serialize_stringBalance_encodesAsString() {
        val account = Account(
            id = 1,
            name = "Test Account",
            description = "Test",
            accountGroupId = 1,
            balance = "1234.56",
            currencyCode = "INR",
            icon = "test_icon",
            isActive = true,
            isSystemDefault = false
        )

        val jsonString = json.encodeToString(Account.serializer(), account)
        assert(jsonString.contains("\"balance\":\"1234.56\""))
    }
}
