package com.example.android.metercollectionapp.data.mappers

import com.example.android.metercollectionapp.data.localdb.DBDevice
import com.example.android.metercollectionapp.data.localdb.DBDeviceParam
import com.example.android.metercollectionapp.data.localdb.DBUser
import com.example.android.metercollectionapp.domain.model.Device
import com.example.android.metercollectionapp.domain.model.DeviceParam
import com.example.android.metercollectionapp.domain.model.User

class FromDomainMapper {

    fun mapUser(user: User) = DBUser(
        id = user.id,
        login = user.login,
        passEncrypted = user.passEncrypted,
        name = user.name,
        status = user.status
    )

    fun mapDevice(device: Device) = DBDevice(
        guid = device.guid,
        devType = 0,
        name = device.name,
        lat = 0.0,
        lon = 0.0,
        status = device.status
    )

    fun mapDeviceParam(deviceParam: DeviceParam) = DBDeviceParam(
        uid = deviceParam.uid,
        paramType = deviceParam.paramType.ordinal,
        measUnit = deviceParam.measUnit,
        name = deviceParam.name,
        shortName = deviceParam.shortName,
        status = deviceParam.status
    )
}
