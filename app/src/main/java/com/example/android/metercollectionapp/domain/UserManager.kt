package com.example.android.metercollectionapp.domain

import com.example.android.metercollectionapp.SyncStatus
import com.example.android.metercollectionapp.domain.model.User
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor (private val repository: Repository) {

    private var _currentUser: User? = null
    val currentUser: User?
        get() = _currentUser

    suspend fun addNewUser(newLogin: String, newPass: String) {
        val newUser = User().apply {
            login = newLogin
            passEncrypted = encryptPass(newLogin, newPass)
            name = login  // до синхронизации
            status = SyncStatus.UNKNOWN
        }

        val newId = repository.addNewUser(newUser)
        newUser.id = newId
        _currentUser = newUser
    }

    suspend fun getUserById(id: Long): User? {
        return try {
            repository.getUserById(id)
        } catch (e: IOException) {
            null
        }
    }

    /**
     * @return Status.OK, Status.NOT_FOUND, Status.AUTH_ERROR
     */
    suspend fun selectUserById(id: Long, login: String, pass: String): Status {
        var status: Status
        try {
            val user = repository.getUserById(id)
            val passEncrypted = encryptPass(login, pass)
            _currentUser = if (user.passEncrypted == passEncrypted) {
                status = Status.OK
                user
            } else {
                status = Status.AUTH_ERROR
                null
            }
        } catch (e: IOException) {
            status = Status.NOT_FOUND
            _currentUser = null
        }
        return status
    }

    fun deselectCurrentUser() {
        _currentUser = null
    }

}
