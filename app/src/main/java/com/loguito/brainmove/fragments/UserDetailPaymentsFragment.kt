package com.loguito.brainmove.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.view.clicks
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.PaymentsAdapter
import com.loguito.brainmove.adapters.decorator.VerticalSpaceItemDecoration
import com.loguito.brainmove.ext.hideLoadingSpinner
import com.loguito.brainmove.ext.showDialog
import com.loguito.brainmove.ext.showFileChooser
import com.loguito.brainmove.ext.showLoadingSpinner
import com.loguito.brainmove.models.remote.User
import com.loguito.brainmove.utils.Constants
import com.loguito.brainmove.viewmodels.UserDetailPaymentViewModel
import kotlinx.android.synthetic.main.fragment_user_detail_payments.*
import java.util.concurrent.TimeUnit

class UserDetailPaymentsFragment : Fragment() {
    private val adapter = PaymentsAdapter()
    private lateinit var viewModel: UserDetailPaymentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(UserDetailPaymentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_detail_payments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        bindListener()
        observeChanges()
        arguments?.takeIf { it.containsKey(Constants.USER_KEY) }?.apply {
            getParcelable<User>(Constants.USER_KEY)?.let {
                viewModel.getPaymentsByUserId(it.id)
            }
        }
    }

    private fun initRecyclerView() {
        paymentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        paymentsRecyclerView.adapter = adapter
        paymentsRecyclerView.addItemDecoration(
            VerticalSpaceItemDecoration(
                20
            )
        )
    }

    private fun observeChanges() {
        viewModel.payments.observe(viewLifecycleOwner) {
            adapter.payments = it
            paymentsRecyclerView.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            emptyContainer.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
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

    @SuppressLint("CheckResult")
    private fun bindListener() {
        addPaymentButton.clicks()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                arguments?.takeIf { it.containsKey(Constants.USER_KEY) }?.apply {
                    getParcelable<User>(Constants.USER_KEY)?.let {
                        findNavController()
                            .navigate(UserDetailFragmentDirections.actionUserDetailFragmentToCreatePaymentFragment(it.id))
                    }
                }
            }
    }
}