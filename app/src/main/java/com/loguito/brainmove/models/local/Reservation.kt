package com.loguito.brainmove.models.local

import java.util.*

data class Reservation(
    val id: String = "",
    val maxCapacity: Int = 0,
    val date: Date = Date(),
    val availableSpaces: Int = 0,
    val isReserved: Boolean = false,
    val activityId: String = ""
)