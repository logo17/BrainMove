package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.loguito.brainmove.R
import com.loguito.brainmove.models.remote.Block
import com.loguito.brainmove.models.remote.BlockImage
import com.loguito.brainmove.models.remote.Exercise
import com.loguito.brainmove.models.remote.UnitMeasure

class CreateBlockViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    var listIndex = -1

    var blockName: String = ""
        set(value) {
            field = value
            validateFields()
        }

    var blockDescription: String = ""
        set(value) {
            field = value
            validateFields()
        }

    var blockDuration: String = ""
        set(value) {
            field = value
            validateFields()
        }

    var blockUnitMeasure: String = ""
        set(value) {
            field = value
            validateFields()
        }

    var blockBackgroundImageUrl: String = ""
        set(value) {
            field = value
            validateFields()
        }

    private var exerciseList = mutableListOf<Exercise>()
    var unitMeasures = mutableListOf<UnitMeasure>()

    // Inputs
    private var _blockNameInput = MutableLiveData<String>()
    private var _blockDescriptionInput = MutableLiveData<String>()
    private var _blockDurationInput = MutableLiveData<String>()

    private var _exercises = MutableLiveData<List<Exercise>>()
    private var _block = MutableLiveData<Pair<Int, Block>>()
    private var _selectedExercises = MutableLiveData<List<Exercise>>()
    private var _exerciseError = MutableLiveData<Int>()
    private var _loadingVisibility = MutableLiveData<Boolean>()
    private var _areValidFields = MutableLiveData<Boolean>()
    private var _blockUnitMeasures = MutableLiveData<List<UnitMeasure>>()
    private var _blockImageList = MutableLiveData<List<BlockImage>>()

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
    val block: LiveData<Pair<Int, Block>>
        get() = _block
    val blockUnitMeasures: LiveData<List<UnitMeasure>>
        get() = _blockUnitMeasures
    val blockImageList: LiveData<List<BlockImage>>
        get() = _blockImageList

    val blockNameOutput: LiveData<String>
        get() = _blockNameInput
    val blockDescriptionOutput: LiveData<String>
        get() = _blockDescriptionInput
    val blockDurationOutput: LiveData<String>
        get() = _blockDurationInput

    init {
        _loadingVisibility.postValue(true)
        db.collection("exercise")
            .orderBy("name", Query.Direction.ASCENDING)
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
                        if (!result.isEmpty) {
                            unitMeasures = result.toObjects(UnitMeasure::class.java)
                        }
                    }
                    .addOnFailureListener {
                        _exerciseError.postValue(R.string.retrieve_workout_measure_error)
                    }
            }
            .addOnFailureListener {
                _exerciseError.postValue(R.string.retrieve_block_measure_error)
            }

        db.collection("block_images")
            .orderBy("name", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                _blockImageList.postValue(result.toObjects(BlockImage::class.java))
            }
            .addOnFailureListener {
                _blockImageList.postValue(emptyList())
            }
    }

    fun addExerciseToBlock(exercise: Exercise) {
        exerciseList.add(exercise)
        _selectedExercises.postValue(exerciseList)
        validateFields()
    }

    fun addExercisesToBlock(exercises: List<Exercise>) {
        exerciseList.addAll(exercises)
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
        _areValidFields.postValue(blockName.isNotEmpty() && blockDuration.isNotEmpty() && blockUnitMeasure.isNotEmpty() && blockBackgroundImageUrl.isNotEmpty() && (exerciseList.isNotEmpty() && exerciseList.any {
            it.quantity.split(
                " "
            )[0].equals("0")
        }
            .not()))
    }

    fun setBlockNameInput(name: String) {
        _blockNameInput.postValue(name)
    }

    fun setBlockDescriptionInput(description: String) {
        _blockDescriptionInput.postValue(description)
    }

    fun setBlockDurationInput(duration: String) {
        _blockDurationInput.postValue(duration)
    }

    fun createBlockObject() {
        _block.postValue(
            Pair(
                listIndex,
                Block(
                    blockDescription,
                    blockUnitMeasure,
                    blockDuration.toInt(),
                    blockBackgroundImageUrl,
                    blockName,
                    exerciseList.toMutableList()
                )
            )
        )
    }
}