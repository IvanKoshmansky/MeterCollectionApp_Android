package com.example.android.metercollectionapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.presentation.uistate.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class WriteValuesViewModel @Inject constructor (private val repository: Repository) : ViewModel() {

    // UiState
    private val _uiState = MutableLiveData(WriteValuesUiState())
    val uiState: LiveData<WriteValuesUiState>
        get() = _uiState

    // переменные для двустороннего датабайндинга
    // позиция выбранного параметра в списке параметров
    val selectedParamIndex = MutableLiveData(0)

    // введенное текстовое значение параметра
    val enteredParamValue = MutableLiveData("")

    // переменные для навигации


    // установка начального состояния отображения экрана
    fun setup(objectGuid: Long, objectName: String) {
        // имя объекта + статус загрузки параметров объекта
        var state = WriteValuesUiState(
            objectUiState = ObjectUiState(uid = objectGuid, name = objectName),
            deviceParams = ShortDeviceParamsUiState(isLoading = true)
        )
        _uiState.value = state
        // загрузка списка параметров для объекта
        viewModelScope.launch {
            val paramsFromRepo = repository.getDeviceParamsAssociatedFrom(objectGuid)
            val uiParams = paramsFromRepo.map {
                ShortDeviceParamUiState(
                    uid = it.uid,
                    measUnit = it.measUnit,
                    name = it.name,
                    shortName = it.shortName
                )
            }
            // список параметров загружен
            state = state.copy(deviceParams = ShortDeviceParamsUiState(isLoading = false, params = uiParams))
            _uiState.value = state
        }
    }

    // выбор параметра из списка параметров по индексу
    fun selectParamByIndex(idx: Int) {
        val state = _uiState.value ?: return
        if (idx < state.deviceParams.params.size) {
            val shortName = state.deviceParams.params[idx].shortName
            _uiState.value = state.copy(selectedParamShortName = shortName)
            enteredParamValue.value = ""
        }
    }

    private fun deleteElementCallback(uid: Long) {
        val state = _uiState.value ?: return
        val elementToDelete = state.enteredValues.find { element -> element.uid == uid }
        if (elementToDelete != null) {
            val newList = state.enteredValues - elementToDelete
            val newState = state.copy(enteredValues = newList)
            _uiState.value = newState
        }
    }

    // сформировать новое значение на сохранение в БД
    fun onWrite() {
        val state = _uiState.value ?: return
        val paramIdx = selectedParamIndex.value ?: return
        val enteredText = enteredParamValue.value ?: return
        if (paramIdx < state.deviceParams.params.size) {
            val valueFormatted = enteredText
            val newElement = WriteValuesElementUiState(
                uid = state.deviceParams.params[paramIdx].uid,
                shortName = state.deviceParams.params[paramIdx].shortName,
                stringValue = valueFormatted,
                measUnit = state.deviceParams.params[paramIdx].measUnit,
                deleteElementLambda = { uid -> deleteElementCallback(uid) }
            )
            val findDublicate = state.enteredValues.find { element -> element.uid == newElement.uid }
            if (findDublicate == null) {
                // дубликатов нет
                val newList = state.enteredValues + newElement  // получить список с добавленным элементом
                val newState = state.copy(enteredValues = newList)
                _uiState.value = newState
            } else {
                // значение параметра уже было введено!

            }
        }
    }
}

// все манипуляции со списком введенных параметров полностью на стороне ViewModel
// кэш для сохранения уже введенных значений (в неком своем формате) при уходе с фрагмента для данного устройства
// можно реализовать в репозитории (при необходимости)
// по умолчанию при уходе с фрагмента ранее набранные значения не сохраняются
