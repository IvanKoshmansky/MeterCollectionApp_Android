package com.example.android.metercollectionapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.domain.model.DeviceParam
import com.example.android.metercollectionapp.presentation.uistate.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeviceParamsSelectViewModel @Inject constructor (private val repository: Repository) : ViewModel() {

    private enum class ListSelector {
        AVAILABLE,
        SELECTED
    }

    // переменные LiveData для двустороннего датабайндинга
    val selectedDeviceSpinnerPos = MutableLiveData(0)

    // UiState
    private val _objectsSelectUiState = MutableLiveData(ObjectSelectUiState(isLoading = true))
    val objectsSelectUiState: LiveData<ObjectSelectUiState>
        get() = _objectsSelectUiState

    private val _deviceParamsSelectuiState = MutableLiveData(DeviceParamsSelectUiState(
        availableParamsLoading = true, selectedParamsLoading = true)
    )
    val deviceParamsSelectUiState: LiveData<DeviceParamsSelectUiState>
        get() = _deviceParamsSelectuiState
    // все-таки желательно делать один посольку это улучшает тестируемость
    // т.е. в тестах будет подставляться только один объект
    // в случае постраничного вывода (ViewPaging) можно делить на отдельные части
    // но здесь в данном конкретном случае пусть будет два объекта

    // "UiAction"
    private fun setCheckedUiAction(listSelector: ListSelector, changedUid: Long, newState: Boolean) {
        val state = _deviceParamsSelectuiState.value
        if (state != null) {
            when (listSelector) {
                ListSelector.AVAILABLE -> {
                    _deviceParamsSelectuiState.value = state.copy(
                        availableParams = state.availableParams.map {
                            if (it.uid == changedUid) { it.copy(checked = newState) } else { it.copy() }
                        }
                    )
                }
                ListSelector.SELECTED -> {
                    _deviceParamsSelectuiState.value = state.copy(
                        selectedParams = state.selectedParams.map {
                            if (it.uid == changedUid) { it.copy(checked = newState) } else { it.copy() }
                        }
                    )
                }
            }
        }
    }

    // переменные для навигации

    /**
     * @return: возвращает GUID нулевого объекта в списке
     */
    private suspend fun setupObjects(): Long? {
        var result: Long? = null
        var state = _objectsSelectUiState.value?.copy(isLoading = true, isEmpty = false)
        if (state != null) {
            // состояние загрузки
            _objectsSelectUiState.value = state
            val objectsFromRepo = repository.getAllDevices()
            if (objectsFromRepo.isNotEmpty()) {
                state = state.copy(isLoading = false, isEmpty = false,
                    objects = objectsFromRepo.map { ObjectUiState(it.guid, it.status, it.name) }
                )
                result = objectsFromRepo[0].guid
            } else {
                state = state.copy(isLoading = false, isEmpty = true)
            }
            // обновить состояние
            _objectsSelectUiState.value = state
        }
        return result
    }

    private suspend fun setupParamsForObjectGuid(guid: Long) {
        var state = _deviceParamsSelectuiState.value?.copy(availableParamsLoading = true, selectedParamsLoading = true,
            availableParamsEmpty = false, selectedParamsEmpty = false)
        if (state != null) {
            // состояние загрузки
            _deviceParamsSelectuiState.value = state
            var paramsFromRepo: List<DeviceParam>
            var uiParamsList: List<DeviceParamSelectUiState>
            paramsFromRepo = repository.getDeviceParamsUnassociatedFrom(guid)
            if (paramsFromRepo.isNotEmpty()) {
                uiParamsList = paramsFromRepo.map {
                    DeviceParamSelectUiState(uid = it.uid, name = it.name,
                        checkingLambda = { newState ->
                            setCheckedUiAction(ListSelector.AVAILABLE, it.uid, newState)
                        }
                    )
                }
                state = state.copy(availableParamsLoading = false, availableParamsEmpty = false,
                    availableParams = uiParamsList)
            } else {
                state = state.copy(availableParamsLoading = false, availableParamsEmpty = true)
            }
            paramsFromRepo = repository.getDeviceParamsAssociatedFrom(guid)
            if (paramsFromRepo.isNotEmpty()) {
                uiParamsList = paramsFromRepo.map {
                    DeviceParamSelectUiState(uid = it.uid, name = it.name,
                        checkingLambda = { newState ->
                            setCheckedUiAction(ListSelector.SELECTED, it.uid, newState)
                        }
                    )
                }
                state = state.copy(selectedParamsLoading = false, selectedParamsEmpty = false,
                    selectedParams = uiParamsList)
            } else {
                state = state.copy(selectedParamsLoading = false, selectedParamsEmpty = true)
            }
            // обновить состояние
            _deviceParamsSelectuiState.value = state
        }
    }

    fun setupParamsForObjectPos(pos: Int) {
//        val currentObjectsList = _uiState.value!!.objects
//        val guid = currentObjectsList[pos].uid
//        viewModelScope.launch {
//            setupParamsForObjectGuid(guid)
//        }
    }

    fun setup() {
        viewModelScope.launch {
            val guid = setupObjects()
            if (guid != null) {
                setupParamsForObjectGuid(guid)
            }
        }
    }

    // манипуляции с отображаемыми списками параметров
    private fun moveParams(from: ListSelector, all: Boolean) {
//        val state = _uiState.value!!.copy(availableParamsChanged = false, selectedParamsChanged = false)
//        val fromAvailable = from == ListSelector.AVAILABLE
//        val listFrom: MutableList<DeviceParamSelectUiState> = (if (fromAvailable) state.availableParams else state.selectedParams).toMutableList()
//        val listTo: MutableList<DeviceParamSelectUiState> = (if (fromAvailable) state.selectedParams else state.availableParams).toMutableList()
//
//        var index = listFrom.lastIndex
//        while (index >= 0) {
//            val element = listFrom[index]
//            if (element.checked || all) {
//                listFrom.remove(element)
//                listTo.add(element.copy(checked = false))
//            }
//            index--
//        }
//        // for и forEach создают итераторы и возможно исключение ConcurrentModificationException
//
//        _uiState.value = state.copy(
//            availableParamsChanged = true, selectedParamsChanged = true,
//            availableParams = if (fromAvailable) listFrom else listTo,
//            selectedParams = if (!fromAvailable) listFrom else listTo
//        )
    }

}
