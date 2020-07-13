package com.loguito.brainmove.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.loguito.brainmove.R
import com.loguito.brainmove.models.remote.Routine
import com.loguito.brainmove.utils.SingleLiveEvent
import kotlinx.android.synthetic.main.plan_item_cell.view.*

class PlanAdapter : RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {

    private val _selectedRoutine = SingleLiveEvent<Routine>()

    val selectedRoutine: LiveData<Routine>
        get() = _selectedRoutine

    var routines: List<Routine> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.plan_item_cell, parent, false)
        return PlanViewHolder(view)
    }

    override fun getItemCount() = routines.size

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        holder.bind(routines[position])
    }

    inner class PlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(routine: Routine) {
            itemView.setOnClickListener {
                _selectedRoutine.postValue(routine)
            }
            itemView.routineNameTextView.text = routine.name
        }
    }
}