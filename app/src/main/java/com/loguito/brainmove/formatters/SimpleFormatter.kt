package com.loguito.brainmove.formatters

import com.github.mikephil.charting.formatter.ValueFormatter
import com.loguito.brainmove.ext.formatToString

class SimpleFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return value.toDouble().formatToString()
    }
}