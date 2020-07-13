package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loguito.brainmove.models.remote.Block
import com.loguito.brainmove.models.remote.Routine

class CreateRoutineViewModel : ViewModel() {
    private val blockList = mutableListOf<Block>()
    private var routineName: String = ""
    private var routineNumber: Int = 0

    private var _areValidFields = MutableLiveData<Boolean>()
    private var _blocks = MutableLiveData<List<Block>>()
    private var _routine = MutableLiveData<Routine>()

    val blocks: LiveData<List<Block>>
        get() = _blocks

    val areValidFields: LiveData<Boolean>
        get() = _areValidFields
    val routine: LiveData<Routine>
        get() = _routine

    fun addBlockToList(block: Block) {
        blockList.add(block.copy())
        _blocks.postValue(blockList)
    }

    fun validateRoutineName(name: String) {
        routineName = name
        validateFields()
    }

    fun validateRoutineNumber(number: Int) {
        routineNumber = number
        validateFields()
    }

    private fun validateFields() {
        _areValidFields.postValue(routineName.isNotEmpty() && routineNumber != 0 && blockList.isNotEmpty())
    }

    fun createRoutineObject() {
        _routine.postValue(
            Routine(
                routineName,
                routineNumber,
                blockList.toMutableList()
            )
        )
    }
}