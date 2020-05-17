package com.loguito.brainmove.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.loguito.brainmove.R
import com.loguito.brainmove.models.remote.Workout
import kotlinx.android.synthetic.main.item_workout_cell.view.*

class WorkoutDetailAdapter : RecyclerView.Adapter<WorkoutDetailAdapter.WorkoutDetailViewHolder>() {

    private val _workoutClicked: MutableLiveData<Workout> = MutableLiveData()

    val workoutClicked: LiveData<Workout>
        get() = _workoutClicked

    var workoutList: List<Workout> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutDetailViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_workout_cell, parent, false)
        return WorkoutDetailViewHolder(view)
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    override fun onBindViewHolder(holder: WorkoutDetailViewHolder, position: Int) {
        holder.bind(workoutList[position])
    }

    inner class WorkoutDetailViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(workout: Workout) {
            view.setOnClickListener {
                _workoutClicked.postValue(workout)
            }
            view.workoutTitleTextView.text = workout.name
            Glide.with(view).load(workout.imageUrl).into(view.workoutImageView)
        }
    }
}