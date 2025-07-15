package com.itsjeel01.finsiblefrontend.common

import java.util.Calendar

class FSUtils {
    companion object {
        fun convertToUTCMillis(localMillis: Long): Long {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = localMillis

            // Returns the amount of time in milliseconds to add to UTC to get local time.
            val timeZoneOffset = calendar.timeZone.getOffset(localMillis)

            return localMillis + timeZoneOffset
        }

        fun convertToLocalMillis(utcMillis: Long): Long {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = utcMillis

            // Returns the amount of time in milliseconds to add to UTC to get local time.
            val timeZoneOffset = calendar.timeZone.getOffset(utcMillis)

            return utcMillis - timeZoneOffset
        }
    }
}
