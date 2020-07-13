package com.loguito.brainmove.ext

import java.text.SimpleDateFormat
import java.util.*

fun Date.toReadableDate(): String {
    val locale = Locale("es", "ES")
    val format = SimpleDateFormat("EEEE, dd MMMM, yyyy", locale)
    return format.format(this).capitalize()
}

fun Date.toShortDate(): String {
    val locale = Locale("es", "ES")
    val format = SimpleDateFormat("dd/MM/yyyy", locale)
    return format.format(this)
}

fun Date.toHour(): String {
    val format = SimpleDateFormat("hh:mm aa", Locale.US)
    return format.format(this)
}

fun Date.toReadableDateFromPicker(): String {
    val locale = Locale("es", "ES")
    val format = SimpleDateFormat("EEEE, dd MMMM, yyyy", locale)
    format.setTimeZone(TimeZone.getTimeZone("UTC-6"));
    return format.format(this).capitalize()
}

fun Date.toShortDateFromPicker(): String {
    val locale = Locale("es", "ES")
    val format = SimpleDateFormat("dd/MM/yyyy", locale)
    format.setTimeZone(TimeZone.getTimeZone("UTC-6"));
    return format.format(this).capitalize()
}