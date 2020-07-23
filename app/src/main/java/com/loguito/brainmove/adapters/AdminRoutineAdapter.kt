package com.loguito.brainmove.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.view.clicks
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.decorator.DividerItemDecorator
import com.loguito.brainmove.adapters.decorator.VerticalSpaceItemDecoration
import com.loguito.brainmove.models.remote.Routine
import com.loguito.brainmove.utils.Constants
import com.loguito.brainmove.utils.SingleLiveEvent
import kotlinx.android.synthetic.main.admin_routine_item_cell.view.*
import java.util.concurrent.TimeUnit

class AdminRoutineAdapter(val enableControlButtons: Boolean = false) :
    RecyclerView.Adapter<AdminRoutineAdapter.AdminRoutineViewHolder>() {

    var routines: List<Routine> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val _modifyRoutine = SingleLiveEvent<Pair<Int, Routine>>()
    private val _removeRoutine = SingleLiveEvent<Int>()

    val modifyRoutine: LiveData<Pair<Int, Routine>>
        get() = _modifyRoutine
    val removeRoutine: LiveData<Int>
        get() = _removeRoutine

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
        @SuppressLint("CheckResult")
        fun bind(routine: Routine) {
            val adapter = AdminBlockAdapter()
            itemView.removeExerciseButton.visibility =
                if (enableControlButtons) View.VISIBLE else View.GONE
            itemView.modifyExerciseButton.visibility =
                if (enableControlButtons) View.VISIBLE else View.GONE

            itemView.modifyExerciseButton.clicks()
                .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
                .subscribe {
                    _modifyRoutine.postValue(Pair(adapterPosition, routine))
                }

            itemView.removeExerciseButton.clicks()
                .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
                .subscribe {
                    _removeRoutine.postValue(adapterPosition)
                }
            itemView.routineNameTextView.text = routine.name
//            itemView.routineNumberTextView.text = "${routine.routineNumber}"
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