package com.loguito.brainmove.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.PlanAdapter
import com.loguito.brainmove.adapters.decorator.VerticalSpaceItemDecoration
import com.loguito.brainmove.ext.*
import com.loguito.brainmove.models.remote.Plan
import com.loguito.brainmove.viewmodels.PlanViewModel
import kotlinx.android.synthetic.main.fragment_plan.*
import kotlinx.android.synthetic.main.widget_measurement_list.view.*

class PlanFragment : Fragment() {
    private lateinit var viewModel: PlanViewModel
    private val adapter = PlanAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(PlanViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        observeData()
    }

    private fun observeData() {
        viewModel.loadingVisibility.observe(
            viewLifecycleOwner,
            Observer {
                if (it) {
                    requireActivity().showLoadingSpinner(getString(R.string.loading_plans_message))
                } else {
                    requireActivity().hideLoadingSpinner()
                }
            })

        viewModel.plan.observe(viewLifecycleOwner,
            Observer {
                dataContainer.visibility = if (it == null) View.GONE else View.VISIBLE
                emptyPlanView.visibility = if (it == null) View.VISIBLE else View.GONE
                if (it == null) {
                    planNameTextView.isEnabled = false
                    planNameTextView.text = getString(R.string.no_plans_text)
                } else {
                    planNameTextView.isEnabled = true
                    planNameTextView.isSelected = true
                    fillPlanData(it)
                }
            })

        viewModel.planError.observe(viewLifecycleOwner,
            Observer {
                showDialog(getString(it), R.string.accept_button_text)
            })

        adapter.selectedRoutine.observe(viewLifecycleOwner, Observer {
            val action = PlanFragmentDirections.actionPlanFragmentToRoutineFragment(it)
            planNameTextView.findNavController().navigate(action)
        })
    }

    private fun initRecyclerView() {
        planRecyclerView.addItemDecoration(VerticalSpaceItemDecoration(40))
        planRecyclerView.adapter = adapter
        planRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun fillPlanData(plan: Plan) {
        imageView2.visibility = View.VISIBLE
        planNameTextView.text = plan.name
        planDateTextView.text =
            String.format(getString(R.string.to_date_label_text), plan.toDate.toReadableDate())
        planDateTextView.visibility = View.VISIBLE
        adapter.routines = plan.routines
    }
}