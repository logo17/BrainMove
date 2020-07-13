package com.loguito.brainmove.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.view.clicks
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.AdminReservationListAdapter
import com.loguito.brainmove.adapters.decorator.DividerItemDecorator
import com.loguito.brainmove.ext.*
import com.loguito.brainmove.utils.Constants
import com.loguito.brainmove.viewmodels.AdminReservationListViewModel
import com.loguito.brainmove.widgets.OnDialogButtonClicked
import kotlinx.android.synthetic.main.fragment_admin_reservation_list.*
import java.util.*
import java.util.concurrent.TimeUnit

class AdminReservationListFragment : Fragment() {
    private lateinit var viewModel: AdminReservationListViewModel
    private val adapter = AdminReservationListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminReservationListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_reservation_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        bindListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getReservationList()
    }

    private fun initViews() {
        dateValueTextView.text = viewModel.getDateText()
        reservationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        reservationsRecyclerView.adapter = adapter
        ContextCompat.getDrawable(requireContext(), R.drawable.divider_item_bg)?.let {
            reservationsRecyclerView.addItemDecoration(DividerItemDecorator(it))
        }
    }

    @SuppressLint("CheckResult")
    private fun bindListeners() {
        dateContainer.clicks()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                val dateRangePicker = buildDatePicker()
                dateRangePicker.show(parentFragmentManager, dateRangePicker.toString())
                dateRangePicker.addOnPositiveButtonClickListener {
                    val date = Date(it)
                    dateValueTextView.text = date.toReadableDateFromPicker()
                    val formattedDate = date.toShortDateFromPicker().shorDateStringToDate()
                    viewModel.updateDates(formattedDate)
                }
            }

        addSessionButton.clicks()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                findNavController().navigate(
                    //TODO: Should pass activity ID
                    AdminReservationListFragmentDirections.actionEventListFragmentToAdminCreateReservationFragment()
                )
            }

        viewModel.reservationList.observe(
            viewLifecycleOwner,
            Observer {
                reservationsRecyclerView.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                emptyContainer.visibility = if (it.isEmpty().not()) View.GONE else View.VISIBLE
                adapter.reservations = it
            })

        viewModel.loadingVisibility.observe(viewLifecycleOwner, Observer {
            if (it) {
                requireActivity().showLoadingSpinner(getString(R.string.loading_generic_text))
            } else {
                requireActivity().hideLoadingSpinner()
            }
        })

        viewModel.reservationListError.observe(viewLifecycleOwner, Observer {
            showDialog(getString(R.string.retrieve_reservations_error), R.string.accept_button_text)
        })

        viewModel.removeSessionSuccess.observe(viewLifecycleOwner, Observer {
            showDialog(
                getString(if (it) R.string.remove_session_success else R.string.remove_session_error),
                R.string.accept_button_text
            )
        })

        adapter.selectedOption.observe(viewLifecycleOwner, Observer {
            showDialog(
                getString(R.string.remove_session_alert),
                R.string.accept_button_text,
                listener = object :
                    OnDialogButtonClicked {
                    override fun onPositiveButtonClicked() {
                        viewModel.removeSession(it.id)
                    }

                    override fun onNegativeButtonClicked() {}
                })
        })
    }
}