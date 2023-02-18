package com.example.android.metercollectionapp.domain.model

import com.example.android.metercollectionapp.SyncStatus

class DeviceParam (
    _uid: Long = 0,
    _paramType: DeviceParamType = DeviceParamType.UINT32,
    _measUnit: String = "",
    _name: String = "",
    _shortName: String = "",
    _status: SyncStatus = SyncStatus.UNKNOWN
) {
    var uid: Long = _uid
    var paramType: DeviceParamType = _paramType
    var measUnit: String = _measUnit
    var name: String = _name
    var shortName: String = _shortName
    var status: SyncStatus = _status
}
