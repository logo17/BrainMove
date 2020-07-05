package com.loguito.brainmove.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.AdminRoutineAdapter
import com.loguito.brainmove.ext.*
import com.loguito.brainmove.utils.Constants
import com.loguito.brainmove.viewmodels.CreatePlanViewModel
import com.loguito.brainmove.widgets.OnDialogButtonClicked
import kotlinx.android.synthetic.main.fragment_create_plan.*
import java.util.*
import java.util.concurrent.TimeUnit

class CreatePlanFragment : Fragment() {
    private val args: CreatePlanFragmentArgs by navArgs()

    val viewModel: CreatePlanViewModel by navGraphViewModels(R.id.create_plan_navigation)

    val adapter = AdminRoutineAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
        bindListeners()
    }

    private fun observeData() {
        viewModel.routines.observe(viewLifecycleOwner, Observer {
            routinesEmptyListWidget.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            routinesRecyclerView.visibility = if (it.isEmpty().not()) View.VISIBLE else View.GONE
            adapter.routines = it
        })

        viewModel.areValidFields.observe(viewLifecycleOwner, Observer {
            toolbar.menu.findItem(R.id.action_save).isVisible = it
        })

        viewModel.loadingVisibility.observe(
            viewLifecycleOwner,
            Observer {
                if (it) {
                    requireActivity().showLoadingSpinner(getString(R.string.creating_plan_loading_message))
                } else {
                    requireActivity().hideLoadingSpinner()
                }
            })

        viewModel.savePlanResponse.observe(viewLifecycleOwner, Observer {
            if (it) {
                showDialog(
                    getString(R.string.save_plan_success_text),
                    R.string.accept_button_text,
                    listener = object :
                        OnDialogButtonClicked {
                        override fun onPositiveButtonClicked() {
                            navigateBack()
                        }

                        override fun onNegativeButtonClicked() {}
                    })
            } else {
                showDialog(getString(R.string.save_plan_failure_text), R.string.accept_button_text)
            }
        })

        viewModel.dateRangeAsString.observe(
            viewLifecycleOwner,
            Observer { dateValueTextView.text = it })
    }

    private fun initViews() {
        routinesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        routinesRecyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        routinesRecyclerView.adapter = adapter

        addRoutineButton.setOnClickListener {
            val action =
                CreatePlanFragmentDirections.actionCreatePlanFragmentToCreateRoutineNavigation()
            findNavController().navigate(action)
        }
        toolbar.setNavigationOnClickListener {
            navigateBack()
        }
        toolbar.inflateMenu(R.menu.toolbar_save_menu)
        toolbar.menu.findItem(R.id.action_save).isVisible = false
        toolbar.setOnMenuItemClickListener {
            viewModel.savePlan(args.userId)
            true
        }
    }

    @SuppressLint("CheckResult")
    private fun bindListeners() {
        planNameEditText.textChanges()
            .skipInitialValue()
            .debounce(Constants.DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe { viewModel.validatePlanName(it.toString()) }

        planGoalEditText.textChanges()
            .skipInitialValue()
            .debounce(Constants.DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                viewModel.validatePlanGoal(it.toString())
            }

        dateContainer.clicks()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                val dateRangePicker = buildDateRangePicker()
                dateRangePicker.show(parentFragmentManager, dateRangePicker.toString())
                dateRangePicker.addOnPositiveButtonClickListener {
                    val fromDate = Date(it.first ?: 0)
                    val toDate = Date(it.second ?: 0)
                    val fromDateFormattedDate =
                        fromDate.toShortDateFromPicker().shorDateStringToDate()
                    val toDateFormattedDate = toDate.toShortDateFromPicker().shorDateStringToDate()
                    viewModel.validateDates(fromDateFormattedDate, toDateFormattedDate)
                }
            }
    }
}