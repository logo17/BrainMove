package com.loguito.brainmove.fragments

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.loguito.brainmove.R
import com.loguito.brainmove.ext.*
import com.loguito.brainmove.utils.Constants
import com.loguito.brainmove.viewmodels.AdminCreateReservationViewModel
import com.loguito.brainmove.widgets.OnDialogButtonClicked
import kotlinx.android.synthetic.main.fragment_admin_create_reservation.*
import java.util.*
import java.util.concurrent.TimeUnit

class AdminCreateReservationFragment : Fragment() {
    private lateinit var viewModel: AdminCreateReservationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(AdminCreateReservationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_create_reservation, container, false)
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
                    requireActivity().showLoadingSpinner(getString(R.string.creating_session_loading_message))
                } else {
                    requireActivity().hideLoadingSpinner()
                }
            })

        viewModel.areValidFields.observe(viewLifecycleOwner, Observer {
            createReservationButton.isEnabled = it
        })

        viewModel.sessionCreated.observe(viewLifecycleOwner, Observer {
            if (it) {
                showDialog(
                    getString(R.string.save_session_success_text),
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
                    getString(R.string.save_session_failure_text),
                    R.string.accept_button_text
                )
            }
        })
    }

    @SuppressLint("CheckResult")
    private fun bindListeners() {
        maxAmountEditText.textChanges()
            .skipInitialValue()
            .debounce(Constants.DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
            .map { text -> if (text.toString().isEmpty()) 0 else text.toString().toInt() }
            .subscribe { x -> viewModel.validateMaxCapacity(x) }

        dateContainer.clicks()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                val dateRangePicker = buildDatePicker()
                dateRangePicker.show(parentFragmentManager, dateRangePicker.toString())
                dateRangePicker.addOnPositiveButtonClickListener {
                    val date = Date(it)
                    viewModel.validateDate(date.toShortDateFromPicker())
                    dateValueTextView.text = date.toShortDateFromPicker()
                }
            }

        hourContainer.clicks()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                val cal = Calendar.getInstance()
                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hour)
                        cal.set(Calendar.MINUTE, minute)
                        hourValueTextView.text = cal.time.toHour()
                        viewModel.validateHour(cal.time.toHour())
                    }
                TimePickerDialog(
                    requireContext(),
                    timeSetListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    false
                ).show()
            }

        createReservationButton.clicks()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                viewModel.saveSession()
            }
    }
}