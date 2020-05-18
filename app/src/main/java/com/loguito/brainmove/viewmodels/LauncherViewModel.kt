package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LauncherViewModel : ViewModel() {
    private var _isLoggedUserAdmin = MutableLiveData<Boolean>()
    private var _navigateToLogin = MutableLiveData<Boolean>()
    private val auth = FirebaseAuth.getInstance()

    val isLoggedUserAdmin: LiveData<Boolean>
        get() = _isLoggedUserAdmin
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    fun checkIfUserIsLoggedIn() {
        val user = auth.currentUser
        if (user != null && user.isEmailVerified) {
            user.getIdToken(false).addOnCompleteListener { token ->
                val isAdmin = token.result?.claims?.get("admin") as? Boolean ?: false
                _isLoggedUserAdmin.postValue(isAdmin)
            }
        } else {
            _navigateToLogin.postValue(true)
        }
    }
}