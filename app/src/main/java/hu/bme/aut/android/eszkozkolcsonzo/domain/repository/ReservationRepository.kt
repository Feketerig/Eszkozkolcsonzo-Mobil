package hu.bme.aut.android.eszkozkolcsonzo.domain.repository

import hu.bme.aut.android.eszkozkolcsonzo.domain.model.ReservationInfo
import hu.bme.aut.android.eszkozkolcsonzo.util.Resource
import kotlinx.coroutines.flow.Flow

interface ReservationRepository {

    suspend fun addReservation(deviceId: Int, startDate: Long, endDate: Long)

    suspend fun getReservations(userId: Int?): Flow<Resource<List<ReservationInfo>>>

    suspend fun getReservation(id: Int): ReservationInfo
}