package hu.bme.aut.android.eszkozkolcsonzo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [RoomDevice::class],
    version = 1
)
abstract class DeviceDatabase: RoomDatabase() {
    abstract val dao: DeviceDao
}