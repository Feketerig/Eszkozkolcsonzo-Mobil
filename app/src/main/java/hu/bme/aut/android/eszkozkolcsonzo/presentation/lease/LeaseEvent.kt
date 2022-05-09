package hu.bme.aut.android.eszkozkolcsonzo.presentation.lease

sealed class LeaseEvent{
    data class FindDevice(val id: Int): LeaseEvent()
    data class FindUSer(val id: Int): LeaseEvent()

    object ScanDevice: LeaseEvent()
    object ScanUser: LeaseEvent()
    object StartLease: LeaseEvent()
    object StartDrawback: LeaseEvent()
}
