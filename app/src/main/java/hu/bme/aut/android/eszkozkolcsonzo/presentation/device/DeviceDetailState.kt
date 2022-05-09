package hu.bme.aut.android.eszkozkolcsonzo.presentation.device

import hu.bme.aut.android.eszkozkolcsonzo.domain.model.Device

data class DeviceDetailState(
    val device: Device? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val startDate: Long? = null,
    val endDate: Long? = null
)
