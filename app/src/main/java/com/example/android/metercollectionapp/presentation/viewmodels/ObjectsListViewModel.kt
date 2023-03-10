package com.example.android.metercollectionapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.presentation.uistate.ObjectUiState
import com.example.android.metercollectionapp.presentation.uistate.ObjectsUiState
import kotlinx.coroutines.launch
import javax.inject.Inject

class ObjectsListViewModel @Inject constructor (private val repository: Repository) : ViewModel() {

    private val _uiState = MutableLiveData(ObjectsUiState(isLoading = true))
    val uiState: LiveData<ObjectsUiState>
        get() = _uiState

    private val shadowObjectsUiState = mutableListOf<ObjectUiState>()

    fun setup() {
        _uiState.value = ObjectsUiState(isLoading = true)
        shadowObjectsUiState.clear()
        viewModelScope.launch {
            val fromRepo = repository.getAllDevices()
            if (fromRepo.isNotEmpty()) {
                val viewObjectsUiState = mutableListOf<ObjectUiState>()
                fromRepo.forEach {
                    shadowObjectsUiState.add(
                        ObjectUiState(uid = it.guid, status = it.status, name = it.name)
                    )
                    viewObjectsUiState.add(
                        ObjectUiState(uid = it.guid, status = it.status, name = it.name)
                    )
                }
                _uiState.value = ObjectsUiState(objects = viewObjectsUiState, isLoading = false)
            } else {
                _uiState.value = ObjectsUiState(objects = emptyList(), isLoading = false)
            }
        }
    }

    fun filtered(pattern: String) {
        if (pattern.isNotEmpty()) {
            val newViewList = shadowObjectsUiState.filter { it.name.contains(pattern) }
            if (newViewList.isNotEmpty()) {
                _uiState.value = ObjectsUiState(objects = newViewList, isLoading = false)
            } else {
                _uiState.value = ObjectsUiState(objects = emptyList(), isLoading = false)
            }
        } else {
            _uiState.value = ObjectsUiState(objects = shadowObjectsUiState, isLoading = false)
        }
    }

    private val _navigateToAdd = MutableLiveData(false)
    val navigateToAdd: LiveData<Boolean>
        get() = _navigateToAdd

    fun navigateToAddDone() {
        _navigateToAdd.value = false
    }

    fun onAdd() {
        _navigateToAdd.value = true
    }

}
