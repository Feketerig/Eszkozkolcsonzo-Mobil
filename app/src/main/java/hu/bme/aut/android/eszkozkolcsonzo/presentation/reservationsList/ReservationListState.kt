package hu.bme.aut.android.eszkozkolcsonzo.presentation.reservationsList

import hu.bme.aut.android.eszkozkolcsonzo.domain.model.ReservationInfo

data class ReservationListState(
    val reservations: List<ReservationInfo> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
)