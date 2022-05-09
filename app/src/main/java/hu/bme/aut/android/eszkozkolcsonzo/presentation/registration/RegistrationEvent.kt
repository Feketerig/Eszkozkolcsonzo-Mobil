package hu.bme.aut.android.eszkozkolcsonzo.presentation.registration

sealed class RegistrationEvent {
    data class EmailChanged(val email: String) : RegistrationEvent()
    data class UserNameChanged(val userName: String): RegistrationEvent()
    data class PhoneChanged(val phone: String): RegistrationEvent()
    data class AddressChanged(val address: String): RegistrationEvent()
    data class PasswordChanged(val password: String) : RegistrationEvent()
    data class RepeatedPasswordChanged(
        val repeatedPassword: String
    ) : RegistrationEvent()

    data class IsAdminChanged(val isAdmin: Boolean): RegistrationEvent()
    data class AcceptTerms(val isAccepted: Boolean) : RegistrationEvent()

    object Submit: RegistrationEvent()
}