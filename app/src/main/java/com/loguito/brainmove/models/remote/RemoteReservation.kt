package com.loguito.brainmove.models.remote

import java.util.*

data class RemoteReservation(
    val activityId: String = "",
    val maxCapacity: Int = 0,
    val date: Date = Date(),
    val spaces: List<String> = emptyList()
)