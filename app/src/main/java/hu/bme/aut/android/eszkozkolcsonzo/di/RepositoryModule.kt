package hu.bme.aut.android.eszkozkolcsonzo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.eszkozkolcsonzo.data.repository.DeviceRepositoryImpl
import hu.bme.aut.android.eszkozkolcsonzo.data.repository.ReservationRepositoryImpl
import hu.bme.aut.android.eszkozkolcsonzo.domain.repository.DeviceRepository
import hu.bme.aut.android.eszkozkolcsonzo.domain.repository.ReservationRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDeviceRepository(
        deviceRepositoryImpl: DeviceRepositoryImpl
    ): DeviceRepository

    @Binds
    @Singleton
    abstract fun bindReservationRepository(
        reservationRepository: ReservationRepositoryImpl
    ): ReservationRepository
}