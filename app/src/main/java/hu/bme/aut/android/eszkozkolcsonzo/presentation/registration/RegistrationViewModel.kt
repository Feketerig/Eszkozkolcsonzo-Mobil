package hu.bme.aut.android.eszkozkolcsonzo.presentation.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.eszkozkolcsonzo.MainViewModel
import hu.bme.aut.android.eszkozkolcsonzo.data.network.NetworkInterface
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.User
import hu.bme.aut.android.eszkozkolcsonzo.domain.use_case.ValidateEmail
import hu.bme.aut.android.eszkozkolcsonzo.domain.use_case.ValidatePassword
import hu.bme.aut.android.eszkozkolcsonzo.domain.use_case.ValidateRepeatedPassword
import hu.bme.aut.android.eszkozkolcsonzo.domain.use_case.ValidateTerms
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val validateRepeatedPassword: ValidateRepeatedPassword,
    private val validateTerms: ValidateTerms,
    private val network: NetworkInterface
): ViewModel() {
    var state by mutableStateOf(RegistrationState())

    private val validationEventChannel = Channel<RegistrationValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: RegistrationEvent) {
        when(event) {
            is RegistrationEvent.EmailChanged -> {
                val emailResult = validateEmail.execute(event.email)
                state = state.copy(email = event.email, emailError = emailResult.errorMessage)
            }
            is RegistrationEvent.PasswordChanged -> {
                val passwordResult = validatePassword.execute(event.password)
                state = state.copy(password = event.password, passwordError = passwordResult.errorMessage)
            }
            is RegistrationEvent.RepeatedPasswordChanged -> {
                val repeatedPasswordResult = validateRepeatedPassword.execute(
                    state.password, event.repeatedPassword
                )
                state = state.copy(repeatedPassword = event.repeatedPassword, repeatedPasswordError = repeatedPasswordResult.errorMessage)
            }
            is RegistrationEvent.AcceptTerms -> {
                val termsResult = validateTerms.execute(event.isAccepted)
                state = state.copy(acceptedTerms = event.isAccepted, termsError = termsResult.errorMessage)
            }
            is RegistrationEvent.Submit -> {
                submitData()
            }
            is RegistrationEvent.AddressChanged -> {
                state = state.copy(address = event.address)
            }
            is RegistrationEvent.IsAdminChanged -> {
                state = state.copy(isAdmin = event.isAdmin)
            }
            is RegistrationEvent.PhoneChanged -> {
                state = state.copy(phone = event.phone)
            }
            is RegistrationEvent.UserNameChanged -> {
                state = state.copy(userName = event.userName)
            }
        }
    }

    private fun submitData() {
        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password)
        val repeatedPasswordResult = validateRepeatedPassword.execute(
            state.password, state.repeatedPassword
        )
        val termsResult = validateTerms.execute(state.acceptedTerms)

        val hasError = listOf(
            emailResult,
            passwordResult,
            repeatedPasswordResult,
            termsResult
        ).any { !it.successful }

        if(hasError) {
            state = state.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                repeatedPasswordError = repeatedPasswordResult.errorMessage,
                termsError = termsResult.errorMessage
            )
            return
        }
        viewModelScope.launch {
            try {
                network.registration(
                    email = state.email,
                    name = state.userName,
                    phone = state.phone,
                    address = state.address,
                    password = state.password
                )
                val token = network.login(state.email, state.password)
                val jwt = JWT(token)
                val id = jwt.getClaim("id").asInt()
                val name = jwt.getClaim("name").asString()
                val email = jwt.getClaim("email").asString()
                val privilege = when(jwt.getClaim("priv").asString()){
                    "Admin" -> User.Privilege.Admin
                    "User" -> User.Privilege.User
                    "Handler" -> User.Privilege.Handler
                    else -> User.Privilege.User
                }
                MainViewModel.state = MainViewModel.state.copy(id = id, name = name, email = email, privilege = privilege, token = token, password = state.password)
                validationEventChannel.send(RegistrationValidationEvent.Success)
            }catch (e: Exception){
                validationEventChannel.send(RegistrationValidationEvent.Failed)
            }
        }
    }

    sealed class RegistrationValidationEvent {
        object Success: RegistrationValidationEvent()
        object Failed: RegistrationValidationEvent()
    }
}