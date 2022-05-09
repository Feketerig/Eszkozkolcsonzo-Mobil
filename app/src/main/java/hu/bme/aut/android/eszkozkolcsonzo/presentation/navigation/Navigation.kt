package hu.bme.aut.android.eszkozkolcsonzo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import hu.bme.aut.android.eszkozkolcsonzo.presentation.device.DeviceDetail
import hu.bme.aut.android.eszkozkolcsonzo.presentation.devicecreation.CreateDevice
import hu.bme.aut.android.eszkozkolcsonzo.presentation.devicelist.DeviceListScreen
import hu.bme.aut.android.eszkozkolcsonzo.presentation.lease.LeaseScreen
import hu.bme.aut.android.eszkozkolcsonzo.presentation.login.LoginScreen
import hu.bme.aut.android.eszkozkolcsonzo.presentation.registration.RegistrationScreen
import hu.bme.aut.android.eszkozkolcsonzo.presentation.reservationsList.ReservationScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.DeviceListScreen.route
    ){
        composable(route = Screen.DeviceListScreen.route){
            DeviceListScreen(
                onNavigate = navController::navigate
            )
        }
        composable(
            route = Screen.DeviceDetailScreen.route+"/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.IntType
                    nullable = false
                }
            )
        ){
            it.arguments?.getInt("id")?.let { it1 ->
                DeviceDetail(it1)
            }
        }
        composable(route = Screen.CameraScreen.route+"/lease"){
            LeaseScreen(mode ="lease", navController = navController)
        }
        composable(route = Screen.CameraScreen.route+"/drawback"){
            LeaseScreen(mode = "drawback", navController = navController)
        }
        composable(route = Screen.LoginScreen.route){
            LoginScreen(navController = navController, target = Screen.DeviceListScreen.route)
        }
        composable(route = Screen.ReservationListScreen.route){
            ReservationScreen(navController = navController, target = Screen.LoginScreen.route)
        }
        composable(route = Screen.RegistrationScreen.route){
            RegistrationScreen(navController = navController)
        }
        composable(route = Screen.CreateDeviceScreen.route){
            CreateDevice(navController = navController)
        }
    }
}