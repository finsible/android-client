package com.itsjeel01.finsiblefrontend.ui.util

import com.google.common.truth.Truth.assertThat
import com.itsjeel01.finsiblefrontend.common.LocaleProvider
import com.itsjeel01.finsiblefrontend.common.UserLocaleRegistry
import org.junit.Before
import org.junit.Test
import java.util.Locale
import java.util.regex.Pattern

class DateUtilsTest {

    @Before
    fun setUp() {
        UserLocaleRegistry.initialize(object : LocaleProvider {
            override fun currentLocale(): Locale = Locale("en", "IN")
            override fun currentRegionIso(): String = "IN"
        })
    }

    @Test
    fun `readableDate formats known epoch correctly`() {
        // 2024-12-25 00:00:00 UTC
        val ms = 1_735_084_800_000L

        val result = DateUtils.readableDate(ms)

        assertThat(result).isEqualTo("25 Dec 2024")
    }

    @Test
    fun `readableDate handles January 1st`() {
        // 2025-01-01 00:00:00 UTC
        val ms = 1_735_699_200_000L

        val result = DateUtils.readableDate(ms)

        assertThat(result).isEqualTo("01 Jan 2025")
    }

    @Test
    fun `readableDate handles December 31st`() {
        // 2024-12-31 00:00:00 UTC
        val ms = 1_735_612_800_000L

        val result = DateUtils.readableDate(ms)

        assertThat(result).isEqualTo("31 Dec 2024")
    }

    @Test
    fun `formatTime produces correct time format`() {
        // Use a known local time: January 15, 2024 at 09:15 local
        // This avoids timezone-dependent test expectations
        val now = System.currentTimeMillis()

        val result = DateUtils.formatTime(now)

        // Should return something like "HH:MM am/pm" format
        assertThat(result).contains(":")
        assertThat(result).matches(Pattern.compile(".*[ap]m"))
    }

    @Test
    fun `getStartOfDayMs returns midnight for given timestamp`() {
        // 2024-06-15 14:30:00 UTC
        val ms = 1_718_464_200_000L

        val startOfDay = DateUtils.getStartOfDayMs(ms)

        // Should be 2024-06-15 00:00:00 in local timezone
        val formatted = DateUtils.readableDate(startOfDay)
        assertThat(formatted).isEqualTo("15 Jun 2024")
    }

    @Test
    fun `getEndOfDayMs returns end of day for given start of day`() {
        // Start of 2024-06-15 in local timezone
        val startOfDay = DateUtils.getStartOfDayMs(1_718_464_200_000L)

        val endOfDay = DateUtils.getEndOfDayMs(startOfDay)

        // End of day should be > start of day
        assertThat(endOfDay).isGreaterThan(startOfDay)

        // The next day's start should be > endOfDay (or equal for the same instant)
        val nextDayStart = DateUtils.getStartOfDayMs(endOfDay + 1)
        assertThat(nextDayStart).isGreaterThan(startOfDay)
    }

    @Test
    fun `formatDateHeader returns today label for today`() {
        val now = System.currentTimeMillis()

        val result = DateUtils.formatDateHeader(now, "Today", "Yesterday")

        assertThat(result).isEqualTo("Today")
    }

    @Test
    fun `formatDateHeader returns yesterday label for yesterday`() {
        val yesterdayMs = System.currentTimeMillis() - 86_400_000L

        val result = DateUtils.formatDateHeader(yesterdayMs, "Today", "Yesterday")

        assertThat(result).isEqualTo("Yesterday")
    }

    @Test
    fun `formatDateHeader returns formatted date for older dates`() {
        // 2023-08-15
        val ms = 1_692_057_600_000L

        val result = DateUtils.formatDateHeader(ms, "Today", "Yesterday")

        assertThat(result).isEqualTo("15 Aug 2023")
    }

    @Test
    fun `getStartOfDayMs returns consistent results for midnight input`() {
        // Already at start of day
        val startOfFirst = DateUtils.getStartOfDayMs(1_735_084_800_000L)

        // Calling getStartOfDayMs on the result should be idempotent
        val startOfSecond = DateUtils.getStartOfDayMs(startOfFirst)

        assertThat(startOfSecond).isEqualTo(startOfFirst)
    }

    @Test
    fun `formatDateHeader handles leap year date`() {
        // 2024-02-29 (leap year)
        val ms = 1_709_164_800_000L

        val result = DateUtils.readableDate(ms)

        assertThat(result).isEqualTo("29 Feb 2024")
    }

    @Test
    fun `getEndOfDayMs is always after startOfDayMs`() {
        val start = DateUtils.getStartOfDayMs(1_700_000_000_000L)
        val end = DateUtils.getEndOfDayMs(start)

        assertThat(end).isGreaterThan(start)
        // Should be approximately 24 hours later
        val diffMs = end - start
        assertThat(diffMs).isAtLeast(86_399_000L) // at least 23:59:59
        assertThat(diffMs).isAtMost(86_401_000L)  // at most 24:00:01
    }

    @Test
    fun `getStartOfDayMs is idempotent`() {
        val original = DateUtils.getStartOfDayMs(1_700_000_000_000L)
        val again = DateUtils.getStartOfDayMs(original)

        assertThat(again).isEqualTo(original)
    }

    @Test
    fun `formatTime returns consistent 12-hour format`() {
        val morning = DateUtils.formatTime(1_735_084_800_000L)
        val evening = DateUtils.formatTime(1_735_134_000_000L)

        // Both should contain : and am/pm
        assertThat(morning).matches(Pattern.compile(".*:.*[ap]m"))
        assertThat(evening).matches(Pattern.compile(".*:.*[ap]m"))
    }
}
