package hu.bme.aut.android.eszkozkolcsonzo.data.network

import hu.bme.aut.android.eszkozkolcsonzo.domain.model.Device
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.Lease
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.Reservation
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.User
import io.ktor.client.*
import io.ktor.client.request.*

class Network(
    private val client: HttpClient
): NetworkInterface {
    override suspend fun login(email: String, password: String): User? {
        return try {
            client.get(NetworkInterface.Endpoints.User.url){
                parameter("email", email)
            }
        } catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

    override suspend fun registration(user: User): User {
        try {
            client.post<User>(NetworkInterface.Endpoints.User.url){
                body = user
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
        return user
    }

    override suspend fun getAllDevices(): List<Device> {
        return try {
            client.get(NetworkInterface.Endpoints.Device.url)
        }catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getDevice(id: Int): Device? {
        return try {
            client.get("${NetworkInterface.Endpoints.Device.url}/" + id.toString())
        }catch (e: Exception){
            null
        }
    }

    override suspend fun addDevice(device: Device): Int {
        try {
            client.post<Device>(NetworkInterface.Endpoints.Device.url){
                body = device
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
        return 1
    }

    /*override suspend fun deleteDevice(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getActiveLeases(): List<Lease> {
        TODO("Not yet implemented")
    }

    override suspend fun getLease(id: Int): Lease {
        TODO("Not yet implemented")
    }*/

    override suspend fun getLeaseIdByReservationId(id: Int): Int {
        return try {
            client.get("${NetworkInterface.Endpoints.Lease.url}/reservation/$id")
        } catch (e: Exception){
            e.printStackTrace()
            0
        }
    }

    override suspend fun addLease(lease: Lease) {
        try {
            client.post<Lease>(NetworkInterface.Endpoints.Lease.url){
                body = lease
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    /*override suspend fun deleteLease(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun activateLease(id: Int) {
        TODO("Not yet implemented")
    }*/

    override suspend fun deactivateLease(id: Int) {
        try {
            client.put("${NetworkInterface.Endpoints.Lease.url}/$id")
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun getAllReservations(): List<Reservation> {
        return try {
            client.get(NetworkInterface.Endpoints.Reservation.url)
        }catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getAllReservationByUserId(id: Int): List<Reservation> {
        return try {
            client.get("${NetworkInterface.Endpoints.Reservation.url}/user/$id")
        }catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getReservationByDeviceId(id: Int): Reservation? {
        return try {
            client.get("${NetworkInterface.Endpoints.Reservation.url}/device/$id")
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

    /*override suspend fun getReservation(id: Int): Reservation {
        TODO("Not yet implemented")
    }*/

    override suspend fun addReservation(reservation: Reservation) {
        try {
            client.post<Reservation>{
                url(NetworkInterface.Endpoints.Reservation.url)
                body =  reservation}
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    /*override suspend fun deleteReservation(id: Int) {
        TODO("Not yet implemented")
    }*/

    override suspend fun getUserNameById(userId: Int): String {
        return try {
            client.get("${NetworkInterface.Endpoints.User.url}/$userId/name")
        } catch (e: Exception){
            e.printStackTrace()
            ""
        }
    }

    override suspend fun getUserById(userId: Int): User? {
        return try {
            client.get("${NetworkInterface.Endpoints.User.url}/$userId")
        } catch (e: Exception){
            e.printStackTrace()
            null
        }
    }
}