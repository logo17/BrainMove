package com.loguito.brainmove.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.loguito.brainmove.R
import com.loguito.brainmove.ext.toHour
import com.loguito.brainmove.models.remote.RemoteReservation
import com.loguito.brainmove.utils.SingleLiveEvent
import kotlinx.android.synthetic.main.item_admin_reservation_list.view.*

class AdminReservationListAdapter :
    RecyclerView.Adapter<AdminReservationListAdapter.AdminReservationViewHolder>() {

    private val _selectedOption = SingleLiveEvent<RemoteReservation>()

    val selectedOption: LiveData<RemoteReservation>
        get() = _selectedOption

    var reservations: List<RemoteReservation> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminReservationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_reservation_list, parent, false)
        return AdminReservationViewHolder(view)
    }

    override fun getItemCount() = reservations.size

    override fun onBindViewHolder(holder: AdminReservationViewHolder, position: Int) {
        holder.bind(reservations[position])
    }

    inner class AdminReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(reservation: RemoteReservation) {
            itemView.setOnLongClickListener {
                _selectedOption.postValue(reservation)
                true
            }
            itemView.reservationDateTextView.text = reservation.date.toHour()
            itemView.maxCapacityTextView.text = itemView.context.getString(
                R.string.admin_reservation_max_amount_value_text,
                reservation.maxCapacity
            )
            itemView.availableSpacesTextView.text = itemView.context.getString(
                R.string.admin_reservation_available_value_text,
                (reservation.maxCapacity - reservation.spaces.size)
            )
            var participantsText = ""
            for (participant in reservation.spaces) {
                participantsText += String.format("%s\n", participant.fullName)
            }
            itemView.participantsTextView.text = participantsText
        }
    }
}