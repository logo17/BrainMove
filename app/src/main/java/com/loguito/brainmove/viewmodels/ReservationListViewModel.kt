package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.loguito.brainmove.R
import com.loguito.brainmove.models.local.Reservation
import com.loguito.brainmove.models.remote.RemoteReservation
import java.util.*

class ReservationListViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var _reservationList = MutableLiveData<List<Reservation>>()
    private var _reservationListError = MutableLiveData<Int>()
    private var _handleReservationError = MutableLiveData<Int>()
    private var _loadingVisibility = MutableLiveData<Boolean>()
    private var _handleReservation = MutableLiveData<Int>()

    val reservationList: LiveData<List<Reservation>>
        get() = _reservationList
    val handleReservationError: LiveData<Int>
        get() = _handleReservationError
    val reservationListError: LiveData<Int>
        get() = _reservationListError
    val loadingVisibility: LiveData<Boolean>
        get() = _loadingVisibility
    val handleReservation: LiveData<Int>
        get() = _handleReservation

    fun getReservationList(fromDate: Date, toDate: Date) {
        auth.currentUser?.let { user ->
            _loadingVisibility.postValue(true)
            db.collection("reservation")
                .whereGreaterThanOrEqualTo("date", Timestamp(fromDate))
                .whereLessThan("date", Timestamp(toDate))
                .get()
                .addOnSuccessListener { result ->
                    _loadingVisibility.postValue(false)
                    if (result.isEmpty) {
                        _reservationList.postValue(emptyList())
                    } else {
                        val resultList = mutableListOf<Reservation>()
                        for (document in result) {
                            val currentReservation =
                                document.toObject(RemoteReservation::class.java)
                            val reservation = Reservation(
                                document.id,
                                currentReservation.maxCapacity,
                                currentReservation.date,
                                (currentReservation.maxCapacity - currentReservation.spaces.size),
                                currentReservation.spaces.contains(user.uid),
                                currentReservation.activityId
                            )
                            resultList.add(reservation)
                        }
                        _reservationList.postValue(resultList)
                    }
                }
                .addOnFailureListener {
                    _loadingVisibility.postValue(false)
                    _handleReservationError.postValue(R.string.retrieve_reservations_error)
                }
        }
    }

    fun makeReservation(reservation: Reservation) {
        auth.currentUser?.let { user ->
            _loadingVisibility.postValue(true)
            val reservationRef = db.collection("reservation").document(reservation.id)
            reservationRef.update("spaces", FieldValue.arrayUnion(user.uid))
                .addOnSuccessListener {
                    _handleReservation.postValue(R.string.make_reservation_success)
                }
                .addOnFailureListener {
                    _handleReservationError.postValue(R.string.make_reservation_error)
                }
        }
    }

    fun releaseReservation(reservation: Reservation) {
        auth.currentUser?.let { user ->
            _loadingVisibility.postValue(true)
            val reservationRef = db.collection("reservation").document(reservation.id)
            reservationRef.update("spaces", FieldValue.arrayRemove(user.uid))
                .addOnSuccessListener {
                    _handleReservation.postValue(R.string.release_reservation_success)
                }
                .addOnFailureListener {
                    _reservationListError.postValue(R.string.release_reservation_error)
                }
        }
    }
}