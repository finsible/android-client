package com.itsjeel01.finsiblefrontend.common

// UTC to Local Time
fun Long.convertUTCToLocal(): Long {
    val timeZone = java.util.TimeZone.getDefault()
    val offset = timeZone.getOffset(this)
    return this + offset
}

// Local Time to UTC
fun Long.convertLocalToUTC(): Long {
    val timeZone = java.util.TimeZone.getDefault()
    val offset = timeZone.getOffset(this)
    return this - offset
}

fun Long.toReadableDate(): String {
    val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
    return dateFormat.format(java.util.Date(this))
}