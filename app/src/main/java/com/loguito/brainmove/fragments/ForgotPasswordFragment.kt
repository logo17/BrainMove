package com.loguito.brainmove.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.loguito.brainmove.R
import com.loguito.brainmove.ext.*
import com.loguito.brainmove.utils.Constants
import com.loguito.brainmove.viewmodels.ForgotPasswordViewModel
import com.loguito.brainmove.widgets.OnDialogButtonClicked
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import java.util.concurrent.TimeUnit

class ForgotPasswordFragment : Fragment() {
    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeChanges()
        bindListener()
    }

    private fun observeChanges() {
        viewModel.emailSentSuccess.observe(viewLifecycleOwner) {
            dismissKeyboard()
            showDialog(
                getString(if (it) R.string.recovery_password_email_sent_text else R.string.recovery_password_email_sent_error_text),
                R.string.accept_button_text,
                listener = object :
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
            passwordRecoveryButton.isEnabled = it
        }

        viewModel.isValidEmail.observe(viewLifecycleOwner) {
            emailInputLayout.error =
                if (it.not()) getString(R.string.valid_email_error_text) else null
        }

        viewModel.loadingVisibility.observe(viewLifecycleOwner) {
            if (it) {
                requireActivity().showLoadingSpinner(getString(R.string.loading_forgot_password_message))
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

        passwordRecoveryButton.clicks()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe { viewModel.sendEmailPasswordRecovery(emailEditText.text.toString()) }
    }
}