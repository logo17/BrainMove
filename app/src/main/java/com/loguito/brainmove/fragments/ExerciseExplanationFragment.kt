package com.loguito.brainmove.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.ExerciseExplanationAdapter
import com.loguito.brainmove.adapters.decorator.DividerItemDecorator
import com.loguito.brainmove.ext.navigateBack
import kotlinx.android.synthetic.main.fragment_exercise_explanation.*

class ExerciseExplanationFragment : Fragment() {
    private val adapter = ExerciseExplanationAdapter()
    private val args: ExerciseExplanationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exercise_explanation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(this).load(args.exercise.demoUrl).into(exerciseExplanationImageView)
        toolbar.title = args.exercise.name
        toolbar.setNavigationOnClickListener {
            navigateBack()
        }
        ContextCompat.getDrawable(requireContext(), R.drawable.splitted_line_border)?.let {
            explanationsRecyclerView.addItemDecoration(DividerItemDecorator(it))
        }
        explanationsRecyclerView.adapter = adapter
        explanationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.explanations = args.exercise.explanations
    }
}