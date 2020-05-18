package com.loguito.brainmove.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.loguito.brainmove.ext.toReadableDate
import com.loguito.brainmove.models.remote.Plan
import com.loguito.brainmove.models.remote.Routine
import java.util.*

class CreatePlanViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val routineList = mutableListOf<Routine>()
    private var planName: String = ""
    private var planGoal: String = ""
    private var fromDate: Date? = null
    private var toDate: Date? = null

    private var _areValidFields = MutableLiveData<Boolean>()
    private var _routines = MutableLiveData<List<Routine>>()
    private var _savePlanResponse = MutableLiveData<Boolean>()
    private var _loadingVisibility = MutableLiveData<Boolean>()
    private var _dateRangeAsString = MutableLiveData<String>()

    val routines: LiveData<List<Routine>>
        get() = _routines
    val areValidFields: LiveData<Boolean>
        get() = _areValidFields
    val savePlanResponse: LiveData<Boolean>
        get() = _savePlanResponse
    val loadingVisibility: LiveData<Boolean>
        get() = _loadingVisibility
    val dateRangeAsString: LiveData<String>
        get() = _dateRangeAsString

    fun addRoutineToList(routine: Routine) {
        routineList.add(routine.copy())
        _routines.postValue(routineList)
    }

    fun validatePlanName(name: String) {
        planName = name
        validateFields()
    }

    fun validatePlanGoal(goal: String) {
        planGoal = goal
        validateFields()
    }

    fun validateDates(fromDate: Date?, toDate: Date?) {
        this.fromDate = fromDate
        this.toDate = toDate
        if (fromDate != null && toDate != null) {
            _dateRangeAsString.postValue(
                String.format(
                    "%s - %s",
                    fromDate.toReadableDate(),
                    toDate.toReadableDate()
                )
            )
        }
        validateFields()
    }

    private fun validateFields() {
        _areValidFields.postValue(planName.isNotEmpty() && planGoal.isNotEmpty() && fromDate != null && toDate != null && routineList.isNotEmpty())
    }

    fun savePlan(userId: String) {
        _loadingVisibility.postValue(true)
        db.collection("plan")
            .add(
                Plan(
                    userId,
                    planName,
                    planGoal,
                    toDate ?: Date(),
                    fromDate ?: Date(),
                    routineList
                )
            )
            .addOnCompleteListener {
                _loadingVisibility.postValue(false)
                _savePlanResponse.postValue(it.isSuccessful)
            }
    }
}