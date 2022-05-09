package hu.bme.aut.android.eszkozkolcsonzo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomDevice(
    @PrimaryKey
    val id: Int,
    val name: String,
    val desc: String,
    val available: Boolean
)
