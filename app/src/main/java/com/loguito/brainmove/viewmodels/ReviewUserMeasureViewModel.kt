package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.loguito.brainmove.models.remote.Measure
import com.loguito.brainmove.utils.SingleLiveEvent

class ReviewUserMeasureViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private var _loadingVisibility = SingleLiveEvent<Boolean>()
    private var _measureCreatedSuccess = SingleLiveEvent<Unit>()
    private var _measureCreatedError = SingleLiveEvent<Unit>()

    val loadingVisibility: LiveData<Boolean>
        get() = _loadingVisibility
    val measureCreated: LiveData<Unit>
        get() = _measureCreatedSuccess
    val measureCreatedError: LiveData<Unit>
        get() = _measureCreatedError

    fun saveMeasure(measure: Measure) {
        _loadingVisibility.postValue(true)
        db.collection("measures")
            .add(measure)
            .addOnSuccessListener {
                _measureCreatedSuccess.postValue(Unit)
                _loadingVisibility.postValue(false)
            }
            .addOnFailureListener {
                _measureCreatedError.postValue(Unit)
                _loadingVisibility.postValue(false)

            }
    }
}