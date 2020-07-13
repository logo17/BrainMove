package com.loguito.brainmove.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loguito.brainmove.R
import kotlinx.android.synthetic.main.exercise_explanation_item_cell.view.*

class ExerciseExplanationAdapter :
    RecyclerView.Adapter<ExerciseExplanationAdapter.ExerciseExplanationViewHolder>() {

    var explanations: List<String> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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
        fun bind(position: Int, text: String) {
            itemView.numberTextView.text = "${position+1}"
            itemView.explanationTextView.text = text
        }
    }
}