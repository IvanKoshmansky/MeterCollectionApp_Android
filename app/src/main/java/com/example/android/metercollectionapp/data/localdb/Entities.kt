package com.example.android.metercollectionapp.data.localdb

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.metercollectionapp.SyncStatus

@Entity(tableName = "users_table")
data class DBUser constructor (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val login: String,
    @ColumnInfo(name = "pass_enc")
    val passEncrypted: String,
    @ColumnInfo(name = "user_name")
    val name: String,
    @ColumnInfo(name = "user_status")
    val status: SyncStatus
)

@Entity(tableName = "devices_table")
data class DBDevice constructor (
    @PrimaryKey
    val guid: Long,
    @ColumnInfo(name = "dev_type")
    val devType: Int,
    @ColumnInfo(name = "device_name")
    val name: String,
    val lat: Double,
    val lon: Double,
    @ColumnInfo(name = "device_status")
    val status: SyncStatus
)

@Entity(tableName = "params_table")
data class DBDeviceParam constructor (
    @PrimaryKey
    val uid: Long,
    @ColumnInfo(name = "param_type")
    val paramType: Int,
    val measUnit: String,
    @ColumnInfo(name = "param_name")
    val name: String,
    val shortName: String,
    @ColumnInfo(name = "param_status")
    val status: SyncStatus
)

@Entity(tableName = "collected_data")
data class DBCollectedData constructor (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "unix_time")
    val unixTime: Long,
    @ColumnInfo(name = "user_id")
    val userId: Long,
    @ColumnInfo(name = "device_id")
    val deviceId: Long,
    @ColumnInfo(name = "param_id")
    val paramId: Long,
    @ColumnInfo(name = "param_value")
    val paramValue: Float,
    @ColumnInfo(name = "collected_status")
    val status: SyncStatus
)

data class DBCollectedDataExtPOJO (
    @Embedded
    val base: DBCollectedData,
    @Embedded
    val device: DBDevice,
    @Embedded
    val param: DBDeviceParam
)
