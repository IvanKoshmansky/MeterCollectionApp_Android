package com.example.android.metercollectionapp.data.mappers

import com.example.android.metercollectionapp.data.localdb.*
import com.example.android.metercollectionapp.data.remote.datatransferobjects.RemoteDevice
import com.example.android.metercollectionapp.data.remote.datatransferobjects.RemoteDeviceParam
import com.example.android.metercollectionapp.data.remote.datatransferobjects.RemoteUser
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

    fun mapUsersFromDB(dbUsers: List<DBUser>) = dbUsers.map {
        mapUserFromDB(it)
    }

    fun mapDeviceFromDB(dbDevice: DBDevice) = Device(
        _guid = dbDevice.guid,
        _devType = dbDevice.devType,
        _name = dbDevice.name,
        _lat = dbDevice.lat,
        _lon = dbDevice.lon,
        _status = dbDevice.status
    )

    fun mapDeviceFromRemote(remoteDevice: RemoteDevice) = Device(
        _guid = remoteDevice.guid,
        _devType = remoteDevice.devType,
        _name = remoteDevice.name,
        _lat = remoteDevice.lat,
        _lon = remoteDevice.lon,
        _status = remoteDevice.status
    )

    fun mapDevicesFromDB(dbDevices: List<DBDevice>) = dbDevices.map {
        mapDeviceFromDB(it)
    }

    fun mapDevicesFromRemote(remoteDevices: List<RemoteDevice>) = remoteDevices.map {
        mapDeviceFromRemote(it)
    }

    fun mapDeviceParamFromDB(dbDeviceParam: DBDeviceParam) = DeviceParam(
        _uid = dbDeviceParam.uid,
        _paramType = DeviceParam.ParamType.values()[dbDeviceParam.paramType],
        _measUnit = dbDeviceParam.measUnit,
        _name = dbDeviceParam.name,
        _shortName = dbDeviceParam.shortName,
        _status = dbDeviceParam.status
    )

    fun mapDeviceParamFromRemote(remoteDeviceParam: RemoteDeviceParam) = DeviceParam(
        _uid = remoteDeviceParam.uid,
        _paramType = DeviceParam.ParamType.values()[remoteDeviceParam.paramType],
        _measUnit = remoteDeviceParam.measUnit,
        _name = remoteDeviceParam.name,
        _shortName = remoteDeviceParam.shortName,
        _status = remoteDeviceParam.status
    )

    fun mapDeviceParamsFromDB(dbDeviceParams: List<DBDeviceParam>) = dbDeviceParams.map {
        mapDeviceParamFromDB(it)
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
        _deviceInfo = mapDeviceFromDB(dbCollectedDataExtPOJO.device),
        _paramInfo = mapDeviceParamFromDB(dbCollectedDataExtPOJO.param)
    )

    fun mapCollectedDataExtList(dbCollectedDataExtPOJOs: List<DBCollectedDataExtPOJO>) = dbCollectedDataExtPOJOs.map {
        mapCollectedDataExt(it)
    }
}
