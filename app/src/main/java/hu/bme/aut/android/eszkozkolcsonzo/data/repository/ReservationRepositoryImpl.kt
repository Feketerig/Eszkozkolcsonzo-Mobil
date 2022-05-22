package hu.bme.aut.android.eszkozkolcsonzo.data.repository

import hu.bme.aut.android.eszkozkolcsonzo.data.network.NetworkInterface
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.ReservationInfo
import hu.bme.aut.android.eszkozkolcsonzo.domain.repository.ReservationRepository
import hu.bme.aut.android.eszkozkolcsonzo.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

class ReservationRepositoryImpl @Inject constructor(
    private val network: NetworkInterface
    ) : ReservationRepository {
    override suspend fun addReservation(deviceId: Int, startDate: Long, endDate: Long) {
        network.addReservation(deviceId, startDate, endDate)
    }

    override suspend fun getReservations(userId: Int?): Flow<Resource<List<ReservationInfo>>> {
       return flow {
           emit(Resource.Loading(true))
           if(userId == null) {
               val result = try {
                   network.getAllReservations()
               }catch (e: Exception){
                   emit(Resource.Error("Couldn't load data"))
                   emit(Resource.Loading(false))
                   null
               }
               result?.let {
                   val resultInfo = it.map {
                       val deviceName = network.getDevice(it.deviceId)?.name ?: ""
                       val userName = network.getUserNameById(it.userId)
                       val startDate = Date(it.startDate)
                       val endDate = Date(it.endDate)
                       val startDateText = "${startDate.year}-${startDate.month+1}-${startDate.day}"
                       val endDateText = "${endDate.year}-${endDate.month+1}-${endDate.day}"
                       ReservationInfo(
                           deviceName = deviceName,
                           userName = userName,
                           startDate = startDateText,
                           endDate = endDateText
                       )
                   }
                   emit(Resource.Success(resultInfo))
                   emit(Resource.Loading(false))
               }
           }else{
               val result = try {
                   network.getAllReservationByUserId(userId)
               }catch (e: Exception){
                   emit(Resource.Error("Couldn't load data"))
                   emit(Resource.Loading(false))
                   null
               }
               result?.let {
                   val resultInfo = it.map {
                       val deviceName = network.getDevice(it.deviceId)?.name ?: ""
                       val userName = network.getUserNameById(it.userId)
                       val startDate = Date(it.startDate)
                       val endDate = Date(it.endDate)
                       val startDateText = "${startDate.year}-${startDate.month+1}-${startDate.day}"
                       val endDateText = "${endDate.year}-${endDate.month+1}-${endDate.day}"
                       ReservationInfo(
                           deviceName = deviceName,
                           userName = userName,
                           startDate = startDateText,
                           endDate = endDateText
                       )
                   }
                   emit(Resource.Success(resultInfo))
                   emit(Resource.Loading(false))
               }
           }
       }
    }

    override suspend fun getReservation(id: Int): ReservationInfo {
        TODO("Not yet implemented")
    }
}