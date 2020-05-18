package com.loguito.brainmove.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.TrendOptionsAdapter
import com.loguito.brainmove.ext.*
import com.loguito.brainmove.models.local.MeasurementOption
import com.loguito.brainmove.models.local.TrendOption
import com.loguito.brainmove.models.remote.Measure
import com.loguito.brainmove.models.remote.TrendMeasure
import com.loguito.brainmove.viewmodels.TrendsViewModel
import kotlinx.android.synthetic.main.fragment_trends.*


class TrendsFragment : Fragment() {
    private lateinit var viewModel: TrendsViewModel
    private lateinit var measures: List<Measure>
    private val adapter = TrendOptionsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TrendsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOptionList()
        observeChanges()
    }

    private fun observeChanges() {
        viewModel.measuresError.observe(viewLifecycleOwner) {
            showDialog(getString(it), R.string.accept_button_text)
        }

        viewModel.loadingVisibility.observe(viewLifecycleOwner) {
            if (it) {
                requireActivity().showLoadingSpinner(getString(R.string.loading_measures_message))
            } else {
                requireActivity().hideLoadingSpinner()
            }
        }

        viewModel.measures.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                emptyContainer.visibility = View.VISIBLE
                dataContainer.visibility = View.GONE
            } else {
                emptyContainer.visibility = View.GONE
                dataContainer.visibility = View.VISIBLE
                measures = it
                fillMeasures(MeasurementOption.fromInt(0))
            }
        }

        adapter.selectedOption.observe(viewLifecycleOwner) {
            fillMeasures(MeasurementOption.fromInt(it))
        }
    }

    private fun fillMeasures(option: MeasurementOption) {
        val kgSuffix = requireContext().getString(R.string.kg_suffix)
        val percentageSuffix = requireContext().getString(R.string.percentage_suffix)
        val kcalSuffix = requireContext().getString(R.string.kcal_suffix)
        val list = measures.map {
            when (option) {
                MeasurementOption.WEIGHT -> TrendMeasure(
                    it.weight,
                    it.date.toShortDate(),
                    kgSuffix
                )
                MeasurementOption.BMI -> TrendMeasure(
                    it.bmi,
                    it.date.toShortDate(),
                    ""
                )
                MeasurementOption.BODY_FAT -> TrendMeasure(
                    it.body_fat,
                    it.date.toShortDate(),
                    percentageSuffix
                )
                MeasurementOption.FAT_FREE_BODY -> TrendMeasure(
                    it.fat_free_body,
                    it.date.toShortDate(),
                    kgSuffix
                )
                MeasurementOption.SUBCUTANEOUS_FAT -> TrendMeasure(
                    it.subcutaneous_fat,
                    it.date.toShortDate(),
                    percentageSuffix
                )
                MeasurementOption.VISCERAL_FAT -> TrendMeasure(
                    it.visceral_fat,
                    it.date.toShortDate(),
                    ""
                )
                MeasurementOption.BODY_WATER -> TrendMeasure(
                    it.body_water,
                    it.date.toShortDate(),
                    percentageSuffix
                )
                MeasurementOption.SKELETAL_MUSCLE -> TrendMeasure(
                    it.skeletal_muscle,
                    it.date.toShortDate(),
                    percentageSuffix
                )
                MeasurementOption.MUSCLE_MASS -> TrendMeasure(
                    it.muscle_mass,
                    it.date.toShortDate(),
                    kgSuffix
                )
                MeasurementOption.BONE_MASS -> TrendMeasure(
                    it.bone_mass,
                    it.date.toShortDate(),
                    kgSuffix
                )
                MeasurementOption.PROTEIN -> TrendMeasure(
                    it.protein,
                    it.date.toShortDate(),
                    percentageSuffix
                )
                MeasurementOption.BMR -> TrendMeasure(
                    it.bmr,
                    it.date.toShortDate(),
                    kcalSuffix
                )
                MeasurementOption.METABOLICAL_AGE -> TrendMeasure(
                    it.metabolical_age,
                    it.date.toShortDate(),
                    ""
                )
            }
        }
        weightChart.trendMeasures = list
        weightChart.valueFormatter = option.toValueFormatter()
        weightChart.trendTitle = getString(option.toTitle())
    }

    private fun initOptionList() {
        val optionList = arrayListOf(
            TrendOption(
                R.drawable.ic_weight_selector,
                requireContext().getString(R.string.weight_label_text)
            ),
            TrendOption(
                R.drawable.ic_bmi_selector,
                requireContext().getString(R.string.bmi_label_text)
            ),
            TrendOption(
                R.drawable.ic_body_fat_selector,
                requireContext().getString(R.string.body_fat_label_text)
            ),
            TrendOption(
                R.drawable.ic_fat_free_body_selector,
                requireContext().getString(R.string.fat_free_body_weight_label_text)
            ),
            TrendOption(
                R.drawable.ic_subcutaneous_fat_selector,
                requireContext().getString(R.string.subcutaneous_fat_label_text)
            ),
            TrendOption(
                R.drawable.ic_visceral_fat_selector,
                requireContext().getString(R.string.visceral_fat_label_text)
            ),
            TrendOption(
                R.drawable.ic_body_water_selector,
                requireContext().getString(R.string.body_water_label_text)
            ),
            TrendOption(
                R.drawable.ic_skeletal_muscle_selector,
                requireContext().getString(R.string.skeletal_muscle_label_text)
            ),
            TrendOption(
                R.drawable.ic_muscle_mass_selector,
                requireContext().getString(R.string.muscle_mass_label_text)
            ),
            TrendOption(
                R.drawable.ic_bone_mass_selector,
                requireContext().getString(R.string.bone_mass_label_text)
            ),
            TrendOption(
                R.drawable.ic_protein_selector,
                requireContext().getString(R.string.protein_label_text)
            ),
            TrendOption(
                R.drawable.ic_bmr_selector,
                requireContext().getString(R.string.bmr_label_text)
            ),
            TrendOption(
                R.drawable.ic_metabolical_age_selector,
                requireContext().getString(R.string.metabolic_age_label_text)
            )
        )
        trendsOptionsRecyclerView.adapter = adapter
        trendsOptionsRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), HORIZONTAL, false)
        adapter.options = optionList
    }
}
