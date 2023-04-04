package com.example.android.metercollectionapp.domain

import com.example.android.metercollectionapp.domain.model.*

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
    suspend fun setDeviceParamsAssociatedTo(guid: Long, ids: List<Long>)

    // добавить новую строчку в список данных для синхронизации, дата-время генерируется автоматически внутри
    suspend fun addNewCollectedDataRow(newRowUserId: Long, newRowDeviceGuid: Long, newRowParamUid: Long,
                                       newRowParamValue: Float)

    // получить собранные (и переданные) данные для данного пользователя
    suspend fun getCollectedData(userId: Long): List<CollectedDataExt>

    // общая синхронизация с сервером
    suspend fun sync(loggedUser: User?)

    // очистить строки в таблице с переданными данными для всех пользователей кроме exceptUser или полностью
    // если exceptUser == null
    suspend fun cleanUpUploaded(exceptUser: User?)
}

// для соблюдения принципа сегрегации интерфейсов (ISP) SOLID лучше сделать несколько однотипных методов
// с разными сигнатурами под конкретные нужды клиентского кода, чтобы не было ситуации "как с TextWatcher"
