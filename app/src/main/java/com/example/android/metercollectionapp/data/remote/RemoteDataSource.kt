package com.example.android.metercollectionapp.data.remote

import com.example.android.metercollectionapp.data.remote.entities.RemoteUser

interface RemoteDataSource {

    /**
     * @param remoteUser: логин и пароль в зашифрованном виде
     * @return: полный набор всех полей если пользователь успешно залогинился либо null если аутентификация не прошла
     */
    suspend fun authUser(remoteUser: RemoteUser): RemoteUser?
    suspend fun signOut()
}
