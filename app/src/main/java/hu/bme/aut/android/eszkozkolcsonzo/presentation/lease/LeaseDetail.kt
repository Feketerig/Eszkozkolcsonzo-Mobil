package hu.bme.aut.android.eszkozkolcsonzo.presentation.lease

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.Device
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.User

@Composable
fun LeaseDetail(
    mode: String,
    device: Device?,
    reservationUser: User?,
    actualUser: User?,
    startDate: String?,
    endDate: String?,
    onDeviceScan: () -> Unit,
    onUserScan: () -> Unit,
    onSubmit: () -> Unit
) {
    val leaseMode = mode == "lease"
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = device?.name ?: "",
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = device?.desc ?: "")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Kiadás dátuma: $startDate")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Visszaadás dátuma: $endDate")
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onDeviceScan() },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Eszköz beolvasás")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(0.5f)
            ) {
                Text(text = "Foglaló személy")
                Divider(Modifier.fillMaxWidth().padding(top = 5.dp, bottom = 5.dp))
                Text(text = "Név: ${reservationUser?.name ?: ""}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Telefon: ${reservationUser?.phone ?: ""}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Email: ${reservationUser?.email ?: ""}")
            }
            if(leaseMode) {
                Column(
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(text = "Átvevő személy")
                    Divider(Modifier.fillMaxWidth().padding(top = 5.dp, bottom = 5.dp))
                    Text(text = "Név: ${actualUser?.name ?: ""}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Telefon: ${actualUser?.phone ?: ""}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Email: ${actualUser?.email ?: ""}")
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { onUserScan() },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = if (leaseMode) "Átvevő beolvasása" else "Átvevő hitelesítése")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { onSubmit() },
            modifier = Modifier.fillMaxWidth(),
            enabled = (leaseMode && actualUser != null && device != null) || (!leaseMode && actualUser != null && actualUser.id == reservationUser?.id)
        ) {
            Text(text = if(leaseMode) "Kiadás" else "Visszavétel")
        }
    }
}