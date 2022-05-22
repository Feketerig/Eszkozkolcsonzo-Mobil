package hu.bme.aut.android.eszkozkolcsonzo.presentation.registration

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import hu.bme.aut.android.eszkozkolcsonzo.presentation.navigation.Screen

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val state = viewModel.state
    val context = LocalContext.current
    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is RegistrationViewModel.RegistrationValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        "Sikeres regisztráció",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate(Screen.DeviceListScreen.route)
                }
                is RegistrationViewModel.RegistrationValidationEvent.Failed -> {
                    Toast.makeText(
                        context,
                        "Sikerestelen regisztráció",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Add meg az adataidat a regisztrációhoz",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = state.email,
            onValueChange = {
                viewModel.onEvent(RegistrationEvent.EmailChanged(it))
            },
            isError = state.emailError != null,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Email")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )
        if (state.emailError != null) {
            Text(
                text = state.emailError,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = state.userName,
            onValueChange = {
                viewModel.onEvent(RegistrationEvent.UserNameChanged(it))
            },
            isError = state.userNameError != null,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Név")
            }
        )
        if (state.userNameError != null) {
            Text(
                text = state.userNameError,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = state.phone,
            onValueChange = {
                viewModel.onEvent(RegistrationEvent.PhoneChanged(it))
            },
            isError = state.phoneError != null,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Telefonszám")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            )
        )
        if (state.phoneError != null) {
            Text(
                text = state.phoneError,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
        /*Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = state.address,
            onValueChange = {
                viewModel.onEvent(RegistrationEvent.AddressChanged(it))
            },
            isError = state.addressError != null,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Lakcím")
            }
        )
        if (state.addressError != null) {
            Text(
                text = state.addressError,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.End)
            )
        }*/
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = state.password,
            onValueChange = {
                viewModel.onEvent(RegistrationEvent.PasswordChanged(it))
            },
            isError = state.passwordError != null,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Jelszó")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = PasswordVisualTransformation()
        )
        if (state.passwordError != null) {
            Text(
                text = state.passwordError,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = state.repeatedPassword,
            onValueChange = {
                viewModel.onEvent(RegistrationEvent.RepeatedPasswordChanged(it))
            },
            isError = state.repeatedPasswordError != null,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Jelszó mégegyszer")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = PasswordVisualTransformation()
        )
        if (state.repeatedPasswordError != null) {
            Text(
                text = state.repeatedPasswordError,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = state.acceptedTerms,
                onCheckedChange = {
                    viewModel.onEvent(RegistrationEvent.AcceptTerms(it))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Felhasználási feltételek")
        }
        if (state.termsError != null) {
            Text(
                text = state.termsError,
                color = MaterialTheme.colors.error,
            )
        }

        Button(
            onClick = {
                viewModel.onEvent(RegistrationEvent.Submit)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Regisztráció")
        }
    }
}