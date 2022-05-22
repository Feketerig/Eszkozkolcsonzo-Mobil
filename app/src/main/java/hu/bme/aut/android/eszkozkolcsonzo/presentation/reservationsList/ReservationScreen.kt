package hu.bme.aut.android.eszkozkolcsonzo.presentation.reservationsList

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import hu.bme.aut.android.eszkozkolcsonzo.MainViewModel

@Composable
fun ReservationScreen(
    viewModel: ReservationViewModel = hiltViewModel(),
    navController: NavHostController,
    target: String
) {

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = viewModel.state.isRefreshing
    )

    val state = viewModel.state
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is ReservationViewModel.ValidationEvent.NotLoggedIn -> {
                    Toast.makeText(
                        context,
                        "Az foglalások megtekintéséhez be kell jelentkezned",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate(target)
                }
            }
        }
    }

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            viewModel.getReservations(MainViewModel.state.id)
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.reservations.size) { index ->
                val reservation = state.reservations[index]
                if(viewModel.isAdmin()){
                    ReservationCard(
                        deviceName = reservation.deviceName,
                        userName = reservation.userName,
                        startDate = reservation.startDate,
                        endDate = reservation.endDate
                    )
                }else{
                    Text(text = reservation.toString())
                    if(index < state.reservations.size - 1) {
                        Divider(modifier = Modifier.padding(
                            horizontal = 16.dp
                        ))
                    }
                }
            }
        }
        if (state.reservations.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sajnos nem találtunk egy foglalást sem"
                )
            }
        }
    }

}