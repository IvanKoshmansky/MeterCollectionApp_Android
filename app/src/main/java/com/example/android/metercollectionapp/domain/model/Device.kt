package com.example.android.metercollectionapp.domain.model

import com.example.android.metercollectionapp.SyncStatus

class Device (
    _guid: Long = 0,
    _devType: Int = 0,
    _name: String = "",
    _lat: Double = 0.0,
    _lon: Double = 0.0,
    _status: SyncStatus = SyncStatus.UNKNOWN
) {
    var guid: Long = _guid
    var devType: Int = _devType
    var name: String = _name
    var lat: Double = _lat
    var lon: Double = _lon
    var status: SyncStatus = _status
}
