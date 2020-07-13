package com.loguito.brainmove.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.jakewharton.rxbinding3.view.clicks
import com.loguito.brainmove.R
import com.loguito.brainmove.ext.hideLoadingSpinner
import com.loguito.brainmove.ext.showDialog
import com.loguito.brainmove.ext.showFileChooser
import com.loguito.brainmove.ext.showLoadingSpinner
import com.loguito.brainmove.models.local.MeasurementOption
import com.loguito.brainmove.models.remote.Measure
import com.loguito.brainmove.models.remote.User
import com.loguito.brainmove.utils.Constants
import com.loguito.brainmove.utils.Constants.Companion.USER_KEY
import com.loguito.brainmove.viewmodels.UserDetailsMeasuresViewModel
import kotlinx.android.synthetic.main.fragment_user_detail_measures.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class UserDetailsMeasuresFragment : Fragment() {
    private lateinit var viewModel: UserDetailsMeasuresViewModel
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(UserDetailsMeasuresViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_detail_measures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.takeIf { it.containsKey(USER_KEY) }?.apply {
            getParcelable<User>(USER_KEY)?.let {
                user = it
                viewModel.getUserMeasures(user.id)
            }
        }
        bindListener()
        observeData()

    }

    private fun observeData() {
        viewModel.measures.observe(viewLifecycleOwner) {
            measuresContainer.measure = it
        }

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
    }

    @SuppressLint("CheckResult")
    private fun bindListener() {
        addMeasureButton.clicks()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe { showFileChooser() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            data?.data?.let {
                val inpS = requireActivity().contentResolver.openInputStream(it)
                inpS?.let { inputStream ->
                    handleReceivedFile(csvReader().readAll(inputStream)[1])
                    inputStream.close()
                }
            }
        }
    }

    private fun handleReceivedFile(data: List<String>) {
        val measureModel = Measure()
        for ((index, measure) in data.withIndex()) {
            if (index != 0) {
                val option = MeasurementOption.fromInt(index - 1)
                val value = measure.replace(Regex("[^\\d.]"), "").toDouble()
                when (option) {
                    MeasurementOption.WEIGHT -> measureModel.weight = value
                    MeasurementOption.BMI -> measureModel.bmi = value
                    MeasurementOption.BODY_FAT -> measureModel.body_fat = value
                    MeasurementOption.FAT_FREE_BODY -> measureModel.fat_free_body = value
                    MeasurementOption.SUBCUTANEOUS_FAT -> measureModel.subcutaneous_fat = value
                    MeasurementOption.VISCERAL_FAT -> measureModel.visceral_fat = value
                    MeasurementOption.BODY_WATER -> measureModel.body_water = value
                    MeasurementOption.SKELETAL_MUSCLE -> measureModel.skeletal_muscle = value
                    MeasurementOption.MUSCLE_MASS -> measureModel.muscle_mass = value
                    MeasurementOption.BONE_MASS -> measureModel.bone_mass = value
                    MeasurementOption.PROTEIN -> measureModel.protein = value
                    MeasurementOption.BMR -> measureModel.bmr = value
                    MeasurementOption.METABOLICAL_AGE -> measureModel.metabolical_age = value
                }
            } else {
                val format = SimpleDateFormat("MMM d, yyyy HH:mm:ss a", Locale.getDefault())
                measureModel.date = format.parse(measure) ?: Date()
            }
        }
        val action =
            UserDetailFragmentDirections.actionUserDetailFragmentToReviewUserMeasureFragment(
                user,
                measureModel,
                false
            )
        findNavController().navigate(action)
    }
}