package com.loguito.brainmove.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.loguito.brainmove.R
import com.loguito.brainmove.viewmodels.LauncherViewModel
import com.loguito.brainmove.viewmodels.SharedViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LauncherFragment : Fragment() {
    private lateinit var viewModel: LauncherViewModel
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel = ViewModelProvider(this).get(LauncherViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_launcher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeChanges()
        GlobalScope.launch {
            delay(2000L)
            viewModel.checkIfUserIsLoggedIn()
        }
    }

    private fun observeChanges() {
        val navOption = NavOptions.Builder().setPopUpTo(R.id.launcherFragment, true).build()
        viewModel.isLoggedUserAdmin.observe(viewLifecycleOwner) {
            val action: NavDirections
            action = if (it) {
                sharedViewModel.receivedMeasurement.value?.let { measure ->
                    LauncherFragmentDirections.actionLauncherFragmentToMeasureFromFileNavGraph(
                        measure
                    )
                } ?: LauncherFragmentDirections.actionLauncherFragmentToAdminMainFragment2()
            } else {
                LauncherFragmentDirections.actionLauncherFragmentToMainFragment()
            }
            findNavController().navigate(action, navOption)
        }

        viewModel.navigateToLogin.observe(viewLifecycleOwner) {
            if (it) {
                val action = LauncherFragmentDirections.actionLauncherFragmentToLoginFragment()
                findNavController().navigate(action, navOption)
            }
        }
    }
}