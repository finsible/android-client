package com.itsjeel01.finsiblefrontend.ui.util

import com.itsjeel01.finsiblefrontend.common.UserLocaleRegistry
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateUtils {

    private fun getFormatter(pattern: String): DateTimeFormatter {
        return DateTimeFormatter.ofPattern(pattern, UserLocaleRegistry.currentLocale())
            .withZone(ZoneId.systemDefault())
    }

    fun formatDateHeader(
        timestampMs: Long,
        todayLabel: String,
        yesterdayLabel: String
    ): String {
        val targetDate = Instant.ofEpochMilli(timestampMs).atZone(ZoneId.systemDefault()).toLocalDate()
        val today = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()

        return when (targetDate) {
            today -> todayLabel
            today.minusDays(1) -> yesterdayLabel
            else -> getFormatter("dd MMM yyyy").format(targetDate)
        }
    }

    fun readableDate(timestampMs: Long): String {
        return getFormatter("dd MMM yyyy").format(Instant.ofEpochMilli(timestampMs))
    }

    fun formatTime(timestampMs: Long): String {
        return getFormatter("h:mm a").format(Instant.ofEpochMilli(timestampMs))
    }

    fun getStartOfDayMs(timestampMs: Long): Long {
        return Instant.ofEpochMilli(timestampMs)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    fun getEndOfDayMs(startOfDayMs: Long): Long {
        return Instant.ofEpochMilli(startOfDayMs)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .atTime(LocalTime.MAX)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }
}

