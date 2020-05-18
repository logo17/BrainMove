package com.loguito.brainmove.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.widget.textChanges
import com.loguito.brainmove.R
import com.loguito.brainmove.models.remote.Exercise
import com.loguito.brainmove.utils.Constants
import kotlinx.android.synthetic.main.admin_selected_exercise_item_cell.view.*
import java.util.concurrent.TimeUnit

class AdminSelectedExerciseAdapter :
    RecyclerView.Adapter<AdminSelectedExerciseAdapter.AdminSelectedExerciseViewHolder>() {

    private val _removeExercise = MutableLiveData<Exercise>()
    private val _updatedExercise = MutableLiveData<Pair<Int, Exercise>>()

    val removeExercise: LiveData<Exercise>
        get() = _removeExercise
    val updatedExercise: LiveData<Pair<Int, Exercise>>
        get() = _updatedExercise

    var exercises: List<Exercise> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdminSelectedExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_selected_exercise_item_cell, parent, false)
        return AdminSelectedExerciseViewHolder(view)
    }

    override fun getItemCount() = exercises.size

    override fun onBindViewHolder(holder: AdminSelectedExerciseViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    inner class AdminSelectedExerciseViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(exercise: Exercise) {
            itemView.exerciseQuantityEditText.setText(exercise.quantity.toString())
            itemView.exerciseNameTextView.text = exercise.name
            itemView.removeExerciseButton.setOnClickListener {
                _removeExercise.postValue(exercise)
            }
            itemView.exerciseQuantityEditText.textChanges()
                .skipInitialValue()
                .debounce(Constants.DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
                .doOnNext {
                    val quantity = it.toString()
                    val exerciseCopy = exercise.copy(quantity = quantity)
                    _updatedExercise.postValue(Pair(adapterPosition, exerciseCopy))
                }
                .subscribe()
        }
    }
}