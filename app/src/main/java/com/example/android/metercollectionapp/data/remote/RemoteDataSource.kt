package com.example.android.metercollectionapp.data.remote

import com.example.android.metercollectionapp.data.remote.datatransferobjects.RemoteDevice
import com.example.android.metercollectionapp.data.remote.datatransferobjects.RemoteDeviceParam
import com.example.android.metercollectionapp.data.remote.datatransferobjects.RemoteUser

interface RemoteDataSource {

    /**
     * @param remoteUser: логин и пароль в зашифрованном виде
     * @return: полный набор всех полей если пользователь успешно залогинился либо null если аутентификация не прошла
     */
    suspend fun authUser(remoteUser: RemoteUser): RemoteUser?
    suspend fun signOut()

    suspend fun syncDevices(remoteDevices: List<RemoteDevice>): List<RemoteDevice>
    suspend fun syncDeviceParams(remoteDeviceParams: List<RemoteDeviceParam>): List<RemoteDeviceParam>

    // синхронизация собранных данных
    // upload...
}
