package hu.bme.aut.android.eszkozkolcsonzo.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Lease(
    val id: Int,
    val reservationId: Int,
    val handlerUserId: Int,
    val requesterUserId: Int,
    val active: Boolean
){
    companion object{
        const val path = "/leases"
    }
}