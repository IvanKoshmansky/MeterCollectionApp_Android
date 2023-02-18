package com.example.android.metercollectionapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.metercollectionapp.SyncStatus
import com.example.android.metercollectionapp.domain.Status
import com.example.android.metercollectionapp.domain.UserManager
import com.example.android.metercollectionapp.presentation.uistate.LoginUiState
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor (private val userManager: UserManager) : ViewModel() {

    private var isNewUser = false
    private var userId = 0L

    private val _loginUiState = MutableLiveData(LoginUiState())
    val loginUiState: LiveData<LoginUiState>
        get() = _loginUiState

    val loginTextLiveData = MutableLiveData("")
    val passTextLiveData = MutableLiveData("")

    private val _navigationNextLiveData = MutableLiveData(false)
    val navigationNextLiveData: LiveData<Boolean>
        get() = _navigationNextLiveData

    fun navigationNextDone() {
        _navigationNextLiveData.value = false
    }

    fun setup(isNewUser: Boolean, userId: Long) {
        this.isNewUser = isNewUser
        this.userId = userId
        if (isNewUser) {
            _loginUiState.value = LoginUiState(isNewUser = true, syncStatus = SyncStatus.UNKNOWN)
        } else {
            viewModelScope.launch {
                val user = userManager.getUserById(userId)
                user?.let {
                    _loginUiState.value = LoginUiState(
                        isNewUser = false,
                        name = it.name,
                        syncStatus = it.status
                    )
                }
            }
        }
    }

    fun onNext() {
        viewModelScope.launch {
            if (isNewUser) {
                userManager.addNewUser(loginTextLiveData.value!!, passTextLiveData.value!!)
                _navigationNextLiveData.value = true
            } else {
                val status = userManager.selectUserById(userId, loginTextLiveData.value!!, passTextLiveData.value!!)
                if (status == Status.OK) {
                    _navigationNextLiveData.value = true
                } else {
                    val newState = _loginUiState.value!!.copy(loginError = true)
                    _loginUiState.value = newState
                }
            }
        }
    }

}

// либо заменить текущий "логинный" фрагмент в стеке на следующий (а не просто пройти по навигации)
// либо логинный обозначить так чтобы при движении назад он пропускался (что наверное проще)
// еще нужно на фрагменте авторизации в ActionBar менять заголовок в зависимости от того с
// каким "интентом" открывается фрагмент
// на текущий момент просто сделать навигацию назад
