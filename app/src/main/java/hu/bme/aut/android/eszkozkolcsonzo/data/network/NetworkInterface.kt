package hu.bme.aut.android.eszkozkolcsonzo.data.network

import hu.bme.aut.android.eszkozkolcsonzo.domain.model.Device
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.Reservation
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.User

interface NetworkInterface {

    suspend fun login(email: String, password: String): String

    suspend fun registration(email: String, name: String, phone: String, address: String, password: String)

    suspend fun getAllDevices(): List<Device>

    suspend fun getDevice(id: Int): Device?

    suspend fun addDevice(device: Device): Int

    //suspend fun deleteDevice(id: Int)

    //suspend fun getActiveLeases(): List<Lease>

    //suspend fun getLease(id: Int): Lease

    suspend fun getLeaseIdByReservationId(id: Int): Int

    suspend fun addLease(reservationId: Int, handlerUserId: Int, requesterUserId: Int)

    //suspend fun deleteLease(id: Int)

    //suspend fun activateLease(id: Int)

    suspend fun deactivateLease(id: Int)

    suspend fun getAllReservations(): List<Reservation>

    suspend fun getAllReservationByUserId(id: Int): List<Reservation>

    suspend fun getReservationByDeviceId(id: Int): Reservation?

    //suspend fun getReservation(id: Int): Reservation

    suspend fun addReservation(deviceId: Int, startDate: Long, endDate: Long)

    //suspend fun deleteReservation(id: Int)

    suspend fun getUserNameById(userId: Int): String

    suspend fun getUserById(userId: Int): User?

    suspend fun getUserByEmail(email: String): User?

    companion object{
        //const val BASE_URL = "http://152.66.181.19:8080"
        const val BASE_URL = "http://10.0.2.2:8080/api"
    }

    sealed class Endpoints(val url: String){
        object Device: Endpoints("$BASE_URL/devices")
        object Reservation: Endpoints("$BASE_URL/reservations")
        object Lease: Endpoints("$BASE_URL/leases")
        object User: Endpoints("$BASE_URL/users")
    }
}