package com.example.android.metercollectionapp.data.mappers

import com.example.android.metercollectionapp.data.localdb.DBDevice
import com.example.android.metercollectionapp.data.localdb.DBDeviceParam
import com.example.android.metercollectionapp.data.localdb.DBUser
import com.example.android.metercollectionapp.domain.model.Device
import com.example.android.metercollectionapp.domain.model.DeviceParam
import com.example.android.metercollectionapp.domain.model.DeviceParamType
import com.example.android.metercollectionapp.domain.model.User

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
}
