package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.loguito.brainmove.R
import com.loguito.brainmove.models.remote.User
import java.util.*

class UserListViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var _users = MutableLiveData<List<User>>()
    private var _loadingVisibility = MutableLiveData<Boolean>()
    private var _usersError = MutableLiveData<Int>()
    private var _isLoggedOutUser = MutableLiveData<Boolean>()

    val users: LiveData<List<User>>
        get() = _users
    val loadingVisibility: LiveData<Boolean>
        get() = _loadingVisibility
    val usersError: LiveData<Int>
        get() = _usersError
    val isLoggedOutUser: LiveData<Boolean>
        get() = _isLoggedOutUser

    fun logoutUser() {
        auth.signOut()
        _isLoggedOutUser.postValue(true)
    }

    fun getUserByKeyword(keyWord: String) {
        _loadingVisibility.postValue(true)
        db.collection("users")
            .limit(10)
            .whereArrayContains("keywords", keyWord.toLowerCase(Locale.getDefault()))
            .get()
            .addOnSuccessListener { result ->
                _loadingVisibility.postValue(false)
                if (result.isEmpty) {
                    _users.postValue(emptyList())
                } else {
                    _users.postValue(result.toObjects(User::class.java))
                }
            }
            .addOnFailureListener {
                _loadingVisibility.postValue(false)
                _usersError.postValue(R.string.retrieve_users_error)
            }
    }
}