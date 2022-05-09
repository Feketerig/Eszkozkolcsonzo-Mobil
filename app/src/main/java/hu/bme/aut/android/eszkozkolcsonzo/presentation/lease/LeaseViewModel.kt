package hu.bme.aut.android.eszkozkolcsonzo.presentation.lease

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.eszkozkolcsonzo.MainViewModel
import hu.bme.aut.android.eszkozkolcsonzo.data.network.NetworkInterface
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.Lease
import hu.bme.aut.android.eszkozkolcsonzo.domain.repository.DeviceRepository
import hu.bme.aut.android.eszkozkolcsonzo.domain.repository.ReservationRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaseViewModel @Inject constructor(
    val network: NetworkInterface,
    val reservationRepository: ReservationRepository,
    val deviceRepository: DeviceRepository
): ViewModel() {

    var state by mutableStateOf(LeaseState())

    private val validationEventChannel = Channel<LeaseScreenEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: LeaseEvent) {
        when(event) {
            is LeaseEvent.FindDevice -> {
                viewModelScope.launch {
                    val device = network.getDevice(event.id)
                    val reservation = network.getReservationByDeviceId(event.id)
                    val user = network.getUserById(reservation?.userId  ?: 0)

                    state = state.copy(
                        device = device,
                        reservation = reservation,
                        reservationUser = user,
                        showCamera = false,
                        isDevice = false
                    )
                }
                /*state = state.copy(
                    device = Device(1, "Telefon", "Ez egy telefon", true),
                    reservation = Reservation(1,1,61611580800000,61611667200000,1),
                    reservationUser = User(1,"Fazekas Levente", "+36306172491", "Valahol", "levente.fazekas@gmail.com", User.Privilege.User),
                    showCamera = false, isDevice = false
                )*/
            }
            is LeaseEvent.FindUSer -> {
                viewModelScope.launch {
                    val user = network.getUserById(event.id)
                    state = state.copy(
                        user = user,
                        showCamera = false,
                        isUser = false
                    )
                }
                /*state = state.copy(
                    user = User(1,"Fazekas Levente", "+36306172491", "Valahol", "levente.fazekas@gmail.com", User.Privilege.User),
                    showCamera = false, isUser = false
                )*/
            }
            is LeaseEvent.StartLease ->{
                viewModelScope.launch {
                    network.addLease(lease = Lease(1,state.reservation!!, MainViewModel.state.user!!, state.user!!, true))
                    validationEventChannel.send(LeaseScreenEvent.LeaseSuccess)
                }
            }
            is LeaseEvent.StartDrawback ->{
                viewModelScope.launch {
                    val id = network.getLeaseIdByReservationId(state.reservation!!.id)
                    network.deactivateLease(id)
                    validationEventChannel.send(LeaseScreenEvent.DrawbackSuccess)
                }
            }
            is LeaseEvent.ScanDevice -> {
                state = state.copy(showCamera = true, isDevice = true)
            }
            is LeaseEvent.ScanUser -> {
                state = state.copy(showCamera = true, isUser = true)
            }
        }
    }

    sealed class LeaseScreenEvent {
        object LeaseSuccess : LeaseScreenEvent()
        object DrawbackSuccess : LeaseScreenEvent()
    }

}