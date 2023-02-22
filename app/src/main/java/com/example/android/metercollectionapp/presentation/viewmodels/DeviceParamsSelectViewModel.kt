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

    private val _deviceParamsSelectuiState = MutableLiveData(DeviceParamsSelectUiState(availableParamsLoading = true,
        selectedParamsLoading = true)
    )
    val deviceParamsSelectUiState: LiveData<DeviceParamsSelectUiState>
        get() = _deviceParamsSelectuiState

    private val _saveStatusUiState = MutableLiveData<DeviceParamsSelectSaveStatusUiState>()
    val saveStatusUiState: LiveData<DeviceParamsSelectSaveStatusUiState>
        get() = _saveStatusUiState
    // все-таки желательно делать один UiState посольку это улучшает тестируемость
    // т.е. в тестах будет подставляться только один объект
    // в случае постраничного вывода (ViewPaging) можно делить на отдельные части
    // но здесь в данном конкретном случае пусть будут раздельные объекты (возможный вариант с одним объектом в ветке debug)

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


    private suspend fun setupObjects() {
        var state = _objectsSelectUiState.value?.copy(isLoading = true, objects = emptyList())
        if (state != null) {
            // состояние загрузки
            _objectsSelectUiState.value = state
            val objectsFromRepo = repository.getAllDevices()
            if (objectsFromRepo.isNotEmpty()) {
                state = state.copy(isLoading = false, objects = objectsFromRepo.map {
                    ObjectUiState(it.guid, it.status, it.name)
                })
            } else {
                state = state.copy(isLoading = false, objects = emptyList())
            }
            // обновить состояние
            _objectsSelectUiState.value = state
        }
    }

    private suspend fun setupParamsForObjectGuid(guid: Long) {
        var state = _deviceParamsSelectuiState.value?.copy(
            availableParamsLoading = true, selectedParamsLoading = true, availableParams = emptyList(),
            selectedParams = emptyList()
        )
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
                state = state.copy(availableParamsLoading = false, availableParams = uiParamsList)
            } else {
                state = state.copy(availableParamsLoading = false, availableParams = emptyList())
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
                state = state.copy(selectedParamsLoading = false, selectedParams = uiParamsList)
            } else {
                state = state.copy(selectedParamsLoading = false, selectedParams = emptyList())
            }
            // обновить состояние
            _deviceParamsSelectuiState.value = state
        }
    }

    fun setupObjectsList() {
        viewModelScope.launch {
            setupObjects()
        }
    }

    private var lastPos = -1

    private fun setupParamsForObjectPosition(pos: Int, forced: Boolean) {
        val objects = _objectsSelectUiState.value?.objects
        if (!objects.isNullOrEmpty()) {
            if (pos < objects.size) {
                if ((pos != lastPos) || forced) {
                    lastPos = pos
                    val guid = objects[pos].uid
                    viewModelScope.launch {
                        setupParamsForObjectGuid(guid)
                    }
                }
            }
        }
    }

    fun setupParamsForObjectPos(pos: Int) {
        setupParamsForObjectPosition(pos, false)
    }

    // манипуляции с отображаемыми списками параметров
    private fun moveParams(from: ListSelector, all: Boolean) {
        val state = _deviceParamsSelectuiState.value
        if (state != null) {
            val fromAvailable = from == ListSelector.AVAILABLE
            val listFrom: MutableList<DeviceParamSelectUiState> =
                (if (fromAvailable) state.availableParams else state.selectedParams).toMutableList()
            val listTo: MutableList<DeviceParamSelectUiState> =
                (if (fromAvailable) state.selectedParams else state.availableParams).toMutableList()

            var index = listFrom.lastIndex
            while (index >= 0) {
                val element = listFrom[index]
                if (element.checked || all) {
                    listFrom.remove(element)
                    val modified = element.copy(checked = false, checkingLambda = { newState -> setCheckedUiAction(
                        if (fromAvailable) ListSelector.SELECTED else ListSelector.AVAILABLE, element.uid, newState) })
                    listTo.add(modified)
                }
                index--
            }
            // for и forEach создают итераторы и возможно исключение ConcurrentModificationException
            // при модификации исходного списка

            val newState = state.copy(
                availableParams = if (fromAvailable) listFrom else listTo,
                selectedParams = if (fromAvailable) listTo else listFrom
            )
            _deviceParamsSelectuiState.value = newState
        }
    }

    // обработчики нажатий на кнопки
    fun onAddSelected() {
        moveParams(ListSelector.AVAILABLE, false)
    }

    fun onAddAll() {
        moveParams(ListSelector.AVAILABLE, true)
    }

    fun onDeleteSelected() {
        moveParams(ListSelector.SELECTED, false)
    }

    fun onDeleteAll() {
        moveParams(ListSelector.SELECTED, true)
    }

    fun onSave() {
        val objects = _objectsSelectUiState.value?.objects
        if (!objects.isNullOrEmpty()) {
            val pos = selectedDeviceSpinnerPos.value
            if ((pos != null) && (pos >= 0) && (pos < objects.size)) {
                val guid = objects[pos].uid
                val associatedIds = _deviceParamsSelectuiState.value?.selectedParams?.map { it.uid }
                if (associatedIds != null) {
                    viewModelScope.launch {
                        repository.setDeviceParamsAssociatedTo(guid, associatedIds)
                        _saveStatusUiState.value = DeviceParamsSelectSaveStatusUiState(saveSuccess = true)
                    }
                }
            }
        } else {
            _saveStatusUiState.value = DeviceParamsSelectSaveStatusUiState(saveError = true)
        }
    }

    fun onCancel() {
        val pos = selectedDeviceSpinnerPos.value
        if ((pos != null) && (pos >= 0)) {
            setupParamsForObjectPosition(pos, true)
        }
    }
}
