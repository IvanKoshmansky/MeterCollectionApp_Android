package com.example.android.metercollectionapp.data

import com.example.android.metercollectionapp.data.localdb.LocalDatabase
import com.example.android.metercollectionapp.data.mappers.FromDomainMapper
import com.example.android.metercollectionapp.data.mappers.ToDomainMapper
import com.example.android.metercollectionapp.data.storage.Storage
import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.domain.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.sql.Timestamp
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
        var device: Device? = null
        withContext(Dispatchers.IO) {
            val dbDevice = localDatabase.databaseDao.getDeviceById(guid)
            if (dbDevice != null) {
                device = ToDomainMapper().mapDevice(dbDevice)
            }
        }
        return device ?: throw IOException("device not found")
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
    // (пусть в данном конкретном случае она и не нарушается) и недопущения перегрузки RAM только для применения фильтра

    override suspend fun setDeviceParamsAssociatedTo(guid: Long, ids: List<Long>) {
        withContext(Dispatchers.IO) {
            storage.setDeviceParamsIdsAssociatedTo(guid, ids)
        }
    }

    override suspend fun addNewCollectedDataRow(newRowUserId: Long, newRowDeviceGuid: Long, newRowParamUid: Long,
                                                newRowParamValue: Float) {
        val collectedData = CollectedData().apply {
            unixTime = Timestamp(System.currentTimeMillis()).time
            userId = newRowUserId
            deviceGuid = newRowDeviceGuid
            paramUid = newRowParamUid
            paramValue = newRowParamValue
        }
        // в формат DB
        val dbRow = FromDomainMapper().mapCollectedData(collectedData)
        withContext(Dispatchers.IO) {
            localDatabase.databaseDao.insertNewCollectedDataRow(dbRow)
        }
    }

    override suspend fun getCollectedData(userId: Long): List<CollectedDataExt> {
        TODO("Not yet implemented")
        // все запросы в связанные таблицы переложить на API Room
        // Room выдает максимально подготовленную выборку для экономии обращений к SD карте и лучшей оптимизации

    }

    override suspend fun sync() {
        TODO("Not yet implemented")
    }

}

// из конструктора репозитория убраны мапперы посольку они не участвуют в модульных тестах
// UseCase имеет смысл применять только для "среза" данных из разных репозиториев или источников
// необходимость в UseCase есть для того, чтобы переиспользовать бизнес логику

// все запросы в связанные таблицы переложить на API Room
// Room выдает максимально подготовленную выборку для экономии обращений к SD карте и лучшей оптимизации
