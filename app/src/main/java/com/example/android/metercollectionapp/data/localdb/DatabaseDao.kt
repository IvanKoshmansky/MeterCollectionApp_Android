package com.example.android.metercollectionapp.data.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DatabaseDao {

    // запросить список пользователей
    @Query("select * from users_table")
    fun getAllUsers(): List<DBUser>

    // запросить конкретного пользователя
    @Query("select * from users_table where id=:id")
    fun getUserById(id: Long): DBUser?

    // добавить нового пользователя
    // возвращается сгенерированное значение первичного ключа
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewUser(newUser: DBUser): Long

    // добавить новое устройство в БД
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewDevice(newDevice: DBDevice)

    // получить устройство по GUID или null если устройство не существует
    @Query("select * from devices_table where guid=:guid")
    fun getDeviceById(guid: Long): DBDevice?

    // получить список всех устройств
    @Query("select * from devices_table")
    fun getAllDevices(): List<DBDevice>

    // получить список всех параметров устройств
    @Query("select * from params_table")
    fun getAllDeviceParams(): List<DBDeviceParam>

    // получить список параметров устройств с требуемыми Id
    @Query("select * from params_table where uid in (:ids)")
    fun getDeviceParamsByParamsIds(ids: List<Long>): List<DBDeviceParam>

    // получить список всех параметров устройств исключая параметры в списке
    @Query ("select * from params_table where uid not in (:ids)")
    fun getDeviceParamsExcludingParamsIds(ids: List<Long>): List<DBDeviceParam>

    // добавить новый параметр устройств в локальную БД
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewDeviceParam(newDeviceParam: DBDeviceParam)

    // добавить новую строку собранных данных в локальную БД
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewCollectedDataRow(newCollectedData: DBCollectedData)
}
