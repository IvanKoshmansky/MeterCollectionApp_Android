package com.example.android.metercollectionapp.domain.model

import com.example.android.metercollectionapp.SyncStatus

class CollectedDataExt (
    _id: Long = 0,
    _unixTime: Long = 0,
    _userId: Long = 0,
    _deviceGuid: Long = 0,
    _paramUid: Long = 0,
    _paramValue: Float = 0.0f,
    _status: SyncStatus = SyncStatus.UNKNOWN,
    _deviceInfo: Device = Device(),
    _paramInfo: DeviceParam = DeviceParam()
) : CollectedData (
    _id,
    _unixTime,
    _userId,
    _deviceGuid,
    _paramUid,
    _paramValue,
    _status
) {
    var deviceInfo: Device = _deviceInfo
    var paramInfo: DeviceParam = _paramInfo
}
