package hu.bme.aut.android.eszkozkolcsonzo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
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
                    id = it.id,
                    name = it.name,
                    email = it.email,
                    stayLogin = it.stayLogin,
                    privilege = it.privilege,
                    password = it.password,
                    token = it.token
                )
            }
            if (MainViewModel.state.stayLogin) {
                val token =
                    network.login(MainViewModel.state.email!!, MainViewModel.state.password!!)
                val jwt = JWT(token)
                val id = jwt.getClaim("id").asInt()
                val name = jwt.getClaim("name").asString()
                val email = jwt.getClaim("email").asString()
                val privilege = when (jwt.getClaim("priv").asString()) {
                    "Admin" -> User.Privilege.Admin
                    "User" -> User.Privilege.User
                    "Handler" -> User.Privilege.Handler
                    else -> User.Privilege.User
                }
                MainViewModel.state = MainViewModel.state.copy(
                    id = id,
                    name = name,
                    email = email,
                    privilege = privilege,
                    token = token,
                )
            }
        }
    }

    fun isAdmin(): Boolean {
        return state.privilege == User.Privilege.Admin
    }
}