package hu.bme.aut.android.eszkozkolcsonzo.domain.repository

import hu.bme.aut.android.eszkozkolcsonzo.domain.model.Device
import hu.bme.aut.android.eszkozkolcsonzo.util.Resource
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {

    suspend fun getDevices(
        forceRefresh: Boolean,
        onlyAvailable: Boolean,
        query: String
    ): Flow<Resource<List<Device>>>

    suspend fun getDevice(
        id: Int
    ): Resource<Device>

    suspend fun addDevice(
        device: Device
    ): Int
}