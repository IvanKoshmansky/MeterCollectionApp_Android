package com.example.android.metercollectionapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.presentation.uistate.*
import kotlinx.coroutines.launch
import java.lang.Float.NEGATIVE_INFINITY
import java.lang.Float.POSITIVE_INFINITY
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
    private val _navigateUp = MutableLiveData(false)
    val navigateUp: LiveData<Boolean>
        get() = _navigateUp

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
                DeviceParamUiState(
                    uid = it.uid,
                    paramType = it.paramType,
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
        var state = _uiState.value ?: return
        state = state.copy(shortMessage = WriteValuesUiState.ShortMessageCode.NOTHING_TO_SHOW)
        if (idx < state.deviceParams.params.size) {
            val shortName = state.deviceParams.params[idx].shortName
            val paramType = state.deviceParams.params[idx].paramType
            _uiState.value = state.copy(selectedParamShortName = shortName, selectedParamType = paramType)
            enteredParamValue.value = ""
        }
    }

    // callback который вызывается при удалении введенного значения с данным uid
    private fun deleteElementCallback(uid: Long) {
        var state = _uiState.value ?: return
        state = state.copy(shortMessage = WriteValuesUiState.ShortMessageCode.NOTHING_TO_SHOW)
        val elementToDelete = state.enteredValues.find { element -> element.uid == uid }
        if (elementToDelete != null) {
            val newList = state.enteredValues - elementToDelete
            _uiState.value = state.copy(enteredValues = newList)
        }
    }

    // в случае возникновения исключения возвращается null
    private fun textToFloat(text: String): Float? {
        var result: Float?
        try {
            result = text.toFloat()
            if ((result == POSITIVE_INFINITY) || (result == NEGATIVE_INFINITY)) {
                throw NumberFormatException()
            }
        } catch (e: NumberFormatException) {
            result = null
        }
        return result
    }

    // сформировать новое значение на сохранение в БД
    fun onWrite() {
        var state = _uiState.value ?: return
        state = state.copy(shortMessage = WriteValuesUiState.ShortMessageCode.NOTHING_TO_SHOW)
        val paramIdx = selectedParamIndex.value ?: return
        val enteredText = enteredParamValue.value ?: return
        if (textToFloat(enteredText) != null) {
            if (paramIdx < state.deviceParams.params.size) {
                val newElement = WriteValuesElementUiState(
                    uid = state.deviceParams.params[paramIdx].uid,
                    shortName = state.deviceParams.params[paramIdx].shortName,
                    stringValue = enteredText,
                    measUnit = state.deviceParams.params[paramIdx].measUnit,
                    deleteElementLambda = { uid -> deleteElementCallback(uid) }
                )
                val findDublicate = state.enteredValues.find { element -> element.uid == newElement.uid }
                if (findDublicate == null) {
                    // дубликатов нет
                    val newList = state.enteredValues + newElement  // получить список с добавленным элементом
                    _uiState.value = state.copy(enteredValues = newList)
                } else {
                    // значение параметра уже было введено!
                    _uiState.value = state.copy(
                        shortMessage = WriteValuesUiState.ShortMessageCode.ALREADY_ENTERED)
                }
            }
        } else {
            // ошибка преобразования во float!
            _uiState.value = state.copy(shortMessage = WriteValuesUiState.ShortMessageCode.CONVERSION_ERROR)
        }
    }

    fun onSave() {
        var state = _uiState.value ?: return
        state = state.copy(shortMessage = WriteValuesUiState.ShortMessageCode.NOTHING_TO_SHOW)
        if (state.enteredValues.isNotEmpty()) {
            viewModelScope.launch {
                var cnt = 0
                state.enteredValues.forEach { element ->
                    val value = textToFloat(element.stringValue)
                    if (value != null) {
                        repository.addNewCollectedDataRow(
                            newRowUserId = 0,  // как определяется userId?
                            newRowDeviceGuid = state.objectUiState.uid,
                            newRowParamUid = element.uid,
                            newRowParamValue = value
                        )
                        cnt++
                    }
                }
                if (cnt > 0) {
                    _uiState.value = state.copy(
                        shortMessage = WriteValuesUiState.ShortMessageCode.SAVE_SUCCESS)
                    _navigateUp.value = true
                }
            }
        } else {
            // список данных для добавления в локальную БД пуст!
            _uiState.value = state.copy(
                shortMessage = WriteValuesUiState.ShortMessageCode.ENTERED_VALUES_EMPTY)
        }
    }

    fun onCancel() {
        _navigateUp.value = true
    }
}

// все значения в локальной БД сохраняются в формате float, реализована только проверка преобразования во float
// можно сделать проверку на возможность преобразования введенного текста в требуемую разрядность и представление
// согласно типу данных
// все манипуляции со списком введенных параметров полностью на стороне ViewModel
// кэш для сохранения уже введенных значений (в неком своем формате) при уходе с фрагмента для данного устройства
// можно реализовать в репозитории (при необходимости)
// по умолчанию при уходе с фрагмента ранее набранные значения не сохраняются
