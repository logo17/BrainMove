package com.loguito.brainmove.formatters

import com.github.mikephil.charting.formatter.ValueFormatter


class WeightFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return "$value kg"
    }
}