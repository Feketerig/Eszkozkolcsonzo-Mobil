package hu.bme.aut.android.eszkozkolcsonzo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.eszkozkolcsonzo.data.repository.AppSettingsRepositoryImpl
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.User
import hu.bme.aut.android.eszkozkolcsonzo.settings.AppSettings
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appSettingsRepositoryImpl: AppSettingsRepositoryImpl
): ViewModel() {

    companion object LoggedInUser{
        var state by mutableStateOf(AppSettings())
    }

    init {
        viewModelScope.launch {
            appSettingsRepositoryImpl.get().collect{
                state = state.copy(user = it.user, stayLogin = it.stayLogin)
            }
        }

    }

    /*private val _user = MutableLiveData(User(1, "Admin", "", "", "", User.Privilege.Admin))
    val user: LiveData<User> = _user

    init {
        viewModelScope.launch {
            appSettingsRepositoryImpl.getUser().collect {
                _user.value = it
            }
        }
    }*/

    fun isAdmin(): Boolean {
        return state.user?.privilege == User.Privilege.Admin
    }
}