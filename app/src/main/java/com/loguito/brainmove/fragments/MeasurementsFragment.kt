package com.loguito.brainmove.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jakewharton.rxbinding3.view.clicks
import com.loguito.brainmove.R
import com.loguito.brainmove.ext.hideLoadingSpinner
import com.loguito.brainmove.ext.showDialog
import com.loguito.brainmove.ext.showImageChooser
import com.loguito.brainmove.ext.showLoadingSpinner
import com.loguito.brainmove.utils.Constants
import com.loguito.brainmove.utils.Constants.Companion.PICKIMAGE_RESULT_CODE
import com.loguito.brainmove.viewmodels.MeasurementsViewModel
import kotlinx.android.synthetic.main.fragment_measurements.*
import java.util.concurrent.TimeUnit


class MeasurementsFragment : Fragment() {
    private lateinit var viewModel: MeasurementsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MeasurementsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_measurements, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeChanges()
        bindListeners()
    }

    private fun observeChanges() {
        viewModel.userName.observe(viewLifecycleOwner) { nameTextView.text = it }

        viewModel.measures.observe(viewLifecycleOwner) { measuresContainer.measure = it }

        viewModel.measuresError.observe(viewLifecycleOwner) {
            showDialog(getString(it), R.string.accept_button_text)
        }

        viewModel.loadingVisibility.observe(viewLifecycleOwner) {
            if (it.first) {
                requireActivity().showLoadingSpinner(
                    getString(
                        it.second ?: R.string.loading_measures_message
                    )
                )
            } else {
                requireActivity().hideLoadingSpinner()
            }
        }

        viewModel.profileImageUrl.observe(viewLifecycleOwner) {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.ic_user_profile_image_placeholder)
                .apply(RequestOptions().circleCrop())
                .into(userImageView)
        }
    }

    @SuppressLint("CheckResult")
    private fun bindListeners() {
        settingsButton.clicks()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                val action =
                    MainFragmentDirections.actionMainFragmentToSettingsFragment()
                requireActivity().findNavController(R.id.fragment).navigate(action)
            }

        userImageView.clicks()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe { showImageChooser() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == PICKIMAGE_RESULT_CODE) {
            data?.data?.let {
                viewModel.uploadUserImage(it)
            }
        }
    }
}
