package com.loguito.brainmove.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.loguito.brainmove.R
import com.loguito.brainmove.models.remote.Exercise
import kotlinx.android.synthetic.main.admin_exercise_item_cell.view.*
import java.util.*
import kotlin.collections.ArrayList

class AdminExerciseAdapter : RecyclerView.Adapter<AdminExerciseAdapter.AdminExerciseViewHolder>(),
    Filterable {

    private val _selectedOption = MutableLiveData<Exercise>()
    var searchableList: MutableList<Exercise> = arrayListOf()

    val selectedOption: LiveData<Exercise>
        get() = _selectedOption

    var exercises: List<Exercise> = ArrayList()
        set(value) {
            field = value
            searchableList = ArrayList(field)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_exercise_item_cell, parent, false)
        return AdminExerciseViewHolder(view)
    }

    override fun getItemCount() = searchableList.size

    override fun onBindViewHolder(holder: AdminExerciseViewHolder, position: Int) {
        holder.bind(searchableList[position])
    }

    inner class AdminExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(exercise: Exercise) {
            val quantityAsText = "${exercise.quantity} - "
            itemView.exerciseNameTextView.text = "${quantityAsText}${exercise.name}"
            itemView.setOnClickListener { _selectedOption.postValue(exercise) }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val filterResults = FilterResults()
                if (charSequence.isEmpty()) {
                    filterResults.count = exercises.size
                    filterResults.values = exercises
                } else {
                    val searchChr = charSequence.toString().toLowerCase(Locale.getDefault())
                    val resultData: MutableList<Exercise> = ArrayList()
                    for (exercise in exercises) {
                        if (exercise.name.toLowerCase(Locale.getDefault()).contains(searchChr)) {
                            resultData.add(exercise)
                        }
                    }
                    filterResults.count = resultData.size
                    filterResults.values = resultData
                }
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                searchableList = filterResults.values as MutableList<Exercise>
                notifyDataSetChanged()
            }
        }
    }
}