package com.example.android.metercollectionapp.domain.usecase

import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.domain.model.DeviceParam
import javax.inject.Inject

// получить список свободных параметров, которые доступны для присвоения данному устройству
class GetDeviceParamsUnassociatedFromUseCase @Inject constructor (private val repository: Repository) {
    suspend fun execute(guid: Long): List<DeviceParam> {
        val allParamsFromRepo = repository.getAllDeviceParams()
        val setOfAssociatedParamsIds = repository.getDeviceParamsIdsAssociatedFrom(guid).toSet()
        return allParamsFromRepo.filterNot {
            setOfAssociatedParamsIds.contains(it.uid)
        }
    }
}
