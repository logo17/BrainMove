package com.loguito.brainmove.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.ReservationsAdapter
import com.loguito.brainmove.adapters.decorator.VerticalSpaceItemDecoration
import com.loguito.brainmove.ext.hideLoadingSpinner
import com.loguito.brainmove.ext.showDialog
import com.loguito.brainmove.ext.showLoadingSpinner
import com.loguito.brainmove.utils.Constants
import com.loguito.brainmove.viewmodels.ReservationListViewModel
import kotlinx.android.synthetic.main.fragment_reservation_list.*
import java.util.*


class ReservationListFragment : Fragment() {
    private lateinit var viewModel: ReservationListViewModel
    private val adapter = ReservationsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReservationListViewModel::class.java)
        getReservationList()
    }

    private fun getReservationList() {
        arguments?.takeIf { it.containsKey(Constants.DATE_KEY) }?.apply {
            getLong(Constants.DATE_KEY).let {
                val today = Date(it)
                val tomorrow = getEndDate(today)
                viewModel.getReservationList(today, tomorrow)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reservation_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel.reservationList.observe(viewLifecycleOwner, Observer {
            reservationRecyclerView.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            emptyContainer.visibility = if (it.isEmpty().not()) View.GONE else View.VISIBLE
            adapter.reservations = it
        })

        adapter.handleReservationClicked.observe(viewLifecycleOwner, Observer {
            if (it.isReserved) {
                viewModel.releaseReservation(it)
            } else {
                viewModel.makeReservation(it)
            }
        })

        viewModel.handleReservation.observe(viewLifecycleOwner, Observer {
            showDialog(getString(it), R.string.accept_button_text)
            getReservationList()
        })

        viewModel.reservationListError.observe(viewLifecycleOwner, Observer {
            showDialog(getString(it), R.string.accept_button_text)
        })

        viewModel.handleReservationError.observe(viewLifecycleOwner, Observer {
            showDialog(getString(it), R.string.accept_button_text)
            getReservationList()
        })

        viewModel.loadingVisibility.observe(viewLifecycleOwner) {
            if (it) {
                requireActivity().showLoadingSpinner(getString(R.string.loading_generic_text))
            } else {
                requireActivity().hideLoadingSpinner()
            }
        }
    }

    private fun initViews() {
        reservationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        reservationRecyclerView.addItemDecoration(VerticalSpaceItemDecoration(40))
        reservationRecyclerView.adapter = adapter
    }

    private fun getEndDate(todayDate: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = todayDate
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 59)
        return cal.time
    }
}