package hu.bme.aut.android.eszkozkolcsonzo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.eszkozkolcsonzo.data.network.NetworkInterface
import hu.bme.aut.android.eszkozkolcsonzo.data.repository.AppSettingsRepositoryImpl
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.User
import hu.bme.aut.android.eszkozkolcsonzo.settings.AppSettings
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appSettingsRepositoryImpl: AppSettingsRepositoryImpl,
    network: NetworkInterface
): ViewModel() {

    companion object LoggedInUser{
        var state by mutableStateOf(AppSettings())
    }

    init {
        viewModelScope.launch {
            appSettingsRepositoryImpl.get().collect{
                state = state.copy(
                    email = it.email,
                    stayLogin = it.stayLogin,
                    password = it.password,
                )
            }
        }
    }

    fun isAdmin(): Boolean {
        return state.privilege == User.Privilege.Admin
    }
}