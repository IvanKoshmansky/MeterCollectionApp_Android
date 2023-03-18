package com.example.android.metercollectionapp.data.mappers

import com.example.android.metercollectionapp.data.localdb.DBCollectedData
import com.example.android.metercollectionapp.data.localdb.DBDevice
import com.example.android.metercollectionapp.data.localdb.DBDeviceParam
import com.example.android.metercollectionapp.data.localdb.DBUser
import com.example.android.metercollectionapp.domain.model.*

class ToDomainMapper {

    fun mapUser(dbUser: DBUser) = User(
        _id = dbUser.id,
        _login = dbUser.login,
        _passEncrypted = dbUser.passEncrypted,
        _name = dbUser.name,
        _status = dbUser.status
    )

    fun mapUsers(dbUsers: List<DBUser>) = dbUsers.map {
        mapUser(it)
    }

    fun mapDevice(dbDevice: DBDevice) = Device(
        _guid = dbDevice.guid,
        _devType = dbDevice.devType,
        _name = dbDevice.name,
        _lat = dbDevice.lat,
        _lon = dbDevice.lon,
        _status = dbDevice.status
    )

    fun mapDevices(dbDevices: List<DBDevice>) = dbDevices.map {
        mapDevice(it)
    }

    fun mapDeviceParam(dbDeviceParam: DBDeviceParam) = DeviceParam(
        _uid = dbDeviceParam.uid,
        _paramType = DeviceParamType.values()[dbDeviceParam.paramType],
        _measUnit = dbDeviceParam.measUnit,
        _name = dbDeviceParam.name,
        _shortName = dbDeviceParam.shortName,
        _status = dbDeviceParam.status
    )

    fun mapDeviceParams(dbDeviceParams: List<DBDeviceParam>) = dbDeviceParams.map {
        mapDeviceParam(it)
    }

    fun mapCollectedData(dbCollectedData: DBCollectedData) = CollectedData(
        _id = dbCollectedData.id,
        _unixTime = dbCollectedData.unixTime,
        _userId = dbCollectedData.userId,
        _deviceGuid = dbCollectedData.deviceId,
        _paramUid = dbCollectedData.paramId,
        _paramValue = dbCollectedData.paramValue,
        _status = dbCollectedData.status
    )

    fun mapCollectedDataList(dbCollectedDataList: List<DBCollectedData>) = dbCollectedDataList.map {
        mapCollectedData(it)
    }
}
