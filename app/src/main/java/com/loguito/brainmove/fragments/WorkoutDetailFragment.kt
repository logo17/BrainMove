package com.loguito.brainmove.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.WorkoutDetailAdapter
import com.loguito.brainmove.adapters.decorator.VerticalSpaceItemDecoration
import com.loguito.brainmove.models.remote.Block
import kotlinx.android.synthetic.main.fragment_workout_detail.*


class WorkoutDetailFragment : Fragment() {
    private val args: WorkoutDetailFragmentArgs by navArgs()
    private val adapter = WorkoutDetailAdapter()

    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workout_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        val block = args.block
        initViews(block)
    }

    private fun initRecyclerView() {
        adapter.exerciseClicked.observe(viewLifecycleOwner, Observer {
            val action =
                WorkoutDetailFragmentDirections.actionWorkoutDetailFragmentToExerciseExplanationFragment(
                    it
                )
            findNavController().navigate(action)
        })
        workoutRecyclerView.addItemDecoration(VerticalSpaceItemDecoration(40))
        workoutRecyclerView.adapter = adapter
    }

    private fun initViews(block: Block) {
        adapter.exerciseList = block.exercises
        toolbar.title = block.name
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        blockSubtitle.text = block.unit
        blockSubtitleQuantity.text = block.duration.toString()
    }
}