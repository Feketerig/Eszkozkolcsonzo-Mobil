package hu.bme.aut.android.eszkozkolcsonzo.presentation.devicecreation

data class CreateDeviceState(
    val deviceName: String = "",
    val deviceNameError: String = "",
    val deviceDesc: String = "",
    val deviceDescError: String = "",
    val available: Boolean = true
)