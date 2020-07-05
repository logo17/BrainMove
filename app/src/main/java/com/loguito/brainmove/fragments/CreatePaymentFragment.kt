package com.loguito.brainmove.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.loguito.brainmove.R
import com.loguito.brainmove.ext.*
import com.loguito.brainmove.utils.Constants
import com.loguito.brainmove.viewmodels.CreatePaymentViewModel
import com.loguito.brainmove.widgets.OnDialogButtonClicked
import kotlinx.android.synthetic.main.fragment_admin_reservation_list.*
import kotlinx.android.synthetic.main.fragment_create_payment.*
import java.util.*
import java.util.concurrent.TimeUnit

class CreatePaymentFragment : Fragment() {
    private lateinit var viewModel: CreatePaymentViewModel
    private val args: CreatePaymentFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(CreatePaymentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_payment, container, false)
    }

    private fun initViews() {
        toolbar.setNavigationOnClickListener {
            navigateBack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
        bindListeners()
    }

    private fun observeData() {
        viewModel.loadingVisibility.observe(
            viewLifecycleOwner,
            Observer {
                if (it) {
                    requireActivity().showLoadingSpinner(getString(R.string.creating_payment_loading_message))
                } else {
                    requireActivity().hideLoadingSpinner()
                }
            })

        viewModel.fromDate.observe(
            viewLifecycleOwner,
            Observer { paymentDateValueTextView.text = it })

        viewModel.toDate.observe(
            viewLifecycleOwner,
            Observer { dueDateValueTextView.text = it })

        viewModel.areValidFields.observe(viewLifecycleOwner, Observer {
            createPaymentButton.isEnabled = it
        })

        viewModel.paymentCreated.observe(viewLifecycleOwner, Observer {
            if (it) {
                showDialog(
                    getString(R.string.save_payment_success_text),
                    R.string.accept_button_text,
                    listener = object :
                        OnDialogButtonClicked {
                        override fun onPositiveButtonClicked() {
                            navigateBack()
                        }

                        override fun onNegativeButtonClicked() {}
                    })
            } else {
                showDialog(
                    getString(R.string.save_payment_failure_text),
                    R.string.accept_button_text
                )
            }
        })
    }

    @SuppressLint("CheckResult")
    private fun bindListeners() {
        paymentDescriptionEditText.textChanges()
            .skipInitialValue()
            .debounce(Constants.DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe { viewModel.validatePaymentDescription(it.toString()) }

        paymentTotalEditText.textChanges()
            .skipInitialValue()
            .debounce(Constants.DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                viewModel.validatePaymentTotal(
                    if (it.toString().isEmpty()) 0.0 else it.toString().toDouble()
                )
            }

        datesContainer.clicks()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                val dateRangePicker = buildDateRangePicker()
                dateRangePicker.show(parentFragmentManager, dateRangePicker.toString())
                dateRangePicker.addOnPositiveButtonClickListener {
                    val paymentDate = Date(it.first ?: 0)
                    val dueDate = Date(it.second ?: 0)
                    val paymentDateFormattedDate = paymentDate.toShortDateFromPicker().shorDateStringToDate()
                    val dueDateFormattedDate = dueDate.toShortDateFromPicker().shorDateStringToDate()
                    viewModel.validateDates(paymentDateFormattedDate, dueDateFormattedDate)
                }
            }

        createPaymentButton.clicks()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                viewModel.savePayment(args.userId)
            }
    }
}