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
import com.loguito.brainmove.models.remote.Block
import kotlinx.android.synthetic.main.admin_block_item_cell.view.*

class AdminBlockAdapter : RecyclerView.Adapter<AdminBlockAdapter.AdminBlockViewHolder>() {

    var blocks: List<Block> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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
        fun bind(block: Block) {
            val adapter = AdminExerciseAdapter()

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