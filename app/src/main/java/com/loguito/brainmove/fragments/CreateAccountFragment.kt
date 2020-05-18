package com.loguito.brainmove.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.loguito.brainmove.R
import com.loguito.brainmove.ext.*
import com.loguito.brainmove.utils.Constants
import com.loguito.brainmove.viewmodels.CreateAccountViewModel
import com.loguito.brainmove.widgets.OnDialogButtonClicked
import kotlinx.android.synthetic.main.fragment_create_account.*
import java.util.concurrent.TimeUnit

class CreateAccountFragment : Fragment() {
    private lateinit var viewModel: CreateAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CreateAccountViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeChanges()
        bindListener()
    }

    private fun observeChanges() {
        viewModel.createAccountSuccess.observe(viewLifecycleOwner) {
            showDialog(getString(if (it) R.string.create_account_success_text else R.string.create_account_failure_text), R.string.accept_button_text, listener = object :
                OnDialogButtonClicked {
                override fun onPositiveButtonClicked() {
                    if (it) {
                        navigateBack()
                    }
                }
                override fun onNegativeButtonClicked() {}
            })
        }

        viewModel.validFields.observe(viewLifecycleOwner) {
            createAccountButton.isEnabled = it
        }

        viewModel.isValidEmail.observe(viewLifecycleOwner) {
            emailInputLayout.error =
                if (it.not()) getString(R.string.valid_email_error_text) else null
        }

        viewModel.isValidPassword.observe(viewLifecycleOwner) {
            passwordInputLayout.error =
                if (it.not()) getString(R.string.required_field_error_text) else null
        }

        viewModel.isValidName.observe(viewLifecycleOwner) {
            nameInputLayout.error =
                if (it.not()) getString(R.string.required_field_error_text) else null
        }

        viewModel.loadingVisibility.observe(viewLifecycleOwner) {
            if (it) {
                requireActivity().showLoadingSpinner(getString(R.string.loading_sign_in_message))
            } else {
                requireActivity().hideLoadingSpinner()
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun bindListener() {
        emailEditText.textChanges()
            .skipInitialValue()
            .debounce(Constants.DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe { viewModel.validateEmail(it) }

        passwordEditText.textChanges()
            .skipInitialValue()
            .debounce(Constants.DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe { viewModel.validatePassword(it) }

        nameEditText.textChanges()
            .skipInitialValue()
            .debounce(Constants.DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
            .subscribe { viewModel.validateName(it) }

        createAccountButton.clicks()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                viewModel.createAccount(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString(),
                    nameEditText.text.toString()
                )
            }
    }
}