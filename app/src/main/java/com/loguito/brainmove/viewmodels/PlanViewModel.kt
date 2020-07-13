package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.loguito.brainmove.R
import com.loguito.brainmove.models.remote.Plan
import java.util.*

class PlanViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var _plan = MutableLiveData<Plan?>()
    private var _planError = MutableLiveData<Int>()
    private var _loadingVisibility = MutableLiveData<Boolean>()

    val plan: LiveData<Plan?>
        get() = _plan
    val planError: LiveData<Int>
        get() = _planError
    val loadingVisibility: LiveData<Boolean>
        get() = _loadingVisibility

    init {
        auth.currentUser?.let { user ->
            val currentDate = Date()
            _loadingVisibility.postValue(true)
            db.collection("plan")
                .limit(1)
                .whereGreaterThan("toDate", Timestamp(currentDate))
                .whereEqualTo("userId", user.uid)
                .get()
                .addOnSuccessListener { result ->
                    _loadingVisibility.postValue(false)
                    if (result.isEmpty) {
                        _plan.postValue(null)
                    } else {
                        for (document in result) {
                            _plan.postValue(document.toObject(Plan::class.java))
                        }
                    }
                }
                .addOnFailureListener {
                    _loadingVisibility.postValue(false)
                    _planError.postValue(R.string.retrieve_plan_error)
                }
        }
    }
}