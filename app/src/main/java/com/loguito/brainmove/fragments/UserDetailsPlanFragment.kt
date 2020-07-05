package com.loguito.brainmove.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.view.clicks
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.AdminRoutineAdapter
import com.loguito.brainmove.adapters.decorator.VerticalSpaceItemDecoration
import com.loguito.brainmove.ext.hideLoadingSpinner
import com.loguito.brainmove.ext.showDialog
import com.loguito.brainmove.ext.showLoadingSpinner
import com.loguito.brainmove.ext.toReadableDate
import com.loguito.brainmove.models.remote.Plan
import com.loguito.brainmove.models.remote.User
import com.loguito.brainmove.utils.Constants
import com.loguito.brainmove.viewmodels.UserDetailsPlanViewModel
import kotlinx.android.synthetic.main.fragment_plan.*
import java.util.concurrent.TimeUnit

class UserDetailsPlanFragment : Fragment() {
    private lateinit var viewModel: UserDetailsPlanViewModel
    private lateinit var userId: String
    val adapter = AdminRoutineAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(UserDetailsPlanViewModel::class.java)
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
        bindListener()
        arguments?.takeIf { it.containsKey(Constants.USER_KEY) }?.apply {
            getParcelable<User>(Constants.USER_KEY)?.let {
                userId = it.id
                viewModel.getUserPlan(userId)
            }
        }
    }

    private fun initRecyclerView() {
        planRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        planRecyclerView.adapter = adapter
        planRecyclerView.addItemDecoration(
            VerticalSpaceItemDecoration(
                20
            )
        )
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
                if (it == null) {
                    planNameTextView.text = getString(R.string.no_plans_text)
                } else {
                    fillPlanData(it)
                }
            })

        viewModel.planError.observe(viewLifecycleOwner,
            Observer {
                showDialog(getString(it), R.string.accept_button_text)
            })
    }

    private fun fillPlanData(plan: Plan) {
        planNameTextView.text = plan.name
        planNameTextView.isSelected = true
        planDateTextView.text =
            String.format(getString(R.string.to_date_label_text), plan.toDate.toReadableDate())
        adapter.routines = plan.routines
    }

    @SuppressLint("CheckResult")
    private fun bindListener() {
        addPlanButton.clicks()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                findNavController().navigate(
                    UserDetailFragmentDirections.actionUserDetailFragmentToCreatePlanNavigation(
                        userId
                    )
                )
            }
    }
}