package hu.bme.aut.android.eszkozkolcsonzo.data.mapper

import hu.bme.aut.android.eszkozkolcsonzo.data.local.RoomDevice
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.Device

fun RoomDevice.toDomainDevice(): Device{
    return Device(
        id = id,
        name = name,
        desc = desc,
        available = available
    )
}

fun Device.toRoomDevice(): RoomDevice{
    return RoomDevice(
        id = id,
        name = name,
        desc = desc,
        available = available
    )
}