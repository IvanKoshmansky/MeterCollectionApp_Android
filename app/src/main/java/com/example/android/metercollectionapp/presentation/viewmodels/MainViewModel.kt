package com.example.android.metercollectionapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.metercollectionapp.domain.Repository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.metercollectionapp.domain.UserManager
import com.example.android.metercollectionapp.presentation.uistate.UserUiState
import com.example.android.metercollectionapp.presentation.uistate.UsersUiState
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor (
    private val repository: Repository,
    private val userManager: UserManager
) : ViewModel() {

    private val _uiState = MutableLiveData(UsersUiState(isLoading = true))
    val uiState: LiveData<UsersUiState>
        get() = _uiState

    fun setup() {
        viewModelScope.launch {
            val users = repository.getAllUsers()
            if (users.isNotEmpty()) {
                val uiUsers = users.map {
                    UserUiState(id = it.id, status = it.status, name = it.name)
                }
                _uiState.value = UsersUiState(users = uiUsers, isLoading = false)
            } else {
                _uiState.value = UsersUiState(users = emptyList(), isLoading = false)
            }
        }
    }

}
