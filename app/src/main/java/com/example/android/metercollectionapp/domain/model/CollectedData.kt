package com.example.android.metercollectionapp.domain.model

import com.example.android.metercollectionapp.SyncStatus

class CollectedData (
    _id: Long = 0,
    _unixTime: Long = 0,
    _userId: Long = 0,
    _deviceGuid: Long = 0,
    _paramUid: Long = 0,
    _paramValue: Float = 0.0f,
    _status: SyncStatus = SyncStatus.UNKNOWN,
) {
    var id: Long = _id
    var unixTime: Long = _unixTime
    var userId: Long = _userId
    var deviceGuid: Long = _deviceGuid
    var paramUid: Long = _paramUid
    var paramValue: Float = _paramValue
    var status: SyncStatus = _status
}
