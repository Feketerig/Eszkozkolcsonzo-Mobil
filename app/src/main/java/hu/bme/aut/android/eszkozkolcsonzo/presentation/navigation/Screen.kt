package hu.bme.aut.android.eszkozkolcsonzo.presentation.navigation

sealed class Screen(val route: String) {
    object DeviceListScreen: Screen("device_list")
    object DeviceDetailScreen: Screen("device_detail")
    object LoginScreen: Screen("login")
    object RegistrationScreen: Screen("registration")
    object CameraScreen: Screen("camera")
    object ReservationListScreen: Screen("lease_list")
    object CreateDeviceScreen: Screen("create_device")
}