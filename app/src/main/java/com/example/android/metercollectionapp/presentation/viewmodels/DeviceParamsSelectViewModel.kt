package com.example.android.metercollectionapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.domain.model.Device
import com.example.android.metercollectionapp.domain.usecase.GetDeviceParamsAssociatedFromUseCase
import com.example.android.metercollectionapp.domain.usecase.GetDeviceParamsUnassociatedFromUseCase
import com.example.android.metercollectionapp.presentation.uistate.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeviceParamsSelectViewModel @Inject constructor (
    private val repository: Repository,
    private val getDeviceParamsAssociatedFromUseCase: GetDeviceParamsAssociatedFromUseCase,
    private val getDeviceParamsUnassociatedFromUseCase: GetDeviceParamsUnassociatedFromUseCase
) : ViewModel() {

    // переменные LiveData для двустороннего датабайндинга
    val selectedDeviceSpinnerPos = MutableLiveData(0)

    // UiState
    // выбор объекта для редактирования
    private val _selectObjectUiState = MutableLiveData(SelectObjectUiState(listIsLoading = true))
    val selectObjectUiState: LiveData<SelectObjectUiState>
        get() = _selectObjectUiState

    // левый список
    private val _availableParamsUiState = MutableLiveData(DeviceParamsSelectUiState(isLoading = true))
    val availableParamsUiState: LiveData<DeviceParamsSelectUiState>
        get() = _availableParamsUiState

    // правый список
    private val _selectedParamsUiState = MutableLiveData(DeviceParamsSelectUiState(isLoading = true))
    val selectedParamsUiState: LiveData<DeviceParamsSelectUiState>
        get() = _selectedParamsUiState

    // внутренние переменные ViewModel
    private var listOfObjects = listOf<Device>()

    // переменные для навигации


    // загрузить оба списка параметров для объекта с данным guid
    private suspend fun setupParamsForObjectGuid(guid: Long) {
        _selectedParamsUiState.value = DeviceParamsSelectUiState(isLoading = true)
        _availableParamsUiState.value = DeviceParamsSelectUiState(isLoading = true)
        val associatedParams = getDeviceParamsAssociatedFromUseCase.execute(guid)
        if (associatedParams.isNotEmpty()) {
            // список присвоенных параметров (справа) не пустой
            val associatedParamsUiState = DeviceParamsSelectUiState(
                paramsUiState = associatedParams.map {
                    DeviceParamSelectUiState(it.uid, it.name, MutableLiveData(false))
                },
                isLoading = false,
                isEmpty = false
            )
            _selectedParamsUiState.value = associatedParamsUiState
        } else {
            // список присвоенных параметров пуст
            _selectedParamsUiState.value = DeviceParamsSelectUiState(isLoading = false, isEmpty = true)
        }
        // загрузить список всех доступных для данного устройства параметров
        val availableParams = getDeviceParamsUnassociatedFromUseCase.execute(guid)
        if (availableParams.isNotEmpty()) {
            // список доступных параметров (слева) не пустой
            val availableParamsUiState = DeviceParamsSelectUiState(
                paramsUiState = availableParams.map {
                    DeviceParamSelectUiState(it.uid, it.name, MutableLiveData(false))
                },
                isLoading = false,
                isEmpty = false
            )
            _availableParamsUiState.value = availableParamsUiState
        } else {
            // список доступных параметров пуст
            _availableParamsUiState.value = DeviceParamsSelectUiState(isLoading = false, isEmpty = true)
        }
    }

    // подгрузить списки параметров для устройства с позицией pos в списке
    fun setupParamsForObjectPos(pos: Int) {
        if (pos < listOfObjects.size) {
            val guid = listOfObjects[pos].guid
            viewModelScope.launch {
                setupParamsForObjectGuid(guid)
            }
        }
    }

    // выполнять один раз при создании ViewModel (при создании фрагмента без учета "пересозданий"
    // фрагмента при повороте экрана
    fun setupObjectSelector() {
        viewModelScope.launch {
            // подгрузить список устройств для выбора устройства
            listOfObjects = repository.getAllDevices()
            if (listOfObjects.isNotEmpty()) {
                // есть устройства
                val selectObjectState = SelectObjectUiState(
                    listOfObjects = listOfObjects.map {
                        ObjectUiState(it.guid, it.status, it.name)
                    },
                    listIsLoading = false,
                    listIsEmpty = false,
                    listChanged = true
                )
                _selectObjectUiState.value = selectObjectState
            } else {
                // в репозитории нет ни одного объекта
                _selectObjectUiState.value = SelectObjectUiState(listIsLoading = false, listIsEmpty = true)
            }
        }
    }

}



// если бы при перекидывании из левого списка в правый список требовался бы "нырок" репозиторий,
// то здесь требовался бы UseCase, который бы проводил все манипуляции с репозиторием
// но поскольку есть кнопка "Сохранить", то такой подход не нужен, достаточно просто репозитория
// UseCase нужен для вычитывания списка параметров (в "доменном" формате) которые ассоциированы с
// данным объектом
// тоже самое можно сделать и на уровне репозитория, но это усложняет репозиторий и переносит
// часть бизнес логики в него, а репозиторий должен быть максимально "тупым", это просто поставщик данных
