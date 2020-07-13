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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding3.view.clicks
import com.loguito.brainmove.R
import com.loguito.brainmove.utils.Constants
import com.loguito.brainmove.viewmodels.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.concurrent.TimeUnit

class SettingsFragment : Fragment() {
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeChanges()
        bindListeners()
    }

    private fun observeChanges() {
        viewModel.isLoggedOutUser.observe(viewLifecycleOwner, Observer {
            val navOption = NavOptions.Builder().setPopUpTo(R.id.mainFragment, true).build()
            requireActivity().findNavController(R.id.fragment)
                .navigate(MainFragmentDirections.actionGlobalLauncherFragment(), navOption)
        })

        viewModel.userName.observe(viewLifecycleOwner, Observer { nameEditText.setText(it) })

        viewModel.email.observe(viewLifecycleOwner, Observer { emailEditText.setText(it) })
    }

    @SuppressLint("CheckResult")
    private fun bindListeners() {
        logoutButton.clicks()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe { viewModel.logoutUser() }

        paymentsButton.clicks()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                findNavController()
                    .navigate(SettingsFragmentDirections.actionSettingsFragmentToPaymentsFragment())
            }
    }
}