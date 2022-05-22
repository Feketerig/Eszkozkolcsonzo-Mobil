package hu.bme.aut.android.eszkozkolcsonzo.presentation.devicelist

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import hu.bme.aut.android.eszkozkolcsonzo.presentation.device.DeviceListItemCard
import hu.bme.aut.android.eszkozkolcsonzo.presentation.navigation.Screen

@Composable
fun DeviceListScreen(
    viewModel: DeviceListViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit,
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
                is DeviceListViewModel.ValidationEvent.NotLoggedIn -> {
                    Toast.makeText(
                        context,
                        "Az eszközök megtekintéséhez be kell jelentkezned",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate(target)
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            TextField(
                value = state.searchQuery,
                onValueChange = {
                    viewModel.onSearchTextChange(it)
                },
                placeholder = {
                    Text(text = "Keresés...")
                },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Column(
                modifier = Modifier.height(60.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { viewModel.getDevices(true) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Keresés", fontSize = 10.sp)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Checkbox(
                        checked = state.onlyAvailable,
                        onCheckedChange = {viewModel.onCheckedChange(it)},
                        enabled = true
                    )
                    Text(
                        text = "Elérhető",
                        fontSize = 10.sp
                    )
                }
            }

        }
        Spacer(modifier = Modifier.height(10.dp))
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.getDevices(true)
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.devices.size) { index ->
                    val device = state.devices[index]
                    DeviceListItemCard(
                        name = device.name,
                        desc = device.desc,
                        //painterResource(id = R.drawable.ic_launcher_foreground)
                    ) {
                        onNavigate(Screen.DeviceDetailScreen.route + "/" + device.id)
                    }
                    /*if(index < state.devices.size - 1) {
                        Divider(modifier = Modifier.padding(
                            horizontal = 16.dp
                        ))
                    }*/
                }
            }
        }
    }
}