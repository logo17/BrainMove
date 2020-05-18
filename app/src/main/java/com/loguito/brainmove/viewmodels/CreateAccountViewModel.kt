package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.loguito.brainmove.ext.isValidEmail
import com.loguito.brainmove.ext.isValidName
import com.loguito.brainmove.ext.isValidPassword
import java.util.*

class CreateAccountViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private var validEmail: Boolean = false
    private var validPassword: Boolean = false
    private var validName: Boolean = false

    private var _validEmail = MutableLiveData<Boolean>()
    private var _validPassword = MutableLiveData<Boolean>()
    private var _validName = MutableLiveData<Boolean>()
    private var _validFields = MutableLiveData<Boolean>()
    private var _createAccountSuccess = MutableLiveData<Boolean>()
    private var _loadingVisibility = MutableLiveData<Boolean>()

    val isValidEmail: LiveData<Boolean>
        get() = _validEmail
    val isValidPassword: LiveData<Boolean>
        get() = _validPassword
    val isValidName: LiveData<Boolean>
        get() = _validName
    val validFields: LiveData<Boolean>
        get() = _validFields
    val createAccountSuccess: LiveData<Boolean>
        get() = _createAccountSuccess
    val loadingVisibility: LiveData<Boolean>
        get() = _loadingVisibility

    fun createAccount(emailAddress: String, password: String, name: String) {
        _loadingVisibility.postValue(true)
        auth.createUserWithEmailAndPassword(emailAddress, password)
            .addOnSuccessListener {
                val createdUser = auth.currentUser
                createdUser?.let { user ->
                    val builder =
                        UserProfileChangeRequest.Builder().setDisplayName(name).build()
                    user.updateProfile(builder)

                    val keywords = name.split(" ").map { it.toLowerCase(Locale.getDefault()) }

                    val userDetails = hashMapOf(
                        "fullName" to name,
                        "email" to user.email,
                        "id" to user.uid,
                        "keywords" to keywords
                    )
                    db.collection("users")
                        .add(userDetails)
                        .addOnSuccessListener {
                            user.sendEmailVerification()
                                .addOnCompleteListener { task ->
                                    _loadingVisibility.postValue(false)
                                    _createAccountSuccess.postValue(task.isSuccessful)
                                }
                        }
                        .addOnFailureListener {
                            _loadingVisibility.postValue(false)
                            _createAccountSuccess.postValue(false)
                        }
                }
            }
            .addOnFailureListener {
                _loadingVisibility.postValue(false)
                _createAccountSuccess.postValue(false)
            }
    }

    fun validateEmail(email: CharSequence) {
        validEmail = email.isValidEmail()
        _validEmail.postValue(validEmail)
        validateFields()
    }

    fun validatePassword(password: CharSequence) {
        validPassword = password.isValidPassword()
        _validEmail.postValue(validPassword)
        validateFields()
    }

    fun validateName(name: CharSequence) {
        validName = name.isValidName()
        _validEmail.postValue(validName)
        validateFields()
    }

    private fun validateFields() {
        _validFields.postValue(validEmail && validPassword && validName)
    }
}