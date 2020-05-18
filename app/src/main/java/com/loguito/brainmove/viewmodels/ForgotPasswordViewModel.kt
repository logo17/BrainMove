package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.loguito.brainmove.ext.isValidEmail

class ForgotPasswordViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private var validEmail: Boolean = false

    private var _validEmail = MutableLiveData<Boolean>()
    private var _validFields = MutableLiveData<Boolean>()
    private var _emailSentSuccess = MutableLiveData<Boolean>()
    private var _loadingVisibility = MutableLiveData<Boolean>()

    val isValidEmail: LiveData<Boolean>
        get() = _validEmail
    val validFields: LiveData<Boolean>
        get() = _validFields
    val emailSentSuccess: LiveData<Boolean>
        get() = _emailSentSuccess
    val loadingVisibility: LiveData<Boolean>
        get() = _loadingVisibility

    fun sendEmailPasswordRecovery(emailAddress: String) {
        _loadingVisibility.postValue(true)
        auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                _loadingVisibility.postValue(false)
                _emailSentSuccess.postValue(task.isSuccessful)
            }
    }

    fun validateEmail(email: CharSequence) {
        validEmail = email.isValidEmail()
        _validEmail.postValue(validEmail)
        validateFields()
    }

    private fun validateFields() {
        _validFields.postValue(validEmail)
    }
}