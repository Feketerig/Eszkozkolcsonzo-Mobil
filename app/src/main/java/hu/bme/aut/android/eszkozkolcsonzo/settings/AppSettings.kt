package hu.bme.aut.android.eszkozkolcsonzo.settings

import hu.bme.aut.android.eszkozkolcsonzo.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val user: User? = null,
    val stayLogin: Boolean = false
)