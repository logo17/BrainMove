package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.loguito.brainmove.models.remote.Measure
import com.loguito.brainmove.utils.SingleLiveEvent

class SharedViewModel : ViewModel() {
    private val _receivedMeasurement = SingleLiveEvent<Measure>()

    val receivedMeasurement: LiveData<Measure>
        get() = _receivedMeasurement

    fun setReceivedMeasure(value: Measure) {
        _receivedMeasurement.postValue(value)
    }
}