package com.example.android.metercollectionapp.domain.model

import com.example.android.metercollectionapp.SyncStatus

class User constructor (
    _id: Long = 0,
    _login: String = "",
    _passEncrypted: String = "",
    _name: String = "",
    _status: SyncStatus = SyncStatus.UNKNOWN
) {
    var id: Long = _id
    var login: String = _login
    var passEncrypted: String = _passEncrypted
    var name: String = _name
    var status: SyncStatus = _status
}
