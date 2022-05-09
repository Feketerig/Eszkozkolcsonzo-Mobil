package hu.bme.aut.android.eszkozkolcsonzo.presentation.lease

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.eszkozkolcsonzo.presentation.camera.CameraScreen
import hu.bme.aut.android.eszkozkolcsonzo.presentation.navigation.Screen
import hu.bme.aut.android.eszkozkolcsonzo.util.QRCodeSchema
import java.util.*

@Composable
fun LeaseScreen(
    mode: String,
    navController: NavController,
    viewModel: LeaseViewModel = hiltViewModel()
) {
    val state = viewModel.state

    val context = LocalContext.current
    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is LeaseViewModel.LeaseScreenEvent.LeaseSuccess -> {
                    Toast.makeText(
                        context,
                        "Sikeres foglalás",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate(Screen.DeviceListScreen.route)
                }
                is LeaseViewModel.LeaseScreenEvent.DrawbackSuccess -> {
                    Toast.makeText(
                        context,
                        "Sikeres visszavétel",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate(Screen.DeviceListScreen.route)
                }
            }
        }
    }

    if(state.showCamera && state.isDevice){
        //viewModel.onEvent(LeaseEvent.FindDevice(1))
        CameraScreen(onResult = {
            if (it.isNotEmpty() && it.contains(QRCodeSchema.Device)){
                val id = it.substringAfterLast("/").toInt()
                viewModel.onEvent(LeaseEvent.FindDevice(id))
            }
        })
    }else if(state.showCamera && state.isUser){
        //viewModel.onEvent(LeaseEvent.FindUSer(1))
        CameraScreen(onResult = {
            if (it.isNotEmpty() && it.contains(QRCodeSchema.User)){
                val id = it.substringAfterLast("/").toInt()
                viewModel.onEvent(LeaseEvent.FindUSer(id))
            }
        })
    } else {
        var startDate: Date? = null
        var endDate: Date? = null
        if (state.reservation != null) {
            startDate = Date(state.reservation.startDate)
            endDate = Date(state.reservation.endDate)
        }
        val startDateText =
            "${startDate?.year ?: ""}-${startDate?.month?.plus(1) ?: ""}-${startDate?.day ?: ""}"
        val endDateText =
            "${endDate?.year ?: ""}-${endDate?.month?.plus(1) ?: ""}-${endDate?.day ?: ""}"
        LeaseDetail(
            mode = mode,
            device = state.device,
            reservationUser = state.reservationUser,
            actualUser = state.user,
            startDate = startDateText,
            endDate = endDateText,
            onDeviceScan = { viewModel.onEvent(LeaseEvent.ScanDevice) },
            onUserScan = { viewModel.onEvent(LeaseEvent.ScanUser) },
            onSubmit = { if (mode == "lease") viewModel.onEvent(LeaseEvent.StartLease) else viewModel.onEvent(LeaseEvent.StartDrawback) }
        )
    }
}