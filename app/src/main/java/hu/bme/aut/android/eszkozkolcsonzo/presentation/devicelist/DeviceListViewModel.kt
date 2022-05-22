package hu.bme.aut.android.eszkozkolcsonzo.presentation.devicelist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.eszkozkolcsonzo.MainViewModel
import hu.bme.aut.android.eszkozkolcsonzo.domain.repository.DeviceRepository
import hu.bme.aut.android.eszkozkolcsonzo.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceListViewModel @Inject constructor(
    private val repository: DeviceRepository
) : ViewModel() {

    var state by mutableStateOf(DeviceListState())

    private var searchJob: Job? = null

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            if (MainViewModel.state.token == null) {
                validationEventChannel.send(ValidationEvent.NotLoggedIn)
                return@launch
            }
            getDevices(true)
        }
    }

    fun getDevices(
        forceRefresh: Boolean = false,
        query: String = state.searchQuery.lowercase()
    ) {
        viewModelScope.launch {
            repository.getDevices(forceRefresh, state.onlyAvailable, query)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { list ->
                                state = state.copy(devices = list)
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

    fun onSearchTextChange(newValue: String){
        state = state.copy(searchQuery = newValue)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            getDevices()
        }
    }

    fun onCheckedChange(newValue: Boolean){
        state = state.copy(onlyAvailable = newValue)
        getDevices(forceRefresh = true)
    }

    sealed class ValidationEvent {
        object NotLoggedIn: ValidationEvent()
    }
}