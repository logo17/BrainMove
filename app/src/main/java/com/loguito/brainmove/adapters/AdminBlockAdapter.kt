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
import com.loguito.brainmove.models.remote.Block
import com.loguito.brainmove.utils.Constants
import com.loguito.brainmove.utils.SingleLiveEvent
import kotlinx.android.synthetic.main.admin_block_item_cell.view.*
import java.util.concurrent.TimeUnit

class AdminBlockAdapter(val enableControlButtons: Boolean = false) :
    RecyclerView.Adapter<AdminBlockAdapter.AdminBlockViewHolder>() {

    var blocks: List<Block> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val _modifyBlock = SingleLiveEvent<Pair<Int, Block>>()
    private val _removeBlock = SingleLiveEvent<Int>()

    val modifyBlock: LiveData<Pair<Int, Block>>
        get() = _modifyBlock
    val removeBlock: LiveData<Int>
        get() = _removeBlock

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminBlockViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_block_item_cell, parent, false)
        return AdminBlockViewHolder(view)
    }

    override fun getItemCount() = blocks.size

    override fun onBindViewHolder(holder: AdminBlockViewHolder, position: Int) {
        holder.bind(blocks[position])
    }

    inner class AdminBlockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("CheckResult")
        fun bind(block: Block) {
            val adapter = AdminExerciseAdapter()

            itemView.removeExerciseButton.visibility =
                if (enableControlButtons) View.VISIBLE else View.GONE
            itemView.modifyExerciseButton.visibility =
                if (enableControlButtons) View.VISIBLE else View.GONE

            itemView.modifyExerciseButton.clicks()
                .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
                .subscribe {
                    _modifyBlock.postValue(Pair(adapterPosition, block))
                }

            itemView.removeExerciseButton.clicks()
                .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
                .subscribe {
                    _removeBlock.postValue(adapterPosition)
                }

            itemView.blockNameTextView.text = block.name
            itemView.blockDurationTextView.text = String.format("%d %s", block.duration, block.unit)
            itemView.exercisesRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            itemView.exercisesRecyclerView.adapter = adapter
            ContextCompat.getDrawable(itemView.context, R.drawable.divider_item_bg)?.let {
                itemView.exercisesRecyclerView.addItemDecoration(DividerItemDecorator(it))
            }
            adapter.exercises = block.exercises
        }
    }
}