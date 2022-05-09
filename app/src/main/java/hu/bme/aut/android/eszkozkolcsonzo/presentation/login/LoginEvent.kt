package hu.bme.aut.android.eszkozkolcsonzo.presentation.login

sealed class LoginEvent{
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    data class StayLoggedInCheckBoxChanged(val stayLoggedIn: Boolean): LoginEvent()

    object Login: LoginEvent()
    object CancelLogout: LoginEvent()
    object Logout: LoginEvent()
}
