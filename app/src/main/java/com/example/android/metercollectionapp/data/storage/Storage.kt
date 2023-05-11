package com.example.android.metercollectionapp.data.storage

interface Storage {
    suspend fun getDeviceParamsIdsAssociatedFrom(guid: Long): List<Long>
    suspend fun setDeviceParamsIdsAssociatedTo(guid: Long, paramsIds: List<Long>)
}
