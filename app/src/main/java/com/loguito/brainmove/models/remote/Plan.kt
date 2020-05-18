package com.loguito.brainmove.models.remote

import java.util.*

data class Plan(
    val userId: String = "",
    val name: String = "",
    val goal: String = "",
    val toDate: Date = Date(),
    val fromDate: Date = Date(),
    val routines: List<Routine> = emptyList()
)