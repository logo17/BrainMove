package com.loguito.brainmove.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.decorator.DividerItemDecorator
import com.loguito.brainmove.adapters.decorator.VerticalSpaceItemDecoration
import com.loguito.brainmove.models.remote.Routine
import kotlinx.android.synthetic.main.admin_routine_item_cell.view.*

class AdminRoutineAdapter : RecyclerView.Adapter<AdminRoutineAdapter.AdminRoutineViewHolder>() {

    var routines: List<Routine> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminRoutineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_routine_item_cell, parent, false)
        return AdminRoutineViewHolder(view)
    }

    override fun getItemCount() = routines.size

    override fun onBindViewHolder(holder: AdminRoutineViewHolder, position: Int) {
        holder.bind(routines[position])
    }

    inner class AdminRoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(routine: Routine) {
            val adapter = AdminBlockAdapter()

            itemView.routineNameTextView.text = routine.name
            itemView.routineNumberTextView.text = "${routine.routineNumber}"
            itemView.blocksRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            itemView.blocksRecyclerView.adapter = adapter
            ContextCompat.getDrawable(itemView.context, R.drawable.divider_item_bg)?.let {
                itemView.blocksRecyclerView.addItemDecoration(DividerItemDecorator(it))
            }
            itemView.blocksRecyclerView.addItemDecoration(VerticalSpaceItemDecoration(30))
            adapter.blocks = routine.blocks
        }
    }
}