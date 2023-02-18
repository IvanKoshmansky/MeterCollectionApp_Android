package com.example.android.metercollectionapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.metercollectionapp.SyncStatus
import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.domain.model.DeviceParam
import com.example.android.metercollectionapp.domain.model.DeviceParamType
import com.example.android.metercollectionapp.presentation.uistate.AddDeviceParamUiState
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import javax.inject.Inject

class AddDeviceParamViewModel @Inject constructor (private val repository: Repository) : ViewModel() {

    // для Databinding с полями ввода
    val uidLiveData = MutableLiveData("")
    val nameLiveData = MutableLiveData("")
    val shortNameLiveData = MutableLiveData("")
    val measUnitLiveData = MutableLiveData("")
    val dataTypeLiveData = MutableLiveData(DeviceParamType.FLOAT.ordinal)  // ordinal порядковый номер начиная с нуля
    // для позиции спиннера есть готовый Two-Way биндинг адаптер
    // такой же адаптер есть и для date-time piker'ов

    // UiState
    private val _addParamUiState = MutableLiveData(AddDeviceParamUiState())
    val addParamUiState: LiveData<AddDeviceParamUiState>
        get() = _addParamUiState

    // переменные для навигации
    private val _navigateUp = MutableLiveData(false)
    val navigateUp: LiveData<Boolean>
        get() = _navigateUp

    fun onSave() {
        // все поля кроме единицы измерения заполнены
        // иначе показать статус "все поля должны быть заполнены"
        if ((uidLiveData.value!!.isNotEmpty()) &&
            (nameLiveData.value!!.isNotEmpty()) &&
            (shortNameLiveData.value!!.isNotEmpty())) {
            // сохранить в БД
            try {
                val uid = uidLiveData.value!!.toLong()
                val dataType = DeviceParamType.values()[dataTypeLiveData.value!!]
                val newDeviceParam = DeviceParam(
                    uid,
                    dataType,
                    measUnitLiveData.value!!,
                    nameLiveData.value!!,
                    shortNameLiveData.value!!,
                    SyncStatus.UNKNOWN
                )
                viewModelScope.launch {
                    repository.addNewDeviceParam(newDeviceParam)
                    // показать что все успешно
                    _addParamUiState.value = AddDeviceParamUiState(success = true)
                    // перейти по навигации на предыдущий фрагмент
                    _navigateUp.value = true
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                // показать ошибку "ошибка при заполнении uid"
                _addParamUiState.value = AddDeviceParamUiState(error = true)
            }
        } else {
            // не все поля заполнены
            _addParamUiState.value = AddDeviceParamUiState(emptyFields = true)
        }
    }

    fun onCancel() {
        _navigateUp.value = true
    }

}
