package com.loguito.brainmove.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.loguito.brainmove.R
import com.loguito.brainmove.models.remote.User
import com.loguito.brainmove.utils.SingleLiveEvent
import kotlinx.android.synthetic.main.user_detail_item_cell.view.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val _selectedUser = SingleLiveEvent<User>()

    val selectedUser: LiveData<User>
        get() = _selectedUser

    var users: List<User> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.user_detail_item_cell, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            itemView.setOnClickListener {
                _selectedUser.postValue(user)
            }
            itemView.userNameTextView.text = user.fullName
            itemView.userEmailTextView.text = user.email
        }
    }
}