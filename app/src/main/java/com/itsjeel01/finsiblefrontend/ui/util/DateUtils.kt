package com.itsjeel01.finsiblefrontend.ui.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private val headerDateFormatter = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue() = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    }

    private val yyyyMMddFormatter = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue() = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    }

    private val timeFormatter = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue() = SimpleDateFormat("h:mm a", Locale.getDefault())
    }

    fun formatDateHeader(timestamp: Long): String {
        val date = Date(timestamp)
        val today = Date()
        val yesterday = Date(today.time - 24 * 60 * 60 * 1000)

        val formatter = yyyyMMddFormatter.get() ?: SimpleDateFormat("yyyyMMdd", Locale.getDefault())

        val transactionDay = formatter.format(date)
        val todayDay = formatter.format(today)
        val yesterdayDay = formatter.format(yesterday)

        return when (transactionDay) {
            todayDay -> "Today"
            yesterdayDay -> "Yesterday"
            else -> (headerDateFormatter.get() ?: SimpleDateFormat("dd MMM yyyy", Locale.getDefault())).format(date)
        }
    }

    fun readableDate(timestamp: Long): String {
        return (headerDateFormatter.get() ?: SimpleDateFormat("dd MMM yyyy", Locale.getDefault())).format(Date(timestamp))
    }

    fun formatTime(timestamp: Long): String {
        return (timeFormatter.get() ?: SimpleDateFormat("h:mm a", Locale.getDefault())).format(Date(timestamp))
    }
}

