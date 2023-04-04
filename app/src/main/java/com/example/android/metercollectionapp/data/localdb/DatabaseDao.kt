package com.example.android.metercollectionapp.data.localdb

import androidx.room.*
import com.example.android.metercollectionapp.SyncStatus

@Dao
interface DatabaseDao {

    // запросить список пользователей
    @Query("SELECT * FROM users_table")
    fun getAllUsers(): List<DBUser>

    // запросить конкретного пользователя
    @Query("SELECT * FROM users_table WHERE id=:id")
    fun getUserById(id: Long): DBUser?

    // добавить нового пользователя
    // возвращается сгенерированное значение первичного ключа
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewUser(newUser: DBUser): Long

    // добавить новое устройство в БД
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewDevice(newDevice: DBDevice)

    // получить устройство по GUID или null если устройство не существует
    @Query("SELECT * FROM devices_table WHERE guid=:guid")
    fun getDeviceById(guid: Long): DBDevice?

    // получить список всех устройств
    @Query("SELECT * FROM devices_table")
    fun getAllDevices(): List<DBDevice>

    // получить список всех параметров устройств
    @Query("SELECT * FROM params_table")
    fun getAllDeviceParams(): List<DBDeviceParam>

    // получить список параметров устройств с требуемыми Id
    @Query("SELECT * FROM params_table WHERE uid IN (:ids)")
    fun getDeviceParamsByParamsIds(ids: List<Long>): List<DBDeviceParam>

    // получить список всех параметров устройств исключая параметры в списке
    @Query ("SELECT * FROM params_table WHERE uid NOT IN (:ids)")
    fun getDeviceParamsExcludingParamsIds(ids: List<Long>): List<DBDeviceParam>

    // добавить новый параметр устройств в локальную БД
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewDeviceParam(newDeviceParam: DBDeviceParam)

    // добавить новую строку собранных данных в локальную БД
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewCollectedDataRow(newCollectedData: DBCollectedData)

    // получить строку собранных данных в расширенном формате
    @Query("""
        SELECT * FROM collected_data
        INNER JOIN devices_table ON devices_table.guid = collected_data.device_id
        INNER JOIN params_table ON params_table.uid = collected_data.param_id
        WHERE collected_data.user_id == :userId
    """)
    fun getCollectedDataExtPOJOs(userId: Long): List<DBCollectedDataExtPOJO>
    // You can use a raw string which is more readable anyway

    // обновить статус данных в таблице collected_data
    @Query("UPDATE collected_data SET collected_status = :newStatus WHERE user_id = :userId")
    fun updateCollectedDataStatus(userId: Long, newStatus: SyncStatus)

    @Query("DELETE FROM collected_data WHERE collected_status = :statusToDelete AND user_id != :userId")
    fun deleteCollectedDataExceptUser(userId: Long?, statusToDelete: SyncStatus)
}

// @Relation требует явное статическое связывание через поля в обоих @Entity, отношение "один к одному" или
// "один ко многим", не всегда это бывает удобно для реализации, особенно связь "многие ко многим",
// поэтому в данном приложении таблицы не связаны, а сложные запросы из нескольких таблиц делаются через INNER JOIN
