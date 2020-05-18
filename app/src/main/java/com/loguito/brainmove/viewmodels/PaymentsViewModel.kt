package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.loguito.brainmove.R
import com.loguito.brainmove.models.remote.Payment
import java.util.*

class PaymentsViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var _payments = MutableLiveData<List<Payment>>()
    private var _paymentError = MutableLiveData<Int>()
    private var _loadingVisibility = MutableLiveData<Boolean>()

    val payments: LiveData<List<Payment>>
        get() = _payments
    val paymentError: LiveData<Int>
        get() = _paymentError
    val loadingVisibility: LiveData<Boolean>
        get() = _loadingVisibility

    init {
        auth.currentUser?.let { user ->
            val currentDate = Date()
            _loadingVisibility.postValue(true)
            db.collection("payments")
                .limit(12)
                .whereEqualTo("userId", user.uid)
                .get()
                .addOnSuccessListener { result ->
                    _loadingVisibility.postValue(false)
                    if (result.isEmpty) {
                        _payments.postValue(emptyList())
                    } else {
                        _payments.postValue(result.toObjects(Payment::class.java))
                    }
                }
                .addOnFailureListener {
                    _loadingVisibility.postValue(false)
                    _paymentError.postValue(R.string.retrieve_payment_error)
                }
        }
    }
}