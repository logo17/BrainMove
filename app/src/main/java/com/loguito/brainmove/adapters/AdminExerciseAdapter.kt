package com.loguito.brainmove.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.loguito.brainmove.R
import com.loguito.brainmove.models.remote.Exercise
import kotlinx.android.synthetic.main.admin_exercise_item_cell.view.*

class AdminExerciseAdapter : RecyclerView.Adapter<AdminExerciseAdapter.AdminExerciseViewHolder>() {

    private val _selectedOption = MutableLiveData<Exercise>()

    val selectedOption: LiveData<Exercise>
        get() = _selectedOption

    var exercises: List<Exercise> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_exercise_item_cell, parent, false)
        return AdminExerciseViewHolder(view)
    }

    override fun getItemCount() = exercises.size

    override fun onBindViewHolder(holder: AdminExerciseViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    inner class AdminExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(exercise: Exercise) {
            val quantityAsText = "${exercise.quantity} - "
            itemView.exerciseNameTextView.text = "${quantityAsText}${exercise.name}"
            itemView.setOnClickListener { _selectedOption.postValue(exercise) }
        }
    }
}