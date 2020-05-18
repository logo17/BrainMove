package com.loguito.brainmove.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.PaymentsAdapter
import com.loguito.brainmove.ext.hideLoadingSpinner
import com.loguito.brainmove.ext.showDialog
import com.loguito.brainmove.ext.showLoadingSpinner
import com.loguito.brainmove.viewmodels.PaymentsViewModel
import kotlinx.android.synthetic.main.fragment_payments.*

class PaymentsFragment : Fragment() {
    private val adapter = PaymentsAdapter()
    private lateinit var viewModel: PaymentsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PaymentsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeChanges()
    }

    private fun initViews() {
        paymentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        paymentsRecyclerView.adapter = adapter
    }

    private fun observeChanges() {

        viewModel.payments.observe(viewLifecycleOwner) {
            adapter.payments = it
        }

        viewModel.paymentError.observe(viewLifecycleOwner) {
            showDialog(getString(it), R.string.accept_button_text)
        }

        viewModel.loadingVisibility.observe(viewLifecycleOwner) {
            if (it) {
                requireActivity().showLoadingSpinner(
                    getString(R.string.loading_payments_message)
                )
            } else {
                requireActivity().hideLoadingSpinner()
            }
        }
    }
}