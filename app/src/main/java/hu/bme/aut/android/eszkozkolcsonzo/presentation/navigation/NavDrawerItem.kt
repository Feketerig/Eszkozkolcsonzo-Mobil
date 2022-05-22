package hu.bme.aut.android.eszkozkolcsonzo.presentation.navigation

sealed class NavDrawerItem(var route: String, var title: String) {
    object Devices: NavDrawerItem(Screen.DeviceListScreen.route, "Eszköz lista")
    object Lease: NavDrawerItem(Screen.CameraScreen.route+"/lease", "Eszköz kiadás")
    object Drawback: NavDrawerItem(Screen.CameraScreen.route+"/drawback", "Eszköz visszavétel")
    object Login: NavDrawerItem(Screen.LoginScreen.route+"/login", "Bejelentkezés")
    object Logout: NavDrawerItem(Screen.LoginScreen.route+"/logout", "Kijelentkezés")
    object Registration: NavDrawerItem(Screen.RegistrationScreen.route, "Regisztráció")
    object ReservationList: NavDrawerItem(Screen.ReservationListScreen.route, "Foglalásaim")
    object CreateDevice: NavDrawerItem(Screen.CreateDeviceScreen.route, "Eszköz felvétel")
}
