package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.loguito.brainmove.R
import com.loguito.brainmove.models.remote.Measure

class UserDetailsMeasuresViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private var _measures = MutableLiveData<Measure?>()
    private var _measuresError = MutableLiveData<Int>()
    private var _loadingVisibility = MutableLiveData<Boolean>()

    val measures: LiveData<Measure?>
        get() = _measures
    val measuresError: LiveData<Int>
        get() = _measuresError
    val loadingVisibility: LiveData<Boolean>
        get() = _loadingVisibility

    fun getUserMeasures(userId: String) {
        _loadingVisibility.postValue(true)
        db.collection("measures")
            .limit(1)
            .orderBy("date", Query.Direction.DESCENDING)
            .whereEqualTo("user_id", userId)
            .get()
            .addOnSuccessListener { result ->
                _loadingVisibility.postValue(false)
                if (result.isEmpty) {
                    _measures.postValue(null)
                } else {
                    for (document in result) {
                        _measures.postValue(document.toObject(Measure::class.java))
                    }
                }
            }
            .addOnFailureListener {
                _loadingVisibility.postValue(false)
                _measuresError.postValue(R.string.retrieve_measures_error)
            }
    }
}