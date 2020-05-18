package com.loguito.brainmove.ext

import android.util.Patterns

fun CharSequence.isValidEmail() =
    !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun CharSequence.isValidPassword() = !isNullOrEmpty() && length >= 6

fun CharSequence.isValidName() = !isNullOrEmpty()