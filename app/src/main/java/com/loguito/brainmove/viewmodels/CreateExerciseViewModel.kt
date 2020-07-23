package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loguito.brainmove.models.remote.Exercise
import com.loguito.brainmove.models.remote.UnitMeasure
import com.loguito.brainmove.utils.SingleLiveEvent
import java.util.*

class CreateExerciseViewModel : ViewModel() {
    private var explanationList = mutableListOf<String>()
    private var remoteDuration = ""

    var exerciseDuration: String = ""
        set(value) {
            field = value
            remoteDuration = String.format("%s %s", field, exerciseUnitMeasure)
            validateFields()
        }

    var exerciseUnitMeasure: String = ""
        set(value) {
            field = value
            remoteDuration = String.format("%s %s", exerciseDuration, field)
            validateFields()
        }

    private var _backgroundImageUrlInput = MutableLiveData<String>()
    private var _demoImageUrlInput = MutableLiveData<String>()
    private var _exerciseNameInput = MutableLiveData<String>()
    private var _exerciseDurationInput = MutableLiveData<String>()
    private var _exerciseUnitMeasureInput = MutableLiveData<Int>()
    private var _areValidFields = MutableLiveData<Boolean>()
    private var _explanations = MutableLiveData<List<String>>()
    private var _exerciseError = MutableLiveData<Int>()
    private var _addExerciseToBlockOutput = SingleLiveEvent<Exercise>()

    val exerciseDurationOutput: LiveData<String>
        get() = _exerciseDurationInput
    val exerciseUnitMeasureOutput: LiveData<Int>
        get() = _exerciseUnitMeasureInput
    val explanations: LiveData<List<String>>
        get() = _explanations
    val backgroundImageUrlOutput: LiveData<String>
        get() = _backgroundImageUrlInput
    val demoImageUrlOutput: LiveData<String>
        get() = _demoImageUrlInput
    val exerciseNameOutput: LiveData<String>
        get() = _exerciseNameInput
    val areValidFields: LiveData<Boolean>
        get() = _areValidFields
    val exerciseError: LiveData<Int>
        get() = _exerciseError
    val addExerciseToBlockOutput: LiveData<Exercise>
        get() = _addExerciseToBlockOutput

    fun addExplanationsToList(explanationList: List<String>) {
        this.explanationList.addAll(explanationList)
        _explanations.postValue(explanationList)
    }

    fun setExerciseDurationInput(duration: String) {
        _exerciseDurationInput.postValue(duration)
    }

    fun setExerciseUnitMeasureInput(measure: String, unitMeasures: List<UnitMeasure>) {
        _exerciseUnitMeasureInput.postValue(
            unitMeasures.indexOf(
                UnitMeasure(
                    measure.toLowerCase(
                        Locale.getDefault()
                    )
                )
            )
        )
    }

    fun setExerciseBackgroundImageInput(imageUrl: String) {
        _backgroundImageUrlInput.postValue(imageUrl)
    }

    fun setExerciseDemoImageInput(imageUrl: String) {
        _demoImageUrlInput.postValue(imageUrl)
    }

    fun setExerciseNameInput(name: String) {
        _exerciseNameInput.postValue(name)
    }

    fun setSaveButtonInput(exercise: Exercise) {
        _addExerciseToBlockOutput.postValue(
            exercise.copy(
                quantity = remoteDuration,
                explanations = explanationList
            )
        )
    }

    fun removeExplanationFromList(explanation: String) {
        this.explanationList.remove(explanation)
        _explanations.postValue(explanationList)
    }

    private fun validateFields() {
        _areValidFields.postValue(exerciseDuration.isNotEmpty() && exerciseUnitMeasure.isNotEmpty())
    }

    fun addExplanationToList(explanation: String) {
        this.explanationList.add(0, explanation)
        _explanations.postValue(explanationList)
    }
}