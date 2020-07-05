package com.loguito.brainmove.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.loguito.brainmove.R
import com.loguito.brainmove.models.remote.Block
import com.loguito.brainmove.utils.SingleLiveEvent
import kotlinx.android.synthetic.main.routine_item_cell.view.*

class RoutineAdapter : RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>() {

    private val _selectedBlock = SingleLiveEvent<Block>()

    val selectedBlock: LiveData<Block>
        get() = _selectedBlock

    var blocks: List<Block> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.routine_item_cell, parent, false)
        return RoutineViewHolder(view)
    }

    override fun getItemCount() = blocks.size

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        holder.bind(blocks[position])
    }

    inner class RoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(block: Block) {
            itemView.setOnClickListener { _selectedBlock.postValue(block) }
            Glide.with(itemView.context).load(block.imageUrl)
                .placeholder(R.drawable.loading_background_placeholder)
                .into(itemView.routineImageView)
            itemView.routineNameTextView.text = block.name
        }
    }
}