package com.example.android.metercollectionapp.domain.usecase

import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.domain.model.DeviceParam
import javax.inject.Inject

// получить список параметров, связанных с данным устройством
class GetDeviceParamsAssociatedFromUseCase @Inject constructor (private val repository: Repository) {
    suspend fun execute(guid: Long): List<DeviceParam> {
        val paramsIds = repository.getDeviceParamsIdsAssociatedFrom(guid)
        return repository.getDeviceParamsByParamsIds(paramsIds)
    }
}

// суть UseCase - переиспользовать бизнес-логику, отвязаться от конкретной реализации репозиториев,
// вынести бизнес-логику из репозиториев, максимально упростить репозитории
