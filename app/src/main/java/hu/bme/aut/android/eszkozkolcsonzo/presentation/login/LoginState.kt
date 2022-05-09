package hu.bme.aut.android.eszkozkolcsonzo.presentation.login

data class LoginState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val stayLoggedIn: Boolean = false,
    val isLoading: Boolean = false
)
