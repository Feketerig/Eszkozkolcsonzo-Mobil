package hu.bme.aut.android.eszkozkolcsonzo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DeviceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevices(devices: List<RoomDevice>)

    @Query("Delete from roomdevice")
    suspend fun clearDevices()

    @Query("Select * from roomdevice where LOWER(name) Like '%' || LOWER(:query) || '%'")
    suspend fun searchDevices(query: String): List<RoomDevice>

    @Query("Select * from roomdevice where id == :id")
    suspend fun getDeviceById(id: Int): RoomDevice

    @Query("Select * from roomdevice where LOWER(name) Like '%' || LOWER(:query) || '%' and available == 1")
    suspend fun searchOnlyActiveDevices(query: String): List<RoomDevice>
}