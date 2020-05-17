package com.loguito.brainmove.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.TrendOptionsAdapter
import com.loguito.brainmove.models.local.TrendOption
import com.loguito.brainmove.models.remote.Measurement
import kotlinx.android.synthetic.main.fragment_trends.*


class TrendsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO: Remove this
        weightChart.measurements = arrayListOf(
            Measurement(78.8f, "01/01/2020"),
            Measurement(76.8f, "01/02/2020"),
            Measurement(75.0f, "01/03/2020"),
            Measurement(77.2f, "01/04/2020"),
            Measurement(73.2f, "01/05/2020"),
            Measurement(71.5f, "01/06/2020"),
            Measurement(71.2f, "01/07/2020")
        )
        weightChart.trendTitle = "Peso"
        val optionList = arrayListOf(
            TrendOption(
                R.drawable.ic_healing_black_24dp,
                requireContext().getString(R.string.weight_label_text)
            ),
            TrendOption(
                R.drawable.ic_healing_black_24dp,
                requireContext().getString(R.string.bmi_label_text)
            ),
            TrendOption(
                R.drawable.ic_healing_black_24dp,
                requireContext().getString(R.string.body_fat_label_text)
            ),
            TrendOption(
                R.drawable.ic_healing_black_24dp,
                requireContext().getString(R.string.fat_free_body_weight_label_text)
            ),
            TrendOption(
                R.drawable.ic_healing_black_24dp,
                requireContext().getString(R.string.subcutaneous_fat_label_text)
            ),
            TrendOption(
                R.drawable.ic_healing_black_24dp,
                requireContext().getString(R.string.visceral_fat_label_text)
            ),
            TrendOption(
                R.drawable.ic_healing_black_24dp,
                requireContext().getString(R.string.body_water_label_text)
            ),
            TrendOption(
                R.drawable.ic_healing_black_24dp,
                requireContext().getString(R.string.skeletal_muscle_label_text)
            ),
            TrendOption(
                R.drawable.ic_healing_black_24dp,
                requireContext().getString(R.string.muscle_mass_label_text)
            ),
            TrendOption(
                R.drawable.ic_healing_black_24dp,
                requireContext().getString(R.string.bone_mass_label_text)
            ),
            TrendOption(
                R.drawable.ic_healing_black_24dp,
                requireContext().getString(R.string.protein_label_text)
            ),
            TrendOption(
                R.drawable.ic_healing_black_24dp,
                requireContext().getString(R.string.bmr_label_text)
            ),
            TrendOption(
                R.drawable.ic_healing_black_24dp,
                requireContext().getString(R.string.metabolic_age_label_text)
            )
        )
        val adapter = TrendOptionsAdapter()
        trendsOptionsRecyclerView.adapter = adapter
        trendsOptionsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
        adapter.options = optionList
    }
}
