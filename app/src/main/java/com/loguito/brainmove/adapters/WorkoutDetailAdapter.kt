package com.loguito.brainmove.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.loguito.brainmove.R
import com.loguito.brainmove.models.remote.Exercise
import com.loguito.brainmove.utils.SingleLiveEvent
import kotlinx.android.synthetic.main.item_workout_cell.view.*

class WorkoutDetailAdapter : RecyclerView.Adapter<WorkoutDetailAdapter.ExerciseViewHolder>() {

    private val _exerciseClicked: SingleLiveEvent<Exercise> = SingleLiveEvent()

    val exerciseClicked: SingleLiveEvent<Exercise>
        get() = _exerciseClicked

    var exerciseList: List<Exercise> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_workout_cell, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(exerciseList[position])
    }

    inner class ExerciseViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(exercise: Exercise) {
            view.setOnClickListener {
                _exerciseClicked.postValue(exercise)
            }
            view.workoutTitleTextView.text = exercise.name
            view.workoutQuantityTextView.text = exercise.quantity
            Glide.with(view)
                .load(exercise.backgroundImageUrl)
                .into(view.workoutImageView)
        }
    }
}