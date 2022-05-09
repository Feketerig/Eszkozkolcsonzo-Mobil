package hu.bme.aut.android.eszkozkolcsonzo.presentation.reservationsList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.eszkozkolcsonzo.MainViewModel
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.User
import hu.bme.aut.android.eszkozkolcsonzo.domain.repository.ReservationRepository
import hu.bme.aut.android.eszkozkolcsonzo.util.Resource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationViewModel @Inject constructor(
    val repository: ReservationRepository
): ViewModel() {

    var state by mutableStateOf(ReservationListState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            if (MainViewModel.state.user == null){
                validationEventChannel.send(ValidationEvent.NotLoggedIn)
                return@launch
            }
            getReservations(MainViewModel.state.user?.id)
        }
    }

    fun getReservations(
        id: Int?
    ){
        viewModelScope.launch {
            repository.getReservations(id)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { list ->
                                state = state.copy(reservations = list)
                            }
                        }
                        is Resource.Error -> {
                        }
                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }

    fun isAdmin(): Boolean{
        return MainViewModel.state.user?.privilege == User.Privilege.Admin
    }

    sealed class ValidationEvent {
        object NotLoggedIn: ValidationEvent()
    }
}