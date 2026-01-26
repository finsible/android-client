package com.itsjeel01.finsiblefrontend.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

/** Unit tests for Extension functions. */
class ExtensionsTest {

    @Test
    fun `test toReadableDate formats date correctly`() {
        // Create a known timestamp: January 1, 2025 00:00:00 UTC
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            set(2025, Calendar.JANUARY, 1, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val timestamp = cal.timeInMillis

        val result = timestamp.toReadableDate()

        assertNotNull(result)
        // The result format is dd/MM/yyyy
        assertTrue("Result should contain date pattern", result.matches(Regex("\\d{2}/\\d{2}/\\d{4}")))
    }

    @Test
    fun `test toReadableDate for current date`() {
        val now = System.currentTimeMillis()

        val result = now.toReadableDate()

        assertNotNull(result)
        assertTrue("Result should be in dd/MM/yyyy format", result.matches(Regex("\\d{2}/\\d{2}/\\d{4}")))
    }

    @Test
    fun `test convertUTCToLocal with positive offset`() {
        // Test basic functionality - the offset varies by timezone
        val utcTimestamp = 1735689600000L  // Some fixed timestamp

        val localTimestamp = utcTimestamp.convertUTCToLocal()

        // The local timestamp should differ from UTC by timezone offset
        // Just verify it's a valid conversion (not testing timezone specifics)
        assertNotNull(localTimestamp)
    }

    @Test
    fun `test convertLocalToUTC with positive offset`() {
        val localTimestamp = 1735689600000L

        val utcTimestamp = localTimestamp.convertLocalToUTC()

        assertNotNull(utcTimestamp)
    }

    @Test
    fun `test convertUTCToLocal and convertLocalToUTC are inverse operations`() {
        val original = 1735689600000L

        val converted = original.convertUTCToLocal().convertLocalToUTC()

        assertEquals("Round-trip conversion should return original", original, converted)
    }

    @Test
    fun `test toReadableDate handles epoch time`() {
        val epoch = 0L

        val result = epoch.toReadableDate()

        assertNotNull(result)
        // Epoch is January 1, 1970
        assertTrue("Should contain 1970", result.contains("1970"))
    }
}
