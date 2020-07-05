package com.loguito.brainmove.models.remote

import java.util.*

data class UnitMeasure(var unit: String = "") {
    @ExperimentalStdlibApi
    override fun toString() = unit.capitalize(Locale.getDefault())
}