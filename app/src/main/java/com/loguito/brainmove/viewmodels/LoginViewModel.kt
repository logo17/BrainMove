package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.loguito.brainmove.R
import com.loguito.brainmove.ext.isValidEmail
import com.loguito.brainmove.ext.isValidPassword
import com.loguito.brainmove.utils.SingleLiveEvent

class LoginViewModel : ViewModel() {

    private var validEmail: Boolean = false
    private var validPassword: Boolean = false

    private val auth = FirebaseAuth.getInstance()
    private var _isLoggedUserAdmin = MutableLiveData<Boolean>()
    private var _verifyEmail = MutableLiveData<Boolean>()
    private var _loginError = SingleLiveEvent<Int>()
    private var _loadingVisibility = MutableLiveData<Boolean>()
    private var _validEmail = MutableLiveData<Boolean>()
    private var _validPassword = MutableLiveData<Boolean>()
    private var _validFields = MutableLiveData<Boolean>()

    val verifyEmail: LiveData<Boolean>
        get() = _verifyEmail
    val isLoggedUserAdmin: LiveData<Boolean>
        get() = _isLoggedUserAdmin
    val isValidEmail: LiveData<Boolean>
        get() = _validEmail
    val isValidPassword: LiveData<Boolean>
        get() = _validPassword
    val validFields: LiveData<Boolean>
        get() = _validFields
    val loginError: LiveData<Int>
        get() = _loginError
    val loadingVisibility: LiveData<Boolean>
        get() = _loadingVisibility

    fun loginUser(email: String, password: String) {
        _loadingVisibility.postValue(true)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    checkIfUserIsLoggedIn()
                } else {
                    _loginError.postValue(R.string.login_error_general_message)
                }
                _loadingVisibility.postValue(false)
            }
    }

    private fun checkIfUserIsLoggedIn() {
        val user = auth.currentUser
        if (user != null) {
            _verifyEmail.postValue(user.isEmailVerified.not())
            if (user.isEmailVerified) {
                user.getIdToken(false).addOnCompleteListener { token ->
                    val isAdmin = token.result?.claims?.get("admin") as? Boolean ?: false
                    _isLoggedUserAdmin.postValue(isAdmin)
                }
            }
        }
    }

    fun resendEmailVerification() {
        auth.currentUser?.sendEmailVerification()
    }

    fun validateEmail(email: CharSequence) {
        validEmail = email.isValidEmail()
        _validEmail.postValue(validEmail)
        validateFields()
    }

    fun validatePassword(password: CharSequence) {
        validPassword = password.isValidPassword()
        _validPassword.postValue(validPassword)
        validateFields()
    }

    private fun validateFields() {
        _validFields.postValue(validEmail && validPassword)
    }
}