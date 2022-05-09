package hu.bme.aut.android.eszkozkolcsonzo.presentation.devicecreation

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.eszkozkolcsonzo.presentation.navigation.Screen

@Composable
fun CreateDevice(
    viewModel: CreateDeviceViewModel = hiltViewModel(),
    navController: NavController
) {

    val state = viewModel.state
    val context = LocalContext.current
    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is CreateDeviceViewModel.CreateDeviceEvent.Succes -> {
                    Toast.makeText(
                        context,
                        "Sikeres eszköz felvétel",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate(Screen.DeviceListScreen.route)
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Text(
            text = "Kérlek add meg az eszköz részleteit",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Eszköz neve:")
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = state.deviceName,
            isError = state.deviceName.isEmpty(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            onValueChange = { viewModel.onNameChange(it) }
        )
        if (state.deviceNameError.isNotEmpty()) {
            Text(
                text = state.deviceNameError,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.End)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Eszköz leírása:")
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = state.deviceDesc,
            isError = state.deviceDesc.isEmpty(),
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { viewModel.onDescChange(it) }
        )
        if (state.deviceDescError.isNotEmpty()) {
            Text(
                text = state.deviceDescError,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.End)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.submit()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Létrehozás")
        }
    }
}