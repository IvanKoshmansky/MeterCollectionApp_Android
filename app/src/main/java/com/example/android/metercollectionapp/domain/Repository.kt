package com.example.android.metercollectionapp.domain

import com.example.android.metercollectionapp.domain.model.Device
import com.example.android.metercollectionapp.domain.model.DeviceParam
import com.example.android.metercollectionapp.domain.model.User

interface Repository {
    suspend fun getAllUsers(): List<User>

    /**
     * Throws IOException если пользователь не существует
     */
    suspend fun getUserById(id: Long): User

    /**
     * @param user: заполняется без id, репозиторий сам назначает id для нового пользователя
     * @return id нового пользователя, который был добавлен в базу и затем будет попытка его авторизовать на сервере
     */
    suspend fun addNewUser(user: User): Long

    /**
     * Throws IOException
     */
    suspend fun getDeviceById(guid: Long): Device
    suspend fun addNewDeviceById(name: String, guid: Long)
    suspend fun getAllDevices(): List<Device>

    suspend fun getAllDeviceParams(): List<DeviceParam>
    suspend fun addNewDeviceParam(deviceParam: DeviceParam)
    suspend fun getDeviceParamsAssociatedFrom(guid: Long): List<DeviceParam>
    suspend fun getDeviceParamsUnassociatedFrom(guid: Long): List<DeviceParam>

    suspend fun sync() // будет вызываться переодически из WorkManager, синхронизация по цепочке начиная с пользователей

}
