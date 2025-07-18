package com.itsjeel01.finsiblefrontend.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import com.itsjeel01.finsiblefrontend.ui.theme.ColorKey
import com.itsjeel01.finsiblefrontend.ui.theme.getCustomColor
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class Utils {
    companion object {

        /** Converts local time in milliseconds to UTC time in milliseconds. */
        fun convertToUTCMillis(localMillis: Long): Long {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = localMillis

            val timeZoneOffset = calendar.timeZone.getOffset(localMillis)
            return localMillis + timeZoneOffset
        }

        /** Converts UTC time in milliseconds to local time in milliseconds. */
        fun convertToLocalMillis(utcMillis: Long): Long {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = utcMillis

            val timeZoneOffset = calendar.timeZone.getOffset(utcMillis)
            return utcMillis - timeZoneOffset
        }

        fun convertMillisToDate(millis: Long): String {
            val date = Date(millis)
            val now = Date()
            val diffInMillis = now.time - millis
            val diffInHours = diffInMillis / (1000 * 60 * 60)
            val diffInDays = diffInHours / 24

            return when {
                diffInDays == 0L && diffInHours < 24 -> "Today"
                diffInDays == 1L -> "Yesterday"
                diffInDays in 2..7 -> SimpleDateFormat(
                    "dd/MM (EEE)",
                    java.util.Locale.getDefault()
                ).format(date)

                else -> SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(date)
            }
        }

        /** Returns a list of colors for the category colors. */
        @Composable
        fun getCategoryColorsList(): List<Color> {
            val colors = listOf(
                ColorKey.YELLOW,
                ColorKey.ORANGE,
                ColorKey.RED,
                ColorKey.PINK,
                ColorKey.PURPLE,
                ColorKey.BLUE,
                ColorKey.GREEN,
                ColorKey.GRAY
            )
            return colors.map { getCustomColor(it) }
        }


        /** Returns the color for a given category name. */
        @Composable
        fun getCategoryColor(color: String): Color {
            val colorKey = runCatching {
                ColorKey.valueOf(color.toUpperCase(Locale.current))
            }.getOrElse {
                ColorKey.GRAY
            }

            return getCustomColor(key = colorKey)
        }

        /** Returns the color for a given transaction type. */
        @Composable
        fun getTransactionColor(type: TransactionType): Color {
            return when (type) {
                TransactionType.INCOME -> getCustomColor(key = ColorKey.Income)
                TransactionType.EXPENSE -> getCustomColor(key = ColorKey.Expense)
                TransactionType.TRANSFER -> getCustomColor(key = ColorKey.Transfer)
            }
        }

        /** Convert a string to title case, replacing underscores and spaces with single spaces. */
        fun toTitleCase(input: String): String {
            return input.trim()
                .split(Regex("[ _]+"))
                .joinToString(" ") { it.lowercase().replaceFirstChar { char -> char.uppercase() } }
        }

        /** Format number according to locale. */
        fun formatNumber(value: Double): String {
            return java.text.NumberFormat.getNumberInstance(java.util.Locale.getDefault())
                .format(value)
        }
    }
}
