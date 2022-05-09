package hu.bme.aut.android.eszkozkolcsonzo.domain.repository

import hu.bme.aut.android.eszkozkolcsonzo.domain.model.Reservation
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.ReservationInfo
import hu.bme.aut.android.eszkozkolcsonzo.util.Resource
import kotlinx.coroutines.flow.Flow

interface ReservationRepository {

    suspend fun addReservation(reservation: Reservation)

    suspend fun getReservations(userId: Int?): Flow<Resource<List<ReservationInfo>>>

    suspend fun getReservation(id: Int): ReservationInfo
}