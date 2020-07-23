package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.loguito.brainmove.ext.toReadableDateFromPicker
import com.loguito.brainmove.models.remote.Plan
import com.loguito.brainmove.models.remote.Routine
import java.util.*

class CreatePlanViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val routineList = mutableListOf<Routine>()
    var planName: String = ""
        set(value) {
            field = value
            validateFields()
        }
    var planGoal: String = ""
        set(value) {
            field = value
            validateFields()
        }
    private var fromDate: Date? = null
    private var toDate: Date? = null

    private var _areValidFields = MutableLiveData<Boolean>()
    private var _routines = MutableLiveData<List<Routine>>()
    private var _savePlanResponse = MutableLiveData<Boolean>()
    private var _loadingVisibility = MutableLiveData<Boolean>()
    private var _dateRangeAsString = MutableLiveData<String>()
    private var _planNameInput = MutableLiveData<String>()
    private var _planGoalInput = MutableLiveData<String>()

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
    val planNameOutput: LiveData<String>
        get() = _planNameInput
    val planGoalOutput: LiveData<String>
        get() = _planGoalInput

    fun addRoutineToList(index: Int, routine: Routine) {
        if (index != -1) {
            routineList[index] = routine.copy()
        } else {
            routineList.add(routine.copy())
        }
        _routines.postValue(routineList)
    }

    fun addRoutinesToList(routines: List<Routine>) {
        routineList.addAll(routines)
        _routines.postValue(routineList)
    }

    fun validateDates(fromDate: Date?, toDate: Date?) {
        this.fromDate = fromDate
        this.toDate = toDate
        if (fromDate != null && toDate != null) {
            _dateRangeAsString.postValue(
                String.format(
                    "%s - %s",
                    fromDate.toReadableDateFromPicker(),
                    toDate.toReadableDateFromPicker()
                )
            )
        }
        validateFields()
    }

    private fun validateFields() {
        _areValidFields.postValue(planName.isNotEmpty() && planGoal.isNotEmpty() && fromDate != null && toDate != null && routineList.isNotEmpty())
    }

    fun updatePlan(userId: String, planId: String) {
        _loadingVisibility.postValue(true)
        db.collection("plan").document(planId)
            .set(Plan(
                userId,
                planName,
                planGoal,
                toDate ?: Date(),
                fromDate ?: Date(),
                routineList
            ))
            .addOnCompleteListener {
                _loadingVisibility.postValue(false)
                _savePlanResponse.postValue(it.isSuccessful)
            }
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

    fun setPlanNameInput(name: String) {
        _planNameInput.postValue(name)
    }

    fun setPlanGoalInput(goal: String) {
        _planGoalInput.postValue(goal)
    }
}