package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.loguito.brainmove.R
import com.loguito.brainmove.models.remote.Block
import com.loguito.brainmove.models.remote.Exercise
import com.loguito.brainmove.models.remote.UnitMeasure
import java.util.*

class CreateBlockViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private var blockName: String = ""
    private var blockDescription: String = ""
    private var blockDuration: String = ""
    private var blockUnitMeasure: String = ""
    private var blockBackgroundImageUrl: String = ""
    private var exerciseList = mutableListOf<Exercise>()

    private var _exercises = MutableLiveData<List<Exercise>>()
    private var _block = MutableLiveData<Block>()
    private var _selectedExercises = MutableLiveData<List<Exercise>>()
    private var _exerciseError = MutableLiveData<Int>()
    private var _loadingVisibility = MutableLiveData<Boolean>()
    private var _areValidFields = MutableLiveData<Boolean>()
    private var _blockUnitMeasures = MutableLiveData<List<UnitMeasure>>()
    private var _workoutUnitMeasures = MutableLiveData<List<UnitMeasure>>()

    val exercises: LiveData<List<Exercise>>
        get() = _exercises
    val selectedExercises: LiveData<List<Exercise>>
        get() = _selectedExercises
    val exerciseError: LiveData<Int>
        get() = _exerciseError
    val loadingVisibility: LiveData<Boolean>
        get() = _loadingVisibility
    val areValidFields: LiveData<Boolean>
        get() = _areValidFields
    val block: LiveData<Block>
        get() = _block
    val blockUnitMeasures: LiveData<List<UnitMeasure>>
        get() = _blockUnitMeasures
    val workoutUnitMeasures: LiveData<List<UnitMeasure>>
        get() = _workoutUnitMeasures

    init {
        _loadingVisibility.postValue(true)
        db.collection("block_unit_measure")
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    _blockUnitMeasures.postValue(emptyList())
                } else {
                    _blockUnitMeasures.postValue(result.toObjects(UnitMeasure::class.java))
                }
                db.collection("workout_unit_measure")
                    .get()
                    .addOnSuccessListener { result ->
                        _loadingVisibility.postValue(false)
                        if (result.isEmpty) {
                            _workoutUnitMeasures.postValue(emptyList())
                        } else {
                            _workoutUnitMeasures.postValue(result.toObjects(UnitMeasure::class.java))
                        }
                    }
                    .addOnFailureListener {
                        _loadingVisibility.postValue(false)
                        _exerciseError.postValue(R.string.retrieve_workout_measure_error)
                    }
            }
            .addOnFailureListener {
                _loadingVisibility.postValue(false)
                _exerciseError.postValue(R.string.retrieve_block_measure_error)
            }
    }

    fun getExerciseByKeyword(keyWord: String) {
        _loadingVisibility.postValue(true)
        db.collection("exercise")
            .limit(10)
            .whereArrayContains("keywords", keyWord.toLowerCase(Locale.getDefault()))
            .get()
            .addOnSuccessListener { result ->
                _loadingVisibility.postValue(false)
                if (result.isEmpty) {
                    _exercises.postValue(emptyList())
                } else {
                    _exercises.postValue(result.toObjects(Exercise::class.java))
                }
            }
            .addOnFailureListener {
                _loadingVisibility.postValue(false)
                _exerciseError.postValue(R.string.retrieve_plan_error)
            }
    }

    fun validateBlockName(name: String) {
        blockName = name
        validateFields()
    }

    fun validateBlockDescription(description: String) {
        blockDescription = description
        validateFields()
    }

    fun validateBlockDuration(duration: String) {
        blockDuration = duration
        validateFields()
    }


    fun validateBlockUnitMeasure(unitMeasure: String) {
        blockUnitMeasure = unitMeasure
        validateFields()
    }

    fun validateBlockBackgroundImageUrl(url: String) {
        blockBackgroundImageUrl = url
        validateFields()
    }

    fun addExerciseToBlock(exercise: Exercise) {
        exerciseList.add(exercise)
        _selectedExercises.postValue(exerciseList)
        validateFields()
    }

    fun removeExerciseFromBlock(exercise: Exercise) {
        exerciseList.remove(exercise)
        _selectedExercises.postValue(exerciseList)
        validateFields()
    }

    fun updateExercise(exercise: Exercise, position: Int) {
        exerciseList[position] = exercise
        validateFields()
    }

    private fun validateFields() {
        _areValidFields.postValue(blockName.isNotEmpty() && blockDescription.isNotEmpty() && blockDuration.isNotEmpty() && blockUnitMeasure.isNotEmpty() && blockBackgroundImageUrl.isNotEmpty() && (exerciseList.isNotEmpty() && exerciseList.any { it.quantity.split(" ")[0].equals("0") }
            .not()))
    }

    fun createBlockObject() {
        _block.postValue(
            Block(
                blockDescription,
                blockUnitMeasure,
                blockDuration.toInt(),
                blockBackgroundImageUrl,
                blockName,
                exerciseList.toMutableList()
            )
        )
    }
}