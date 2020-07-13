package com.loguito.brainmove.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loguito.brainmove.R
import com.loguito.brainmove.ext.toShortDate
import com.loguito.brainmove.models.remote.Payment
import kotlinx.android.synthetic.main.payment_item_cell.view.*
import java.text.DecimalFormat

class PaymentsAdapter : RecyclerView.Adapter<PaymentsAdapter.PaymentViewHolder>() {

    var payments: List<Payment> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.payment_item_cell, parent, false)
        return PaymentViewHolder(view)
    }

    override fun getItemCount() = payments.size

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        holder.bind(payments[position])
    }

    inner class PaymentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(payment: Payment) {
            val decimalFormatter = DecimalFormat("#,###.##")
            itemView.descriptionTextView.text = payment.description
            itemView.dateTextView.text = payment.paymentDate.toShortDate()
            itemView.totalTextView.text = "₡ ${decimalFormatter.format(payment.total)}"
        }
    }
}