package com.loguito.brainmove.models.remote

import java.util.*

data class UnitMeasure(var unit: String = "") {
    @ExperimentalStdlibApi
    override fun toString() = unit.capitalize(Locale.getDefault())
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UnitMeasure

        if (unit != other.unit) return false

        return true
    }

    override fun hashCode(): Int {
        return unit.hashCode()
    }
}