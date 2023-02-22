package com.example.android.metercollectionapp.data

import com.example.android.metercollectionapp.data.localdb.LocalDatabase
import com.example.android.metercollectionapp.data.mappers.FromDomainMapper
import com.example.android.metercollectionapp.data.mappers.ToDomainMapper
import com.example.android.metercollectionapp.data.storage.Storage
import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.domain.model.Device
import com.example.android.metercollectionapp.domain.model.DeviceParam
import com.example.android.metercollectionapp.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class RepositoryImpl @Inject constructor (
    private val localDatabase: LocalDatabase,
    private val storage: Storage
): Repository {

    override suspend fun getAllUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            ToDomainMapper().mapUsers(localDatabase.databaseDao.getAllUsers())
        }
    }

    override suspend fun getUserById(id: Long): User {
        var user: User? = null
        withContext(Dispatchers.IO) {
            val dbUser = localDatabase.databaseDao.getUserById(id)
            if (dbUser != null) {
                user = ToDomainMapper().mapUser(dbUser)
            }
        }
        return user ?: throw IOException("user not found")
    }

    override suspend fun addNewUser(user: User): Long {
        val dbUser = FromDomainMapper().mapUser(user)
        return withContext(Dispatchers.IO) {
            localDatabase.databaseDao.insertNewUser(dbUser)
        }
    }

    override suspend fun getDeviceById(guid: Long): Device {
        TODO("Not yet implemented")
    }

    // TODO: отслеживать ситуацию, когда устройство с данным GUID уже существует
    override suspend fun addNewDeviceById(name: String, guid: Long) {
        val device = Device().also {
            it.name = name
            it.guid = guid
        }
        val dbDevice = FromDomainMapper().mapDevice(device)
        withContext(Dispatchers.IO) {
            localDatabase.databaseDao.insertNewDevice(dbDevice)
        }
    }

    override suspend fun getAllDevices(): List<Device> {
        return withContext(Dispatchers.IO) {
            val dbDevices = localDatabase.databaseDao.getAllDevices()
            ToDomainMapper().mapDevices(dbDevices)
        }
    }

    override suspend fun getAllDeviceParams(): List<DeviceParam> {
        return withContext(Dispatchers.IO) {
            val dbDeviceParams = localDatabase.databaseDao.getAllDeviceParams()
            ToDomainMapper().mapDeviceParams(dbDeviceParams)
        }
    }

    override suspend fun addNewDeviceParam(deviceParam: DeviceParam) {
        val dbDeviceParam = FromDomainMapper().mapDeviceParam(deviceParam)
        withContext(Dispatchers.IO) {
            localDatabase.databaseDao.insertNewDeviceParam(dbDeviceParam)
        }
    }

    override suspend fun getDeviceParamsAssociatedFrom(guid: Long): List<DeviceParam> {
        return withContext(Dispatchers.IO) {
            val ids = storage.getDeviceParamsIdsAssociatedFrom(guid)
            val dbDeviceParams = localDatabase.databaseDao.getDeviceParamsByParamsIds(ids)
            ToDomainMapper().mapDeviceParams(dbDeviceParams)
        }
    }

    override suspend fun getDeviceParamsUnassociatedFrom(guid: Long): List<DeviceParam> {
        return withContext(Dispatchers.IO) {
            val ids = storage.getDeviceParamsIdsAssociatedFrom(guid)
            val dbDeviceParams = localDatabase.databaseDao.getDeviceParamsExcludingParamsIds(ids)
            ToDomainMapper().mapDeviceParams(dbDeviceParams)
        }
    }
    // вот здесь в раньше запрашивались все параметры из репозитория (первый запрос к SD карте),
    // затем запрашивались привязанные ID (второй запрос к SD карте)
    // и с помощью фильтра отсекались ненужные - подход не правильный,
    // все "составные" запросы лучше перенести на уровень базы данных для обеспечения атомарности
    // (пусть не в данном конкретном случае) и недопущения загрузки в RAM всего только для применения фильтра

    override suspend fun setDeviceParamsAssociatedTo(guid: Long, ids: List<Long>) {
        withContext(Dispatchers.IO) {
            storage.setDeviceParamsIdsAssociatedTo(guid, ids)
        }
    }

    override suspend fun sync() {
        TODO("Not yet implemented")
    }

}

// из конструктора репозитория нужно убрать мапперы посольку они не участвуют в модульных тестах
// UseCase имеет смысл применять только для "среза" данных из разных репозиториев или источников
// необходимость в UseCase есть для того, чтобы переиспользовать бизнес логику
