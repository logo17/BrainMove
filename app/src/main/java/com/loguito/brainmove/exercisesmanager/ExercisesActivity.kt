package com.loguito.brainmove.exercisesmanager

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.decorator.VerticalSpaceItemDecoration
import com.loguito.brainmove.utils.Utils
import kotlinx.android.synthetic.main.activity_exercises.*
import java.lang.reflect.Type

class ExercisesActivity : Activity() {
    val adapter = ExerciseManagerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises)

        exerciseList.adapter = adapter
        exerciseList.layoutManager = LinearLayoutManager(this)
        exerciseList.addItemDecoration(VerticalSpaceItemDecoration(40))

        val exercisesAsString: String? =
            Utils.getJsonFromAssets(
                this,
                "exercise_data_info.json"
            )
        val gson = Gson()

        val listExerciseType: Type = object : TypeToken<List<RemoteExercise?>?>() {}.type
        val exercises: List<RemoteExercise> = gson.fromJson(exercisesAsString, listExerciseType)

        adapter.managerExercises = exercises

        // TODO: Remove this
        val workoutManagerAdmin =
            WorkoutManagerAdmin(this)
    }
}