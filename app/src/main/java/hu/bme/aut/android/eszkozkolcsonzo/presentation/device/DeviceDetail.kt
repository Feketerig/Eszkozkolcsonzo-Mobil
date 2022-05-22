package hu.bme.aut.android.eszkozkolcsonzo.presentation.device

import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import hu.bme.aut.android.eszkozkolcsonzo.R
import java.util.*


@Composable
fun DeviceDetail(
    id: Int,
    viewModel: DeviceDetailViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()
    val mYear = mCalendar.get(Calendar.YEAR)
    val mMonth = mCalendar.get(Calendar.MONTH)
    val mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
    val startDateText = remember { mutableStateOf("") }
    val endDateText = remember { mutableStateOf("") }

    val state = viewModel.state

    val context = LocalContext.current
    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is DeviceDetailViewModel.DetailEvent.Success -> {
                    Toast.makeText(
                        context,
                        "Sikeres foglalás",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.popBackStack()
                }
            }
        }
    }

    val startDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            val startDate = Date(mYear, mMonth+1, mDayOfMonth)
            startDateText.value  = "$mYear-${mMonth+1}-$mDayOfMonth"
            viewModel.setStartDate(startDate.time)
        }, mYear, mMonth, mDay
    )
    val endDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            val endDate = Date(mYear, mMonth+1, mDayOfMonth)
            endDateText.value = "$mYear-${mMonth+1}-$mDayOfMonth"
            viewModel.setEndDate(endDate.time)
        }, mYear, mMonth, mDay
    )

    if(state.error == null) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = state.device?.name ?: "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = state.device?.name ?: "",
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = state.device?.desc ?: "")
            Spacer(modifier = Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth()) {
                Button(onClick = { startDatePickerDialog.show() }) {
                    Text(text = "Kiadás dátuma: ${startDateText.value}")
                }
            }
            Row(Modifier.fillMaxWidth()) {
                Button(onClick = { endDatePickerDialog.show() }) {
                    Text(text = "Visszaadás dátuma: ${endDateText.value}")
                }
            }
            Button(
                onClick = { viewModel.reserve() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Foglalás")
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if(state.isLoading) {
            CircularProgressIndicator()
        } else if(state.error != null) {
            AlertDialog(
                onDismissRequest = {
                   viewModel.resetError()
                },
                title = {
                    Text(text = "Hiba")
                },
                text = {
                    Text(state.error)
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.resetError()
                        }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}