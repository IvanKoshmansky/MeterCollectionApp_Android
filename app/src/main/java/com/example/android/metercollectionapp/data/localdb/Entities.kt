package com.example.android.metercollectionapp.data.localdb

import androidx.room.ColumnInfo
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
    val name: String,
    val status: SyncStatus
)

@Entity(tableName = "devices_table")
data class DBDevice constructor (
    @PrimaryKey
    val guid: Long,
    @ColumnInfo(name = "dev_type")
    val devType: Int,
    val name: String,
    val lat: Double,
    val lon: Double,
    val status: SyncStatus
)

@Entity(tableName = "params_table")
data class DBDeviceParam constructor (
    @PrimaryKey
    val uid: Long,
    @ColumnInfo(name = "param_type")
    val paramType: Int,
    val measUnit: String,
    val name: String,
    val shortName: String,
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
    val status: SyncStatus
)
