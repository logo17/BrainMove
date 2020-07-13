package com.loguito.brainmove.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jakewharton.rxbinding3.view.clicks
import com.loguito.brainmove.R
import com.loguito.brainmove.ext.hideLoadingSpinner
import com.loguito.brainmove.ext.showDialog
import com.loguito.brainmove.ext.showLoadingSpinner
import com.loguito.brainmove.models.remote.Measure
import com.loguito.brainmove.models.remote.User
import com.loguito.brainmove.utils.Constants.Companion.THROTTLE_FIRST_DURATION
import com.loguito.brainmove.viewmodels.ReviewUserMeasureViewModel
import com.loguito.brainmove.widgets.OnDialogButtonClicked
import kotlinx.android.synthetic.main.fragment_review_user_measure.*
import java.util.concurrent.TimeUnit

class ReviewUserMeasureFragment : Fragment() {
    private val args: ReviewUserMeasureFragmentArgs by navArgs()
    private lateinit var user: User
    private lateinit var measure: Measure
    private lateinit var viewModel: ReviewUserMeasureViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReviewUserMeasureViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_review_user_measure, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = args.user
        measure = args.measurement.copy()
        measure.user_id = user.id
        initViews()
        observeChanges()
        bindListener()
    }

    private fun initViews() {
        nameTextView.text = user.fullName
        measuresContainer.measure = measure
    }

    private fun observeChanges() {
        viewModel.loadingVisibility.observe(
            viewLifecycleOwner,
            Observer {
                if (it) {
                    requireActivity().showLoadingSpinner(getString(R.string.saving_measures_message))
                } else {
                    requireActivity().hideLoadingSpinner()
                }
            })
        viewModel.measureCreatedError.observe(viewLifecycleOwner, Observer {
            showDialog(getString(R.string.save_measure_error), R.string.accept_button_text)
        })
        viewModel.measureCreated.observe(viewLifecycleOwner, Observer {
            showDialog(
                getString(R.string.save_measure_success),
                R.string.accept_button_text,
                listener = object :
                    OnDialogButtonClicked {
                    override fun onPositiveButtonClicked() {
                        val navOption =
                            NavOptions.Builder().setPopUpTo(R.id.reviewUserMeasureFragment, true)
                                .build()
                        if (args.navigateToRoot) {
                            findNavController()
                                .navigate(
                                    AddMeasureUserSelectionFragmentDirections.actionGlobalAdminMainFragment3(),
                                    navOption
                                )
                        } else {
                            findNavController().popBackStack()
                        }
                    }

                    override fun onNegativeButtonClicked() {}
                })
        })
    }

    @SuppressLint("CheckResult")
    private fun bindListener() {
        saveButton.clicks()
            .throttleFirst(THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe { viewModel.saveMeasure(measure) }

        cancelButton.clicks()
            .throttleFirst(THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe { findNavController().popBackStack() }
    }
}