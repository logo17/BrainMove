package com.loguito.brainmove.ext

import java.text.DecimalFormat

fun Double.formatToKgString(): String {
    val formatter = DecimalFormat("#.##")
    return String.format("%s kg", formatter.format(this))
}

fun Double.formatToString(): String {
    val formatter = DecimalFormat("#.##")
    return String.format("%s", formatter.format(this))
}

fun Double.formatToPercentageString(): String {
    val formatter = DecimalFormat("#.##")
    return String.format("%s %%", formatter.format(this))
}

fun Double.formatToKcalString(): String {
    val formatter = DecimalFormat("#.##")
    return String.format("%s kcal", formatter.format(this))
}