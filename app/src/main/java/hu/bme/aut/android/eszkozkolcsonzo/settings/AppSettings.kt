package hu.bme.aut.android.eszkozkolcsonzo.settings

import hu.bme.aut.android.eszkozkolcsonzo.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val id: Int? = null,
    val name: String? = null,
    val email: String? = null,
    val privilege: User.Privilege? = null,
    val stayLogin: Boolean = false,
    val password: String? = null,
    val token: String? = null
)