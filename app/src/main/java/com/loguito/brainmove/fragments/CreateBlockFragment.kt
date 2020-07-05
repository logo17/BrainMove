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
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.widget.itemSelections
import com.jakewharton.rxbinding3.widget.textChanges
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.AdminExerciseAdapter
import com.loguito.brainmove.adapters.AdminSelectedExerciseAdapter
import com.loguito.brainmove.adapters.decorator.DividerItemDecorator
import com.loguito.brainmove.ext.hideLoadingSpinner
import com.loguito.brainmove.ext.navigateBack
import com.loguito.brainmove.ext.showDialog
import com.loguito.brainmove.ext.showLoadingSpinner
import com.loguito.brainmove.utils.Constants
import com.loguito.brainmove.viewmodels.CreateBlockViewModel
import com.loguito.brainmove.viewmodels.CreateRoutineViewModel
import kotlinx.android.synthetic.main.fragment_create_block.*
import kotlinx.android.synthetic.main.image_loader_widget.view.*
import java.util.concurrent.TimeUnit

class CreateBlockFragment : Fragment() {
    val sharedViewModel: CreateRoutineViewModel by navGraphViewModels(R.id.create_routine_navigation)
    private lateinit var viewModel: CreateBlockViewModel
    private val adapter = AdminExerciseAdapter()
    private val selectedExercisesAdapter = AdminSelectedExerciseAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(CreateBlockViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_create_block,
            container,
            false
        )
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
            viewModel.createBlockObject()
            true
        }
        toolbar.inflateMenu(R.menu.toolbar_save_menu)
        toolbar.menu.findItem(R.id.action_save).isVisible = false
        searchedExerciseRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        ContextCompat.getDrawable(requireContext(), R.drawable.divider_item_bg)?.let {
            searchedExerciseRecyclerView.addItemDecoration(DividerItemDecorator(it))
        }
        searchedExerciseRecyclerView.adapter = adapter
        selectedExercisesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        ContextCompat.getDrawable(requireContext(), R.drawable.divider_item_bg)?.let {
            selectedExercisesRecyclerView.addItemDecoration(DividerItemDecorator(it))
        }
        selectedExercisesRecyclerView.adapter = selectedExercisesAdapter
    }

    private fun observeData() {
        viewModel.exercises.observe(viewLifecycleOwner, Observer { adapter.exercises = it })

        viewModel.loadingVisibility.observe(viewLifecycleOwner, Observer {
            if (it) {
                requireActivity().showLoadingSpinner(getString(R.string.loading_exercises_message))
            } else {
                requireActivity().hideLoadingSpinner()
            }
        })

        viewModel.exerciseError.observe(viewLifecycleOwner, Observer {
            showDialog(getString(it), R.string.accept_button_text)
        })

        viewModel.areValidFields.observe(viewLifecycleOwner, Observer {
            toolbar.menu.findItem(R.id.action_save).isVisible = it
        })

        adapter.selectedOption.observe(
            viewLifecycleOwner,
            Observer { viewModel.addExerciseToBlock(it) })

        viewModel.selectedExercises.observe(
            viewLifecycleOwner,
            Observer {
                selectExerciseTextView.text =
                    if (it.isEmpty()) getString(R.string.select_exercise_text) else requireContext().resources.getQuantityString(
                        R.plurals.numberOfExercises,
                        it.size,
                        it.size
                    )
                selectedExercisesAdapter.exercises = it
            })

        selectedExercisesAdapter.updatedExercise.observe(viewLifecycleOwner, Observer {
            viewModel.updateExercise(it.second, it.first)
        })

        selectedExercisesAdapter.removeExercise.observe(viewLifecycleOwner, Observer {
            viewModel.removeExerciseFromBlock(it)
        })

        viewModel.block.observe(viewLifecycleOwner, Observer {
            sharedViewModel.addBlockToList(it)
            navigateBack()
        })

        viewModel.blockUnitMeasures.observe(viewLifecycleOwner, Observer {
            val adapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, it)
            unitMeasureDropdown.adapter = adapter
        })

        viewModel.workoutUnitMeasures.observe(viewLifecycleOwner, Observer {
            selectedExercisesAdapter.unitMeasures = it
        })
    }

    @SuppressLint("CheckResult")
    private fun bindListeners() {
        searchEditText.textChanges()
            .skipInitialValue()
            .debounce(Constants.DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe { viewModel.getExerciseByKeyword(it.toString()) }

        blockNameEditText.textChanges()
            .skipInitialValue()
            .debounce(Constants.DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe { viewModel.validateBlockName(it.toString()) }

        blockDescriptionEditText.textChanges()
            .skipInitialValue()
            .debounce(Constants.DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe { viewModel.validateBlockDescription(it.toString()) }

        blockDurationEditText.textChanges()
            .skipInitialValue()
            .debounce(Constants.DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe { viewModel.validateBlockDuration(it.toString()) }

        unitMeasureDropdown.itemSelections()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .filter{unitMeasureDropdown.adapter != null}
            .subscribe {
                viewModel.validateBlockUnitMeasure(unitMeasureDropdown.adapter.getItem(it).toString())
            }

        blockBackgroundImageLoader.imageUrlEditText.textChanges()
            .skipInitialValue()
            .debounce(Constants.DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe { viewModel.validateBlockBackgroundImageUrl(it.toString()) }
    }
}