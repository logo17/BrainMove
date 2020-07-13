package com.loguito.brainmove.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.loguito.brainmove.R
import com.loguito.brainmove.models.local.TrendOption
import kotlinx.android.synthetic.main.trends_option_cell.view.*

class TrendOptionsAdapter : RecyclerView.Adapter<TrendOptionsAdapter.TrendOptionViewHolder>() {

    private val _selectedOption = MutableLiveData<Int>()

    val selectedOption: LiveData<Int>
        get() = _selectedOption

    var options: List<TrendOption> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var selectedIndex: Int = 0
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendOptionViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.trends_option_cell, parent, false)
        return TrendOptionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return options.size
    }

    override fun onBindViewHolder(holder: TrendOptionViewHolder, position: Int) {
        holder.bind(options[position])
    }

    inner class TrendOptionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(option: TrendOption) {
            view.titleTextView.text = option.title
            view.imageView.background = ContextCompat.getDrawable(view.context, option.icon)

            itemView.isSelected = selectedIndex == adapterPosition

            itemView.setOnClickListener {
                _selectedOption.postValue(adapterPosition)
                selectedIndex = adapterPosition
            }
        }
    }
}