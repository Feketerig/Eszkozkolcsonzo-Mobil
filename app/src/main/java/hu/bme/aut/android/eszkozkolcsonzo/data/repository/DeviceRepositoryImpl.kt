package hu.bme.aut.android.eszkozkolcsonzo.data.repository

import hu.bme.aut.android.eszkozkolcsonzo.data.local.DeviceDatabase
import hu.bme.aut.android.eszkozkolcsonzo.data.local.RoomDevice
import hu.bme.aut.android.eszkozkolcsonzo.data.mapper.toDomainDevice
import hu.bme.aut.android.eszkozkolcsonzo.data.mapper.toRoomDevice
import hu.bme.aut.android.eszkozkolcsonzo.data.network.NetworkInterface
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.Device
import hu.bme.aut.android.eszkozkolcsonzo.domain.repository.DeviceRepository
import hu.bme.aut.android.eszkozkolcsonzo.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepositoryImpl @Inject constructor(
    private val network: NetworkInterface,
    private val db: DeviceDatabase
): DeviceRepository {

    private val dao = db.dao

    override suspend fun getDevices(
        forceRefresh: Boolean,
        onlyAvailable: Boolean,
        query: String
    ): Flow<Resource<List<Device>>> {
        return flow {
            emit(Resource.Loading(true))

            val localDevices = searchDevices(onlyAvailable, query)
            emit(Resource.Success(localDevices.map { it.toDomainDevice() }))

            val isDbEmpty = localDevices.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !forceRefresh
            if(shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteDevices = try {
                network.getAllDevices()
            }catch (e: Exception){
                emit(Resource.Error("Couldn't load data"))
                emit(Resource.Loading(false))
                null
            }

            remoteDevices?.let { devices ->
                if (devices.isEmpty()){
                    return@flow
                }
                dao.clearDevices()
                dao.insertDevices(devices.map { it.toRoomDevice() })
                emit(Resource.Success(searchDevices(onlyAvailable, "").map { it.toDomainDevice() }))
                emit(Resource.Loading(false))
            }
        }
    }

    private suspend fun searchDevices(
        onlyAvailable: Boolean,
        query: String
    ): List<RoomDevice>{
        return if (onlyAvailable){
            dao.searchOnlyActiveDevices(query)
        }else{
            dao.searchDevices(query)
        }
    }

    override suspend fun getDevice(id: Int): Resource<Device> {
        return Resource.Success(dao.getDeviceById(id).toDomainDevice())
        /*return try {
            val device = network.getDevice(id)
            if (device != null){
                Resource.Success(device)
            }else{
                Resource.Error("Couldn't load data")
            }
        }catch (e: Exception){
            Resource.Error("Couldn't load data")
        }*/
    }

    override suspend fun addDevice(device: Device): Int {
        return network.addDevice(device)
    }
}