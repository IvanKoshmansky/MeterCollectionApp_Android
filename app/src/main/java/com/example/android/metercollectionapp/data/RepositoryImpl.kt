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
    private val storage: Storage,
    private val toDomainMapper: ToDomainMapper,
    private val fromDomainMapper: FromDomainMapper
): Repository {

    override suspend fun getAllUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            toDomainMapper.mapUsers(localDatabase.databaseDao.getAllUsers())
        }
    }

    override suspend fun getUserById(id: Long): User {
        var user: User? = null
        withContext(Dispatchers.IO) {
            val dbUser = localDatabase.databaseDao.getUserById(id)
            if (dbUser != null) {
                user = toDomainMapper.mapUser(dbUser)
            }
        }
        return user ?: throw IOException("user not found")
    }

    override suspend fun addNewUser(user: User): Long {
        val dbUser = fromDomainMapper.mapUser(user)
        return withContext(Dispatchers.IO) {
            localDatabase.databaseDao.insertNewUser(dbUser)
        }
    }

    override suspend fun getDeviceById(guid: Long): Device {
        TODO("Not yet implemented")
    }

    // TODO: на будущее отслеживать ситуацию, когда устройство с данным GUID уже существует
    override suspend fun addNewDeviceById(name: String, guid: Long) {
        val device = Device().also {
            it.name = name
            it.guid = guid
        }
        val dbDevice = fromDomainMapper.mapDevice(device)
        withContext(Dispatchers.IO) {
            localDatabase.databaseDao.insertNewDevice(dbDevice)
        }
    }

    override suspend fun getAllDevices(): List<Device> {
        return withContext(Dispatchers.IO) {
            val dbDevices = localDatabase.databaseDao.getAllDevices()
            toDomainMapper.mapDevices(dbDevices)
        }
    }

    override suspend fun getAllDeviceParams(): List<DeviceParam> {
        return withContext(Dispatchers.IO) {
            val dbDeviceParams = localDatabase.databaseDao.getAllDeviceParams()
            toDomainMapper.mapDeviceParams(dbDeviceParams)
        }
    }

    override suspend fun getDeviceParamsByParamsIds(ids: List<Long>): List<DeviceParam> {
        return withContext(Dispatchers.IO) {
            val dbDeviceParams = localDatabase.databaseDao.getDeviceParamsByParamsIds(ids)
            toDomainMapper.mapDeviceParams(dbDeviceParams)
        }
    }

    override suspend fun addNewDeviceParam(deviceParam: DeviceParam) {
        val dbDeviceParam = fromDomainMapper.mapDeviceParam(deviceParam)
        withContext(Dispatchers.IO) {
            localDatabase.databaseDao.insertNewDeviceParam(dbDeviceParam)
        }
    }

    override suspend fun getDeviceParamsIdsAssociatedFrom(guid: Long): List<Long> {
        return withContext(Dispatchers.IO) {
            storage.getDeviceParamsIdsAssociatedFrom(guid)
        }
    }

    override suspend fun sync() {
        TODO("Not yet implemented")
    }

}
