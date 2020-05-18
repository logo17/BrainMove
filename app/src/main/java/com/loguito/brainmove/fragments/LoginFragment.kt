package com.loguito.brainmove.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.loguito.brainmove.R
import com.loguito.brainmove.ext.*
import com.loguito.brainmove.utils.Constants
import com.loguito.brainmove.viewmodels.LoginViewModel
import com.loguito.brainmove.widgets.OnDialogButtonClicked
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.concurrent.TimeUnit

class LoginFragment : Fragment() {
    private lateinit var layoutListener: ViewTreeObserver.OnGlobalLayoutListener
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        addKeyboardDetectListener()
    }

    override fun onPause() {
        super.onPause()
        val topView = requireActivity().window.decorView.findViewById<View>(android.R.id.content)
        topView.viewTreeObserver.removeOnGlobalLayoutListener(layoutListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeChanges()
        bindListener()
    }

    private fun observeChanges() {
        viewModel.isLoggedUserAdmin.observe(viewLifecycleOwner) {
            dismissKeyboard()
            val navOption = NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build()
            val action: NavDirections
            if (it.not()) {
                action = LoginFragmentDirections.actionLoginFragmentToMainFragment()
            } else {
                action = LoginFragmentDirections.actionLoginFragmentToAdminMainFragment2()
            }
            findNavController().navigate(action, navOption)
        }

        viewModel.verifyEmail.observe(viewLifecycleOwner) {
            if (it) {
                showVerifyEmailDialog()
            }
        }

        viewModel.validFields.observe(viewLifecycleOwner) {
            loginButton.isEnabled = it
        }

        viewModel.loginError.observe(viewLifecycleOwner) {
            showDialog(getString(it), R.string.accept_button_text)
        }

        viewModel.isValidEmail.observe(viewLifecycleOwner) {
            emailInputLayout.error =
                if (it.not() && emailEditText.text?.isNotEmpty() == true) getString(R.string.valid_email_error_text) else null
        }

        viewModel.isValidPassword.observe(viewLifecycleOwner) {
            passwordInputLayout.error =
                if (it.not() && passwordEditText.text?.isNotEmpty() == true) getString(R.string.required_field_error_text) else null
        }

        viewModel.loadingVisibility.observe(viewLifecycleOwner) {
            if (it) {
                requireActivity().showLoadingSpinner(getString(R.string.loading_login_message))
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

        forgotPasswordButton.clicks()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                findNavController()
                    .navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
            }

        signInButton.clicks()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                findNavController()
                    .navigate(LoginFragmentDirections.actionLoginFragmentToCreateAccountFragment())
            }

        loginButton.clicks()
            .throttleFirst(Constants.THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .subscribe {
                viewModel.loginUser(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
    }

    private fun showVerifyEmailDialog() {
        showDialog(getString(
            R.string.verify_email_text_message,
            requireContext().getString(R.string.app_name)
        ), R.string.resend_email_text, R.string.cancel_text, listener = object : OnDialogButtonClicked {
            override fun onPositiveButtonClicked() {
                viewModel.resendEmailVerification()
            }

            override fun onNegativeButtonClicked() {}
        })
    }

    private fun addKeyboardDetectListener() {
        val topView = requireActivity().window.decorView.findViewById<View>(android.R.id.content)
        layoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            val heightDifference = topView.rootView.height - topView.height
            if (heightDifference > dpToPx(requireContext(), 200F)) {
                termsConditionButton.visibility = View.GONE
            } else {
                termsConditionButton.visibility = View.VISIBLE
            }
        }
        topView.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
    }

}