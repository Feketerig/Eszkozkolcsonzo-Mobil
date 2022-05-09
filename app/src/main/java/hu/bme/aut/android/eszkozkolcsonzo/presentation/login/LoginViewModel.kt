package hu.bme.aut.android.eszkozkolcsonzo.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.eszkozkolcsonzo.MainViewModel
import hu.bme.aut.android.eszkozkolcsonzo.data.network.NetworkInterface
import hu.bme.aut.android.eszkozkolcsonzo.data.repository.AppSettingsRepositoryImpl
import hu.bme.aut.android.eszkozkolcsonzo.domain.use_case.ValidateEmail
import hu.bme.aut.android.eszkozkolcsonzo.domain.use_case.ValidatePassword
import hu.bme.aut.android.eszkozkolcsonzo.settings.AppSettings
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val network: NetworkInterface,
    private val appSettingsRepository: AppSettingsRepositoryImpl
): ViewModel() {

    var state by mutableStateOf(LoginState())

    private val validationEventChannel = Channel<LoginScreenEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.EmailChanged -> {
                val emailResult = validateEmail.execute(event.email)
                state = state.copy(email = event.email, emailError = emailResult.errorMessage)
            }
            is LoginEvent.PasswordChanged -> {
                val passwordResult = validatePassword.execute(event.password)
                state = state.copy(password = event.password, passwordError = passwordResult.errorMessage)
            }
            is LoginEvent.StayLoggedInCheckBoxChanged -> {
                state = state.copy(stayLoggedIn = event.stayLoggedIn)
            }
            is LoginEvent.Login -> {
                state = state.copy(isLoading = true)
                submitData()
            }
            is LoginEvent.CancelLogout -> {
                cancelLogout()
            }
            is LoginEvent.Logout -> {
                logout()
            }
        }
    }

    private fun submitData() {

        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password)

        val hasError = listOf(
            emailResult,
            passwordResult
        ).any { !it.successful }

        if(hasError) {
            state = state.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
            return
        }
        viewModelScope.launch {
            try {
                val user = network.login(state.email, state.password)
                MainViewModel.state = MainViewModel.state.copy(user = user)
                if(state.stayLoggedIn){
                    appSettingsRepository.set(AppSettings(user = user, stayLogin = true))
                }else{
                    appSettingsRepository.set(AppSettings())
                }
                validationEventChannel.send(LoginScreenEvent.LoginSuccess)
            }catch (e: Exception){
                validationEventChannel.send(LoginScreenEvent.LoginFailed)
            }
        }
    }

    private fun cancelLogout(){
        viewModelScope.launch {
            validationEventChannel.send(LoginScreenEvent.LogoutFailed)
        }
    }

    private fun logout(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            MainViewModel.state = MainViewModel.state.copy(user = null, stayLogin = false)
            appSettingsRepository.set(AppSettings())
            validationEventChannel.send(LoginScreenEvent.LogoutSuccess)
            state = state.copy(isLoading = false)
        }
    }

    sealed class LoginScreenEvent {
        object LoginSuccess: LoginScreenEvent()
        object LoginFailed: LoginScreenEvent()
        object LogoutSuccess: LoginScreenEvent()
        object LogoutFailed: LoginScreenEvent()
    }
}