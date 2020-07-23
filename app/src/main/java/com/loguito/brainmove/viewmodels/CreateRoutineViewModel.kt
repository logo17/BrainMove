package com.loguito.brainmove.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loguito.brainmove.models.remote.Block
import com.loguito.brainmove.models.remote.Routine

class CreateRoutineViewModel : ViewModel() {
    private val blockList = mutableListOf<Block>()
    var routineName: String = ""
        set(value) {
            field = value
            validateFields()
        }

    var routineNumber: Int = 0
        set(value) {
            field = value
            validateFields()
        }

    var listIndex = -1

    private var _areValidFields = MutableLiveData<Boolean>()
    private var _blocks = MutableLiveData<List<Block>>()
    private var _routine = MutableLiveData<Pair<Int, Routine>>()
    private var _routineNameInput = MutableLiveData<String>()
    private var _routineNumberInput = MutableLiveData<Int>()

    val blocks: LiveData<List<Block>>
        get() = _blocks

    val areValidFields: LiveData<Boolean>
        get() = _areValidFields
    val routine: LiveData<Pair<Int, Routine>>
        get() = _routine
    val routineNameOutput: LiveData<String>
        get() = _routineNameInput
    val routineNumberOutput: LiveData<Int>
        get() = _routineNumberInput

    fun addBlockToList(index: Int, block: Block) {
        if (index != -1) {
            blockList[index] = block.copy()
        } else {
            blockList.add(block.copy())
        }
        _blocks.postValue(blockList)
        validateFields()
    }

    fun addBlocksToList(blocks: List<Block>) {
        blockList.addAll(blocks)
        _blocks.postValue(blockList)
        validateFields()
    }

    fun removeBlockFromList(index: Int) {
        blockList.removeAt(index)
        _blocks.postValue(blockList)
        validateFields()
    }

    private fun validateFields() {
        _areValidFields.postValue(routineName.isNotEmpty() && routineNumber != 0 && blockList.isNotEmpty())
    }

    fun createRoutineObject() {
        _routine.postValue(
            Pair(
                listIndex,
                Routine(
                    routineName,
                    routineNumber,
                    blockList.toMutableList()
                )
            )
        )
    }

    fun setRoutineNameInput(name: String) {
        _routineNameInput.postValue(name)
    }

    fun setRoutineNumberInput(routineNumber: Int) {
        _routineNumberInput.postValue(routineNumber)
    }
}