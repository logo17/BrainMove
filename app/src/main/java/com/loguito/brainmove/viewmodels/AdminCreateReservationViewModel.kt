package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.loguito.brainmove.models.remote.RemoteReservation
import com.loguito.brainmove.utils.Constants.Companion.ACTIVITY_ID
import java.text.SimpleDateFormat
import java.util.*

class AdminCreateReservationViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private var maxCapacity: Int = 0
    private var reservationDate: String = ""
    private var reservationHour: String = ""

    private var _sessionCreated = MutableLiveData<Boolean>()
    private var _loadingVisibility = MutableLiveData<Boolean>()
    private var _areValidFields = MutableLiveData<Boolean>()

    val areValidFields: LiveData<Boolean>
        get() = _areValidFields
    val sessionCreated: LiveData<Boolean>
        get() = _sessionCreated
    val loadingVisibility: LiveData<Boolean>
        get() = _loadingVisibility

    fun validateMaxCapacity(maxCapacity: Int) {
        this.maxCapacity = maxCapacity
        validateFields()
    }

    fun validateDate(reservationDate: String) {
        this.reservationDate = reservationDate
        validateFields()
    }

    fun validateHour(reservationHour: String) {
        this.reservationHour = reservationHour
        validateFields()
    }

    private fun validateFields() {
        _areValidFields.postValue(reservationDate.isNotEmpty() && maxCapacity > 0 && reservationHour.isNotEmpty())
    }

    fun saveSession() {
        _loadingVisibility.postValue(true)
        db.collection("reservation")
            .add(
                RemoteReservation(
                    ACTIVITY_ID, //TODO replace this for valid activityId
                    maxCapacity,
                    getDateFromStrings(reservationDate, reservationHour),
                    emptyList(),
                    false
                )
            )
            .addOnCompleteListener {
                _loadingVisibility.postValue(false)
                _sessionCreated.postValue(it.isSuccessful)
            }
    }

    private fun getDateFromStrings(date: String, hour: String): Date {
        val dateAsString = String.format("%s %s", date, hour)
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault())
        return formatter.parse(dateAsString) ?: Date()
    }
}