package com.example.android.metercollectionapp.data.mappers

import com.example.android.metercollectionapp.data.localdb.*
import com.example.android.metercollectionapp.data.remote.datatransferobjects.RemoteDevice
import com.example.android.metercollectionapp.data.remote.datatransferobjects.RemoteDeviceParam
import com.example.android.metercollectionapp.data.remote.datatransferobjects.RemoteUser
import com.example.android.metercollectionapp.domain.model.CollectedData
import com.example.android.metercollectionapp.domain.model.Device
import com.example.android.metercollectionapp.domain.model.DeviceParam
import com.example.android.metercollectionapp.domain.model.User

class FromDomainMapper {

    fun mapUserToDB(user: User) = DBUser(
        id = user.id,
        login = user.login,
        passEncrypted = user.passEncrypted,
        name = user.name,
        status = user.status
    )

    fun mapUserToRemote(user: User) = RemoteUser(
        id = user.id,
        login = user.login,
        passEncrypted = user.passEncrypted,
        name = user.name,
        status = user.status
    )

    fun mapDeviceToDB(device: Device) = DBDevice(
        guid = device.guid,
        devType = device.devType,
        name = device.name,
        lat = device.lat,
        lon = device.lon,
        status = device.status
    )

    fun mapDeviceToRemote(device: Device) = RemoteDevice(
        guid = device.guid,
        devType = device.devType,
        name = device.name,
        lat = device.lat,
        lon = device.lon,
        status = device.status
    )

    fun mapDevicesToDB(devices: List<Device>) = devices.map {
        mapDeviceToDB(it)
    }

    fun mapDevicesToRemote(devices: List<Device>) = devices.map {
        mapDeviceToRemote(it)
    }

    fun mapDeviceParamToDB(deviceParam: DeviceParam) = DBDeviceParam(
        uid = deviceParam.uid,
        paramType = deviceParam.paramType.ordinal,
        measUnit = deviceParam.measUnit,
        name = deviceParam.name,
        shortName = deviceParam.shortName,
        status = deviceParam.status
    )

    fun mapDeviceParamToRemote(deviceParam: DeviceParam) = RemoteDeviceParam(
        uid = deviceParam.uid,
        paramType = deviceParam.paramType.ordinal,
        measUnit = deviceParam.measUnit,
        name = deviceParam.name,
        shortName = deviceParam.shortName,
        status = deviceParam.status
    )

    fun mapCollectedData(collectedData: CollectedData) = DBCollectedData(
        id = collectedData.id,
        unixTime = collectedData.unixTime,
        userId = collectedData.userId,
        deviceId = collectedData.deviceGuid,
        paramId = collectedData.paramUid,
        paramValue = collectedData.paramValue,
        status = collectedData.status
    )
}
