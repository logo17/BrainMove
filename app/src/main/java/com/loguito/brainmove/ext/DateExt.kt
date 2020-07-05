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
    val format = SimpleDateFormat("dd/MM/YYYY", locale)
    return format.format(this)
}

fun Date.toHour(): String {
    val format = SimpleDateFormat("hh:mm aa", Locale.US)
    return format.format(this)
}