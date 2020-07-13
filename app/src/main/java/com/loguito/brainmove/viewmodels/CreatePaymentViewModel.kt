package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.loguito.brainmove.ext.toReadableDateFromPicker
import com.loguito.brainmove.models.remote.Payment
import java.util.*

class CreatePaymentViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private var description: String = ""
    private var total: Double = 0.0
    private var paymentDate: Date? = null
    private var dueDate: Date? = null

    private var _paymentCreated = MutableLiveData<Boolean>()
    private var _loadingVisibility = MutableLiveData<Boolean>()
    private var _areValidFields = MutableLiveData<Boolean>()
    private var _fromDate = MutableLiveData<String>()
    private var _toDate = MutableLiveData<String>()

    val areValidFields: LiveData<Boolean>
        get() = _areValidFields
    val paymentCreated: LiveData<Boolean>
        get() = _paymentCreated
    val loadingVisibility: LiveData<Boolean>
        get() = _loadingVisibility
    val fromDate: LiveData<String>
        get() = _fromDate
    val toDate: LiveData<String>
        get() = _toDate

    fun validatePaymentDescription(description: String) {
        this.description = description
        validateFields()
    }

    fun validatePaymentTotal(total: Double) {
        this.total = total
        validateFields()
    }

    fun validateDates(paymentDate: Date?, dueDate: Date?) {
        this.paymentDate = paymentDate
        this.dueDate = dueDate
        if (paymentDate != null && dueDate != null) {
            _fromDate.postValue(paymentDate.toReadableDateFromPicker())
            _toDate.postValue(dueDate.toReadableDateFromPicker())
        }
        validateFields()
    }

    private fun validateFields() {
        _areValidFields.postValue(description.isNotEmpty() && total > 0.0 && paymentDate != null && dueDate != null)
    }

    fun savePayment(userId: String) {
        _loadingVisibility.postValue(true)
        db.collection("payments")
            .add(
                Payment(
                    userId,
                    description,
                    total,
                    dueDate ?: Date(),
                    paymentDate ?: Date()
                )
            )
            .addOnCompleteListener {
                _loadingVisibility.postValue(false)
                _paymentCreated.postValue(it.isSuccessful)
            }
    }
}