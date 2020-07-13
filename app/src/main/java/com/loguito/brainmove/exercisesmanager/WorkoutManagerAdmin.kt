package com.loguito.brainmove.exercisesmanager

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.loguito.brainmove.utils.Utils
import java.lang.reflect.Type

class WorkoutManagerAdmin(context: Context) {
    private val db = FirebaseFirestore.getInstance()

    init {
        val exercisesAsString: String? =
            Utils.getJsonFromAssets(
                context,
                "exercise_data_info.json"
            )
        val gson = Gson()

        val listExerciseType: Type = object : TypeToken<List<RemoteExercise?>?>() {}.type
        val exercises: List<RemoteExercise> = gson.fromJson(exercisesAsString, listExerciseType)
        val count = exercises.size

        for(exercise in exercises) {
            db.collection("exercise")
                .add(exercise)
                .addOnCompleteListener {
                    Log.d("Created", it.result?.id)
                }
        }
    }
}