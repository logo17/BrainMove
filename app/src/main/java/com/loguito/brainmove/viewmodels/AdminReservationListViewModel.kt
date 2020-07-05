package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.loguito.brainmove.R
import com.loguito.brainmove.ext.toReadableDate
import com.loguito.brainmove.models.remote.RemoteReservation
import java.util.*

class AdminReservationListViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private var fromFormattedDate = getFromDate(Date())
    private var toFormattedDate = getToDate(Date())

    private var _reservationList = MutableLiveData<List<RemoteReservation>>()
    private var _reservationListError = MutableLiveData<Int>()
    private var _loadingVisibility = MutableLiveData<Boolean>()
    private var _removeSessionSuccess = MutableLiveData<Boolean>()

    val reservationList: LiveData<List<RemoteReservation>>
        get() = _reservationList
    val reservationListError: LiveData<Int>
        get() = _reservationListError
    val loadingVisibility: LiveData<Boolean>
        get() = _loadingVisibility
    val removeSessionSuccess: LiveData<Boolean>
        get() = _removeSessionSuccess

    fun getReservationList() {
        _loadingVisibility.postValue(true)
        db.collection("reservation")
            .whereGreaterThanOrEqualTo("date", Timestamp(fromFormattedDate))
            .whereLessThan("date", Timestamp(toFormattedDate))
            .get()
            .addOnSuccessListener { result ->
                _loadingVisibility.postValue(false)
                if (result.isEmpty) {
                    _reservationList.postValue(emptyList())
                } else {
                    val resultList = mutableListOf<RemoteReservation>()
                    for (document in result) {
                        val reservation =
                            document.toObject(RemoteReservation::class.java)
                        reservation.id = document.id
                        resultList.add(reservation)
                    }
                    _reservationList.postValue(resultList)
                }
            }
            .addOnFailureListener {
                _loadingVisibility.postValue(false)
                _reservationListError.postValue(R.string.retrieve_reservations_error)
            }
    }

    fun removeSession(sessionId: String) {
        _loadingVisibility.postValue(true)
        db.collection("reservation").document(sessionId)
            .delete()
            .addOnCompleteListener {
                _loadingVisibility.postValue(false)
                _removeSessionSuccess.postValue(it.isSuccessful)
                getReservationList()
            }
    }

    fun updateDates(date: Date) {
        fromFormattedDate = getFromDate(date)
        toFormattedDate = getToDate(date)
        getReservationList()
    }

    fun getDateText() = fromFormattedDate.toReadableDate()

    private fun getFromDate(day: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = day
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.time
    }

    private fun getToDate(day: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = day
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 59)
        return cal.time
    }
}