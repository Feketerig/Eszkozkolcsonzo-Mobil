package hu.bme.aut.android.eszkozkolcsonzo.presentation.lease

import hu.bme.aut.android.eszkozkolcsonzo.domain.model.Device
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.Reservation
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.User

data class LeaseState(
    val showCamera: Boolean = false,
    val reservation: Reservation? = null,
    val reservationUser: User? = null,
    val isDevice: Boolean = false,
    val deviceId: Int? = null,
    val device: Device? = null,
    val isUser: Boolean = false,
    val userId: Int? = null,
    val user: User? = null
)