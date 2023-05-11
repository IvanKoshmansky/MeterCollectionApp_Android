package com.example.android.metercollectionapp.data.mappers

import com.example.android.metercollectionapp.data.localdb.*
import com.example.android.metercollectionapp.data.remote.entities.RemoteUser
import com.example.android.metercollectionapp.domain.model.*

class ToDomainMapper {

    fun mapUserFromDB(dbUser: DBUser) = User(
        _id = dbUser.id,
        _login = dbUser.login,
        _passEncrypted = dbUser.passEncrypted,
        _name = dbUser.name,
        _status = dbUser.status
    )

    fun mapUserFromRemote(remoteUser: RemoteUser) = User(
        _id = remoteUser.id,
        _login = remoteUser.login,
        _passEncrypted = remoteUser.passEncrypted,
        _name = remoteUser.name,
        _status = remoteUser.status
    )

    fun mapUsers(dbUsers: List<DBUser>) = dbUsers.map {
        mapUserFromDB(it)
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
        _paramType = DeviceParam.ParamType.values()[dbDeviceParam.paramType],
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

    fun mapCollectedDataExt(dbCollectedDataExtPOJO: DBCollectedDataExtPOJO) = CollectedDataExt(
        _id = dbCollectedDataExtPOJO.base.id,
        _unixTime = dbCollectedDataExtPOJO.base.unixTime,
        _userId = dbCollectedDataExtPOJO.base.userId,
        _deviceGuid = dbCollectedDataExtPOJO.base.deviceId,
        _paramUid = dbCollectedDataExtPOJO.base.paramId,
        _paramValue = dbCollectedDataExtPOJO.base.paramValue,
        _status = dbCollectedDataExtPOJO.base.status,
        _deviceInfo = mapDevice(dbCollectedDataExtPOJO.device),
        _paramInfo = mapDeviceParam(dbCollectedDataExtPOJO.param)
    )

    fun mapCollectedDataExtList(dbCollectedDataExtPOJOs: List<DBCollectedDataExtPOJO>) = dbCollectedDataExtPOJOs.map {
        mapCollectedDataExt(it)
    }
}
