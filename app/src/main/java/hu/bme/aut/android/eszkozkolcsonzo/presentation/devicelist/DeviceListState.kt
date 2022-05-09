package hu.bme.aut.android.eszkozkolcsonzo.presentation.devicelist

import hu.bme.aut.android.eszkozkolcsonzo.domain.model.Device

data class DeviceListState(
    val devices: List<Device> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val onlyAvailable: Boolean = true,
    val searchQuery: String = ""
)
