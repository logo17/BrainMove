package com.loguito.brainmove.exercisesmanager

data class RemoteExercise(
    val backgroundImageUrl: String = "",
    val demoUrl: String = "",
    val name: String = "",
    val keywords: List<String> = emptyList(),
    val explanations: List<String> = emptyList()
)