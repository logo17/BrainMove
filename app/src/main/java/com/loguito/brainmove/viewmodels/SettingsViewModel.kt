package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.loguito.brainmove.R

class SettingsViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private var _userName = MutableLiveData<String>()
    private var _email = MutableLiveData<String>()
    private var _isLoggedOutUser = MutableLiveData<Boolean>()

    val isLoggedOutUser: LiveData<Boolean>
        get() = _isLoggedOutUser
    val userName: LiveData<String>
        get() = _userName
    val email: LiveData<String>
        get() = _email

    init {
        auth.currentUser?.let { user ->
            _userName.postValue(user.displayName ?: "")
            _email.postValue(user.email ?: "")
        }
    }

    fun logoutUser() {
        auth.signOut()
        _isLoggedOutUser.postValue(true)
    }
}