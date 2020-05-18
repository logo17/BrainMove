package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.loguito.brainmove.R
import com.loguito.brainmove.models.remote.Block
import com.loguito.brainmove.models.remote.Exercise
import java.util.*

class CreateBlockViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private var blockName: String = ""
    private var blockDescription: String = ""
    private var blockDuration: String = ""
    private var blockBackgroundImageUrl: String = ""
    private var exerciseList = mutableListOf<Exercise>()

    private var _exercises = MutableLiveData<List<Exercise>>()
    private var _block = MutableLiveData<Block>()
    private var _selectedExercises = MutableLiveData<List<Exercise>>()
    private var _exerciseError = MutableLiveData<Int>()
    private var _loadingVisibility = MutableLiveData<Boolean>()
    private var _areValidFields = MutableLiveData<Boolean>()

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
        _areValidFields.postValue(blockName.isNotEmpty() && blockDescription.isNotEmpty() && blockDuration.isNotEmpty() && blockBackgroundImageUrl.isNotEmpty() && (exerciseList.isNotEmpty() && exerciseList.any { it.quantity.isEmpty() }
            .not()))
    }

    fun createBlockObject() {
        _block.postValue(
            Block(
                blockDescription,
                "",//TODO Hanlde this on Admin
                blockDuration.toInt(),
                blockBackgroundImageUrl,
                blockName,
                exerciseList.toMutableList()
            )
        )
    }
}