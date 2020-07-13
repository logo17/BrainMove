package com.loguito.brainmove.ext

import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.*

fun CharSequence.isValidEmail() =
    !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun CharSequence.isValidPassword() = !isNullOrEmpty() && length >= 6

fun CharSequence.isValidName() = !isNullOrEmpty()

fun String.shorDateStringToDate(): Date {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.parse(this) ?: Date()
}