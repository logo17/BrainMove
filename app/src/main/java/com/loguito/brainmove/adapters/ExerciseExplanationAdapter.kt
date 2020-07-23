package com.loguito.brainmove.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.view.clicks
import com.loguito.brainmove.R
import com.loguito.brainmove.utils.Constants
import com.loguito.brainmove.utils.SingleLiveEvent
import kotlinx.android.synthetic.main.exercise_explanation_item_cell.view.*
import java.util.concurrent.TimeUnit

class ExerciseExplanationAdapter :
    RecyclerView.Adapter<ExerciseExplanationAdapter.ExerciseExplanationViewHolder>() {

    var deleteButtonVisibility = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var explanations: List<String> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val _deleteExplanation = SingleLiveEvent<String>()

    val deleteExplanation: LiveData<String>
        get() = _deleteExplanation

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExerciseExplanationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.exercise_explanation_item_cell, parent, false)
        return ExerciseExplanationViewHolder(view)
    }

    override fun getItemCount() = explanations.size

    override fun onBindViewHolder(holder: ExerciseExplanationViewHolder, position: Int) {
        holder.bind(position, explanations[position])
    }

    inner class ExerciseExplanationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("CheckResult")
        fun bind(position: Int, text: String) {
            itemView.numberTextView.text = "${position + 1}"
            itemView.explanationTextView.text = text
            itemView.deleteButton.visibility =
                if (deleteButtonVisibility) View.VISIBLE else View.GONE

            itemView.deleteButton.clicks()
                .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
                .subscribe {
                    _deleteExplanation.postValue(explanations[adapterPosition])
                }
        }
    }
}