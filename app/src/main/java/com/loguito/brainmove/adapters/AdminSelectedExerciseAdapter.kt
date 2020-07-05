package com.loguito.brainmove.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.widget.itemSelections
import com.jakewharton.rxbinding3.widget.textChanges
import com.loguito.brainmove.R
import com.loguito.brainmove.models.remote.Exercise
import com.loguito.brainmove.models.remote.UnitMeasure
import com.loguito.brainmove.utils.Constants
import kotlinx.android.synthetic.main.admin_selected_exercise_item_cell.view.*
import kotlinx.android.synthetic.main.admin_selected_exercise_item_cell.view.unitMeasureDropdown
import kotlinx.android.synthetic.main.fragment_create_block.*
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

    var unitMeasures: List<UnitMeasure> = ArrayList()
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
        holder.bind(exercises[position], unitMeasures)
    }

    inner class AdminSelectedExerciseViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(exercise: Exercise, unitMeasures: List<UnitMeasure>) {
            itemView.exerciseQuantityEditText.setText(if (exercise.quantity.isEmpty()) "0" else exercise.quantity.split(" ")[0])
            itemView.exerciseNameTextView.text = exercise.name
            itemView.removeExerciseButton.setOnClickListener {
                _removeExercise.postValue(exercise)
            }
            val adapter = ArrayAdapter(itemView.context,
                android.R.layout.simple_spinner_dropdown_item, unitMeasures)
            itemView.unitMeasureDropdown.adapter = adapter
            itemView.exerciseQuantityEditText.textChanges()
                .skipInitialValue()
                .debounce(Constants.DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
                .doOnNext {
                    val quantity = if (it.toString().isEmpty()) "0" else it.toString()
                    val unitMeasure = unitMeasures[itemView.unitMeasureDropdown.selectedItemPosition]
                    val exerciseCopy = exercise.copy(quantity = String.format("%s %s", quantity, unitMeasure))
                    _updatedExercise.postValue(Pair(adapterPosition, exerciseCopy))
                }
                .subscribe()

            itemView.unitMeasureDropdown.itemSelections()
                .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
                .doOnNext {
                    val quantity = if (itemView.exerciseQuantityEditText.text.toString()
                            .isEmpty()
                    ) "0" else itemView.exerciseQuantityEditText.text.toString()
                    val unitMeasure = unitMeasures[it]
                    val exerciseCopy = exercise.copy(quantity = String.format("%s %s", quantity, unitMeasure))
                    _updatedExercise.postValue(Pair(adapterPosition, exerciseCopy))
                }
                .subscribe()
        }
    }
}