package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.loguito.brainmove.R
import com.loguito.brainmove.models.remote.Measure

class UserDetailsTrendsViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private var _measures = MutableLiveData<List<Measure>>()
    private var _measuresError = MutableLiveData<Int>()
    private var _loadingVisibility = MutableLiveData<Boolean>()

    val measures: LiveData<List<Measure>>
        get() = _measures
    val measuresError: LiveData<Int>
        get() = _measuresError
    val loadingVisibility: LiveData<Boolean>
        get() = _loadingVisibility

    fun getUserTrends(userId: String) {
        _loadingVisibility.postValue(true)
        db.collection("measures")
            .limit(10)
            .orderBy("date", Query.Direction.ASCENDING)
            .whereEqualTo("user_id", userId)
            .get()
            .addOnSuccessListener { result ->
                _loadingVisibility.postValue(false)
                if (result.isEmpty) {
                    _measures.postValue(emptyList())
                } else {
                    _measures.postValue(result.toObjects(Measure::class.java))
                }
            }
            .addOnFailureListener {
                _loadingVisibility.postValue(false)
                _measuresError.postValue(R.string.retrieve_measures_error)
            }
    }
}