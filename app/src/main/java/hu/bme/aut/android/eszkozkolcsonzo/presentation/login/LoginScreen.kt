package hu.bme.aut.android.eszkozkolcsonzo.presentation.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
import hu.bme.aut.android.eszkozkolcsonzo.MainViewModel
import hu.bme.aut.android.eszkozkolcsonzo.presentation.navigation.Screen

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavHostController,
    target: String
) {
    val state = viewModel.state
    val context = LocalContext.current
    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is LoginViewModel.LoginScreenEvent.LoginSuccess -> {
                    Toast.makeText(
                        context,
                        "Sikeres bejelentkezés",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate(target)
                }
                is LoginViewModel.LoginScreenEvent.LoginFailed -> {
                    Toast.makeText(
                        context,
                        "Nem megfelelő email vagy jelszó",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is LoginViewModel.LoginScreenEvent.LogoutFailed -> {
                    Toast.makeText(
                        context,
                        "Sikertelen kijelentkezés",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate(Screen.DeviceListScreen.route)
                }
                is LoginViewModel.LoginScreenEvent.LogoutSuccess -> {
                    Toast.makeText(
                        context,
                        "Sikeres kijelentkezés",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    if (MainViewModel.state.user == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            Text(
                text = "Kérlek jelentkezz be",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Email:")
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = state.email,
                isError = state.emailError != null,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                onValueChange = { viewModel.onEvent(LoginEvent.EmailChanged(it)) }
            )
            if (state.emailError != null) {
                Text(
                    text = state.emailError,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.align(Alignment.End)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Jelszó:")
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = state.password,
                isError = state.passwordError != null,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = { viewModel.onEvent(LoginEvent.PasswordChanged(it)) }
            )
            if (state.passwordError != null) {
                Text(
                    text = state.passwordError,
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
                    checked = state.stayLoggedIn,
                    onCheckedChange = {
                        viewModel.onEvent(LoginEvent.StayLoggedInCheckBoxChanged(it))
                    }
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Maradjak bejelentkezve")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.onEvent(LoginEvent.Login)
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Belépés")
            }
        }
    }else if(MainViewModel.state.user != null && !state.isLoading){
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(LoginEvent.CancelLogout) },
            title = {
                Text(text = "Figyelem!")
            },
            text = {
                Text("Biztos ki szeretnél jelentkezni?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.onEvent(LoginEvent.Logout)
                    }) {
                    Text("Igen")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        viewModel.onEvent(LoginEvent.CancelLogout)
                    }) {
                    Text("Nem")
                }
            }
        )
    }else if (state.isLoading){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
