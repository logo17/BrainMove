package com.loguito.brainmove.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.RoutineAdapter
import com.loguito.brainmove.adapters.decorator.VerticalSpaceItemDecoration
import com.loguito.brainmove.models.remote.Routine
import kotlinx.android.synthetic.main.fragment_routine.*

class RoutineFragment : Fragment() {
    private val args: RoutineFragmentArgs by navArgs()
    private val adapter = RoutineAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_routine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val routine = args.routine
        initViews(routine)
        observeData()
    }

    private fun observeData() {
        adapter.selectedBlock.observe(viewLifecycleOwner, Observer {
            val action = RoutineFragmentDirections.actionRoutineFragmentToWorkoutDetailFragment(it)
            findNavController().navigate(action)
        })
    }

    private fun initViews(routine: Routine) {
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        toolbar.title = routine.name
        routineRecyclerView.addItemDecoration(VerticalSpaceItemDecoration(40))
        routineRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        routineRecyclerView.adapter = adapter
        adapter.blocks = routine.blocks
    }
}