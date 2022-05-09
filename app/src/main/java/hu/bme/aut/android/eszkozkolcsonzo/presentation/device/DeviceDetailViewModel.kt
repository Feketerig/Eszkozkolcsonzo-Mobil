package hu.bme.aut.android.eszkozkolcsonzo.presentation.device

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.eszkozkolcsonzo.MainViewModel
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.Reservation
import hu.bme.aut.android.eszkozkolcsonzo.domain.repository.DeviceRepository
import hu.bme.aut.android.eszkozkolcsonzo.domain.repository.ReservationRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceDetailViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val reservationRepository: ReservationRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    var state by mutableStateOf(DeviceDetailState())

    private val validationEventChannel = Channel<DetailEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val id = savedStateHandle.get<Int>("id") ?: return@launch
            state = state.copy(isLoading = true)
            state = state.copy(device = deviceRepository.getDevice(id).data)
            state = state.copy(isLoading = false)
        }
    }

    fun setStartDate(date: Long){
        state = state.copy(startDate = date)
    }

    fun setEndDate(date: Long){
        state = state.copy(endDate = date)
    }

    fun reserve(){
        if (state.startDate == null){
            state = state.copy(error = "A kezdődátum nincs megadva")
            return
        }
        if (state.endDate == null){
            state = state.copy(error = "A végdátum nincs megadva")
            return
        }
        if (state.startDate!! > state.endDate!!){
            state = state.copy(error = "A kezdődátum nem lehet előrébb a végdátumnál")
            return
        }
        if (MainViewModel.state.user == null){
            state = state.copy(error = "Nem vagy bejelentkezve")
            return
        }
        viewModelScope.launch {
            reservationRepository.addReservation(
                Reservation(
                    1,
                    savedStateHandle.get<Int>("id") ?: 0,
                    state.startDate!!,
                    state.endDate!!,
                    userId = MainViewModel.state.user?.id!!
                )
            )
            validationEventChannel.send(DetailEvent.Succes)
        }
    }

    fun resetError(){
        state = state.copy(error = null)
    }

    sealed class DetailEvent{
        object Succes: DetailEvent()
    }
}
