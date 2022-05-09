package hu.bme.aut.android.eszkozkolcsonzo.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Reservation(
    val id: Int,
    val deviceId: Int,
    val startDate: Long,
    val endDate: Long,
    val userId: Int
){
    companion object {
        const val path = "/reservations"
    }
}
