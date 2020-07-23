package com.loguito.brainmove.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.itemSelections
import com.jakewharton.rxbinding3.widget.textChanges
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.ExerciseExplanationAdapter
import com.loguito.brainmove.adapters.decorator.DividerItemDecorator
import com.loguito.brainmove.ext.navigateBack
import com.loguito.brainmove.utils.Constants
import com.loguito.brainmove.viewmodels.CreateBlockViewModel
import com.loguito.brainmove.viewmodels.CreateExerciseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_admin_create_exercise.*
import java.util.*
import java.util.concurrent.TimeUnit

class CreateExerciseFragment : Fragment() {

    private val args: CreateExerciseFragmentArgs by navArgs()
    private val adapter = ExerciseExplanationAdapter()

    private val sharedViewModel: CreateBlockViewModel by navGraphViewModels(R.id.create_exercise_navigation)
    private lateinit var viewModel: CreateExerciseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(CreateExerciseViewModel::class.java)
        args.exercise?.let {
            if (it.quantity.isNotEmpty()) {
                viewModel.setExerciseDurationInput(it.quantity.split(' ')[0])
                viewModel.setExerciseUnitMeasureInput(
                    it.quantity.split(' ')[1],
                    sharedViewModel.unitMeasures
                )
            }
            viewModel.addExplanationsToList(it.explanations)
            viewModel.setExerciseNameInput(it.name)
            viewModel.setExerciseBackgroundImageInput(it.backgroundImageUrl)
            viewModel.setExerciseDemoImageInput(it.demoUrl)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_create_exercise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeData()
        bindListeners()
    }

    private fun initViews() {
        toolbar.setNavigationOnClickListener {
            navigateBack()
        }
        toolbar.setOnMenuItemClickListener {
            args.exercise?.let {
                viewModel.setSaveButtonInput(it)
            }
            true
        }
        toolbar.inflateMenu(R.menu.toolbar_save_menu)
        toolbar.menu.findItem(R.id.action_save).isVisible = false

        ContextCompat.getDrawable(requireContext(), R.drawable.splitted_line_border)?.let {
            recyclerView.addItemDecoration(DividerItemDecorator(it))
        }
        adapter.deleteButtonVisibility = true
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item, sharedViewModel.unitMeasures
        )
        unitMeasureDropdown.adapter = adapter

    }

    private fun observeData() {
        viewModel.areValidFields.observe(viewLifecycleOwner, Observer {
            toolbar.menu.findItem(R.id.action_save).isVisible = it
        })

        viewModel.explanations.observe(viewLifecycleOwner, Observer {
            adapter.explanations = it
        })

        viewModel.demoImageUrlOutput.observe(viewLifecycleOwner, Observer {
            Glide.with(requireContext()).load(it)
                .placeholder(R.drawable.loading_background_placeholder)
                .into(demoImageView)
        })

        viewModel.exerciseNameOutput.observe(viewLifecycleOwner, Observer { toolbar.title = it })

        viewModel.exerciseDurationOutput.observe(
            viewLifecycleOwner,
            Observer { exerciseQuantityEditText.setText(it) })

        viewModel.exerciseUnitMeasureOutput.observe(
            viewLifecycleOwner,
            Observer { unitMeasureDropdown.setSelection(it) })

        adapter.deleteExplanation.observe(viewLifecycleOwner, Observer {
            viewModel.removeExplanationFromList(it)
        })

        viewModel.addExerciseToBlockOutput.observe(viewLifecycleOwner, Observer {
            if (args.listIndex == -1) {
                sharedViewModel.addExerciseToBlock(it)
            } else {
                sharedViewModel.updateExercise(it, args.listIndex)
            }
            navigateBack()
        })
    }

    @SuppressLint("CheckResult")
    private fun bindListeners() {
        additionalInfoEditText.textChanges()
            .skipInitialValue()
            .debounce(Constants.DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { addNoteButton.isEnabled = it.toString().isNotEmpty() }

        unitMeasureDropdown.itemSelections()
            .skipInitialValue()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                viewModel.exerciseUnitMeasure =
                    unitMeasureDropdown.selectedItem.toString().toLowerCase(Locale.getDefault())
            }

        exerciseQuantityEditText.textChanges()
            .skipInitialValue()
            .debounce(Constants.DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { viewModel.exerciseDuration = it.toString() }

        addNoteButton.clicks()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                viewModel.addExplanationToList(additionalInfoEditText.text.toString())
                additionalInfoEditText.setText("")
            }
    }
}