package com.example.android.metercollectionapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.presentation.uistate.AddObjectUiState
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import javax.inject.Inject

class AddObjectViewModel @Inject constructor (private val repository: Repository) : ViewModel() {

    // для Databinding с полями ввода
    val nameLiveData = MutableLiveData("")
    val guidLiveData = MutableLiveData("")

    // UiState
    private val _newObjectUiState = MutableLiveData(AddObjectUiState())
    val addObjectUiState: LiveData<AddObjectUiState>
        get() = _newObjectUiState

    // переменные для навигации
    private val _navigateUp = MutableLiveData(false)
    val navigateUp: LiveData<Boolean>
        get() = _navigateUp

    // запустить камеру для сканирования QR кодов
    fun onScan() {

    }

    fun onSave() {
        val newName = nameLiveData.value!!
        if (newName.isNotEmpty()) {
            val newGuidString = guidLiveData.value!!
            if (newGuidString.isNotEmpty()) {
                try {
                    val newGuid = newGuidString.toLong()
                    viewModelScope.launch {
                        repository.addNewDeviceById(newName, newGuid)
                        _newObjectUiState.value = AddObjectUiState(success = true)
                        _navigateUp.value = true
                    }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    _newObjectUiState.value = AddObjectUiState(error = true)
                }
            } else {
                _newObjectUiState.value = AddObjectUiState(emptyGuid = true)
            }
        } else {
            _newObjectUiState.value = AddObjectUiState(emptyName = true)
        }
    }

    fun onCancel() {
        _navigateUp.value = true
    }

}
