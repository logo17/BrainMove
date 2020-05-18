package com.loguito.brainmove.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.loguito.brainmove.R
import com.loguito.brainmove.ext.toHour
import com.loguito.brainmove.models.local.Reservation
import com.loguito.brainmove.utils.SingleLiveEvent
import kotlinx.android.synthetic.main.reservation_cell_item.view.*

class ReservationsAdapter : RecyclerView.Adapter<ReservationsAdapter.ReservationViewHolder>() {

    var reservations: List<Reservation> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val _handleReservationClicked = SingleLiveEvent<Reservation>()
    val handleReservationClicked: LiveData<Reservation>
        get() = _handleReservationClicked

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.reservation_cell_item, parent, false)
        return ReservationViewHolder(view)
    }

    override fun getItemCount() = reservations.size

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        holder.bind(reservations[position])
    }

    inner class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(reservation: Reservation) {
            val hourText = reservation.date.toHour()
            itemView.reservationInfoTextView.text =
                if (reservation.availableSpaces > 0) String.format(
                    "%s - %d ESPACIOS",
                    hourText,
                    reservation.availableSpaces
                ) else hourText
            itemView.reservationMainButton.text = getButtonText(reservation)
            setCellState(reservation)
            itemView.reservationMainButton.setOnClickListener {
                _handleReservationClicked.postValue(reservation)
            }
        }

        private fun getButtonText(reservation: Reservation): String {
            return when {
                reservation.isReserved -> {
                    itemView.context.getString(R.string.reserved_text)
                }
                reservation.availableSpaces > 0 -> {
                    itemView.context.getString(R.string.available_text)
                }
                else -> {
                    itemView.context.getString(R.string.unavailable_text)
                }
            }
        }

        private fun setCellState(reservation: Reservation) {
            when {
                reservation.isReserved -> {
                    itemView.mainContainer.isSelected = true
                    itemView.reservationMainButton.isSelected = true
                }
                reservation.availableSpaces > 0 -> {
                    itemView.mainContainer.isSelected = false
                    itemView.reservationMainButton.isSelected = false
                }
                else -> {
                    itemView.mainContainer.isEnabled = false
                    itemView.reservationMainButton.isEnabled = false
                }
            }
        }
    }
}