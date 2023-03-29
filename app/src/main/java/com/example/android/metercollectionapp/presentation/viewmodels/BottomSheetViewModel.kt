package com.example.android.metercollectionapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.metercollectionapp.SyncStatus
import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.domain.UserManager
import com.example.android.metercollectionapp.presentation.uistate.SyncValuesCardUiState
import com.example.android.metercollectionapp.presentation.uistate.SyncValuesRowUiState
import com.example.android.metercollectionapp.presentation.uistate.SyncValuesUiState
import kotlinx.coroutines.launch
import javax.inject.Inject

class BottomSheetViewModel @Inject constructor (
    private val repository: Repository,
    private val userManager: UserManager
) : ViewModel() {

    private val _uiState = MutableLiveData(SyncValuesUiState())
    val uiState: LiveData<SyncValuesUiState>
        get() = _uiState

    fun setup() {
        var state = SyncValuesUiState(isLoading = true)
        _uiState.value = state
        viewModelScope.launch {
            val currentUser = userManager.currentUser
            if (currentUser != null) {
                state = state.copy(userLoggedIn = true, userName = currentUser.name)
                // val collectedData = repository.getCollectedData(currentUser.id)

                val rows1 = listOf(
                    SyncValuesRowUiState(0, SyncStatus.UNKNOWN, "Pвых", "25", "Вт"),
                    SyncValuesRowUiState(123, SyncStatus.UNKNOWN, "Uвых", "12", "В"),
                    SyncValuesRowUiState(777, SyncStatus.UNKNOWN, "Iвых", "5", "А"),
                    SyncValuesRowUiState(53453, SyncStatus.UNKNOWN, "Pвых", "25", "Вт"),
                    SyncValuesRowUiState(676, SyncStatus.UNKNOWN, "Uвых", "12", "В"),
                    SyncValuesRowUiState(75767, SyncStatus.UNKNOWN, "Iвых", "5", "А"),
                )
                val rows2 = listOf(
                    SyncValuesRowUiState(0, SyncStatus.UNKNOWN, "Pвых", "34", "Вт"),
                    SyncValuesRowUiState(123, SyncStatus.UNKNOWN, "Uвых", "15", "В"),
                    SyncValuesRowUiState(777, SyncStatus.UNKNOWN, "Iвых", "23", "А"),
                    SyncValuesRowUiState(53453, SyncStatus.UNKNOWN, "Pвых", "25", "Вт"),
                    SyncValuesRowUiState(676, SyncStatus.UNKNOWN, "Uвых", "12", "В"),
                    SyncValuesRowUiState(75767, SyncStatus.UNKNOWN, "Iвых", "5", "А"),
                )
                val cards = listOf(
                    SyncValuesCardUiState(333, "Сыктывкар 1", rows1),
                    SyncValuesCardUiState(444, "Новый Уренгой 5", rows2),
                    SyncValuesCardUiState(333, "Сыктывкар 1", rows1),
                    SyncValuesCardUiState(444, "Новый Уренгой 5", rows2)
                )
                state = SyncValuesUiState(cards, false, true, "В.Иванов")

                // state = state.copy(values = uiList, isLoading = false)
            } else {
                state = state.copy(userLoggedIn = false, isLoading = false)
            }
            _uiState.value = state
        }
    }

}
