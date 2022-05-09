package hu.bme.aut.android.eszkozkolcsonzo.presentation.registration

data class RegistrationState(
    val email: String = "",
    val emailError: String? = null,
    val userName: String = "",
    val userNameError: String? = null,
    val phone: String = "",
    val phoneError: String? = null,
    val address: String = "",
    val addressError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val repeatedPassword: String = "",
    val repeatedPasswordError: String? = null,
    val isAdmin: Boolean = true,
    val acceptedTerms: Boolean = false,
    val termsError: String? = null
)