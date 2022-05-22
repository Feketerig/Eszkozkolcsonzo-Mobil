package hu.bme.aut.android.eszkozkolcsonzo.data.network

import hu.bme.aut.android.eszkozkolcsonzo.MainViewModel
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.Device
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.Lease
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.Reservation
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.User
import hu.bme.aut.android.eszkozkolcsonzo.util.sha256
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.HttpHeaders.Authorization

class Network(
    private val client: HttpClient
): NetworkInterface {
    override suspend fun login(email: String, password: String): String {
        return try {
            client.post<String>("${NetworkInterface.Endpoints.User.url}/login"){
                body = "$email|${password.sha256()}"
            }
        } catch (e: Exception){
            e.printStackTrace()
            ""
        }
    }

    override suspend fun registration(email: String, name: String, phone: String, address: String, password: String) {
        try {
            client.post<User>(NetworkInterface.Endpoints.User.url){
                parameter("name", name)
                parameter("email", email)
                parameter("phone", phone)
                parameter("address", address)
                parameter("pwHash", password.sha256())
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun getAllDevices(): List<Device> {
        return try {
            client.get(NetworkInterface.Endpoints.Device.url){
                headers {
                    append(Authorization, "Bearer ${MainViewModel.state.token}")
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getDevice(id: Int): Device? {
        return try {
            client.get("${NetworkInterface.Endpoints.Device.url}/" + id.toString()){
                headers {
                    append(Authorization, "Bearer ${MainViewModel.state.token}")
                }
            }
        }catch (e: Exception){
            null
        }
    }

    override suspend fun addDevice(device: Device): Int {
        try {
            client.post<Device>(NetworkInterface.Endpoints.Device.url){
                parameter("name",device.name)
                parameter("desc",device.desc)
                headers {
                    append(Authorization, "Bearer ${MainViewModel.state.token}")
                }
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
            client.get("${NetworkInterface.Endpoints.Lease.url}/reservation/$id"){
                headers {
                    append(Authorization, "Bearer ${MainViewModel.state.token}")
                }
            }
        } catch (e: Exception){
            e.printStackTrace()
            0
        }
    }

    override suspend fun addLease(reservationId: Int, handlerUserId: Int, requesterUserId: Int) {
        try {
            client.post<Lease>(NetworkInterface.Endpoints.Lease.url){
                parameter("resId", reservationId)
                parameter("kiado", handlerUserId)
                parameter("atvevo", requesterUserId)
                headers {
                    append(Authorization, "Bearer ${MainViewModel.state.token}")
                }
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
            client.put("${NetworkInterface.Endpoints.Lease.url}/$id"){
                headers {
                    append(Authorization, "Bearer ${MainViewModel.state.token}")
                }
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun getAllReservations(): List<Reservation> {
        return try {
            client.get(NetworkInterface.Endpoints.Reservation.url){
                headers {
                    append(Authorization, "Bearer ${MainViewModel.state.token}")
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getAllReservationByUserId(id: Int): List<Reservation> {
        return try {
            client.get("${NetworkInterface.Endpoints.Reservation.url}/user"){
                headers {
                    append(Authorization, "Bearer ${MainViewModel.state.token}")
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getReservationByDeviceId(id: Int): Reservation? {
        return try {
            client.get("${NetworkInterface.Endpoints.Reservation.url}/device/$id"){
                headers {
                    append(Authorization, "Bearer ${MainViewModel.state.token}")
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

    /*override suspend fun getReservation(id: Int): Reservation {
        TODO("Not yet implemented")
    }*/

    override suspend fun addReservation(deviceId: Int, startDate: Long, endDate: Long) {
        try {
            client.post<Reservation>{
                url(NetworkInterface.Endpoints.Reservation.url)
                parameter("deviceid", deviceId)
                parameter("from", startDate)
                parameter("to", endDate)
                headers {
                    append(Authorization, "Bearer ${MainViewModel.state.token}")
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    /*override suspend fun deleteReservation(id: Int) {
        TODO("Not yet implemented")
    }*/

    override suspend fun getUserNameById(userId: Int): String {
        return try {
            client.get("${NetworkInterface.Endpoints.User.url}/$userId/name"){
                headers {
                    append(Authorization, "Bearer ${MainViewModel.state.token}")
                }
            }
        } catch (e: Exception){
            e.printStackTrace()
            ""
        }
    }

    override suspend fun getUserById(userId: Int): User? {
        return try {
            client.get("${NetworkInterface.Endpoints.User.url}/$userId"){
                headers {
                    append(Authorization, "Bearer ${MainViewModel.state.token}")
                }
            }
        } catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

    override suspend fun getUserByEmail(email: String): User? {
        return try {
            client.get(NetworkInterface.Endpoints.User.url){
                parameter("email",email)
                    headers {
                        append(Authorization, "Bearer ${MainViewModel.state.token}")
                    }
            }
        } catch (e: Exception){
            e.printStackTrace()
            throw e
        }
    }
}