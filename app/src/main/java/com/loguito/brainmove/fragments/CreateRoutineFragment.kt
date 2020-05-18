package com.loguito.brainmove.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.widget.textChanges
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.AdminBlockAdapter
import com.loguito.brainmove.ext.navigateBack
import com.loguito.brainmove.utils.Constants
import com.loguito.brainmove.viewmodels.CreatePlanViewModel
import com.loguito.brainmove.viewmodels.CreateRoutineViewModel
import kotlinx.android.synthetic.main.fragment_create_routine.*
import java.util.concurrent.TimeUnit

class CreateRoutineFragment : Fragment() {
    private val adapter = AdminBlockAdapter()
    val viewModel: CreateRoutineViewModel by navGraphViewModels(R.id.create_routine_navigation)
    val sharedViewModel: CreatePlanViewModel by navGraphViewModels(R.id.create_plan_navigation)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_routine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeData()
        bindListeners()
    }

    private fun observeData() {
        viewModel.blocks.observe(viewLifecycleOwner, Observer {
            blocksEmptyListWidget.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            blocksRecyclerView.visibility = if (it.isEmpty().not()) View.VISIBLE else View.GONE
            adapter.blocks = it
        })

        viewModel.areValidFields.observe(viewLifecycleOwner, Observer {
            toolbar.menu.findItem(R.id.action_save).isVisible = it
        })

        viewModel.routine.observe(viewLifecycleOwner, Observer {
            sharedViewModel.addRoutineToList(it)
            navigateBack()
        })
    }

    private fun initViews() {
        blocksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        blocksRecyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        blocksRecyclerView.adapter = adapter

        addBlockButton.setOnClickListener {
            val action =
                CreateRoutineFragmentDirections.actionCreateRoutineFragmentToCreateBlockFragment()
            findNavController().navigate(action)
        }
        toolbar.setNavigationOnClickListener {
            navigateBack()
        }
        toolbar.inflateMenu(R.menu.toolbar_save_menu)
        toolbar.menu.findItem(R.id.action_save).isVisible = false
        toolbar.setOnMenuItemClickListener {
            viewModel.createRoutineObject()
            true
        }
    }

    @SuppressLint("CheckResult")
    private fun bindListeners() {
        routineNameEditText.textChanges()
            .skipInitialValue()
            .debounce(Constants.DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe { viewModel.validateRoutineName(it.toString()) }

        routineNumberEditText.textChanges()
            .skipInitialValue()
            .debounce(Constants.DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                viewModel.validateRoutineNumber(
                    if (it.isEmpty()) 0 else it.toString().toInt()
                )
            }
    }
}