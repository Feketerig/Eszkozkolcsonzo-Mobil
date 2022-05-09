package hu.bme.aut.android.eszkozkolcsonzo.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Lease(
    val id: Int,
    val reservation: Reservation,
    val kiado: User,
    val atvevo: User,
    val active: Boolean
){
    companion object{
        const val path = "/leases"
    }
}