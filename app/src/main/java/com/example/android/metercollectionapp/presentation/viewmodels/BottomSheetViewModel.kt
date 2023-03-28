package com.example.android.metercollectionapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.domain.UserManager
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
                // TODO: проверить для начала на подстановочных данных

                // state = state.copy(values = uiList, isLoading = false)
            } else {
                state = state.copy(userLoggedIn = false, isLoading = false)
            }
            _uiState.value = state
        }
    }

}
