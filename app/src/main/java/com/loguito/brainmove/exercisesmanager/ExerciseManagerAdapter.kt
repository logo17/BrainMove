package com.loguito.brainmove.exercisesmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.loguito.brainmove.R
import kotlinx.android.synthetic.main.exercise_manager_item.view.*

class ExerciseManagerAdapter :
    RecyclerView.Adapter<ExerciseManagerAdapter.ExerciseManagerViewHolder>() {
    var managerExercises: List<RemoteExercise> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExerciseManagerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.exercise_manager_item, parent, false)
        return ExerciseManagerViewHolder(view)
    }

    override fun getItemCount() = managerExercises.size

    override fun onBindViewHolder(
        holder: ExerciseManagerViewHolder,
        position: Int
    ) {
        holder.bind(managerExercises[position])
    }

    inner class ExerciseManagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(exercise: RemoteExercise) {
            itemView.textView3.text = exercise.name
            Glide.with(itemView.context)
                .load(exercise.demoUrl)
                .placeholder(R.drawable.loading_background_placeholder)
                .into(itemView.imageView7)
            Glide.with(itemView.context)
                .load(exercise.backgroundImageUrl)
                .placeholder(R.drawable.loading_background_placeholder)
                .into(itemView.imageView6)
            itemView.textView5.text = exercise.explanations.toString()
        }
    }
}