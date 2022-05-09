package hu.bme.aut.android.eszkozkolcsonzo.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.eszkozkolcsonzo.data.local.DeviceDatabase
import hu.bme.aut.android.eszkozkolcsonzo.data.network.Network
import hu.bme.aut.android.eszkozkolcsonzo.data.network.NetworkInterface
import hu.bme.aut.android.eszkozkolcsonzo.data.repository.AppSettingsRepositoryImpl
import hu.bme.aut.android.eszkozkolcsonzo.domain.use_case.ValidateEmail
import hu.bme.aut.android.eszkozkolcsonzo.domain.use_case.ValidatePassword
import hu.bme.aut.android.eszkozkolcsonzo.domain.use_case.ValidateRepeatedPassword
import hu.bme.aut.android.eszkozkolcsonzo.domain.use_case.ValidateTerms
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNetworkInterface(): NetworkInterface{
        return Network(
            HttpClient(Android) {
                install(Logging) {
                    level = LogLevel.ALL
                }
                install(JsonFeature) {
                    serializer = KotlinxSerializer()
                }
                defaultRequest {
                    if (method != HttpMethod.Get) contentType(ContentType.Application.Json)
                    accept(ContentType.Application.Json)
                }
            })
    }

    @Provides
    @Singleton
    fun provideDeviceDatabase(app: Application): DeviceDatabase {
        return Room.databaseBuilder(
            app,
            DeviceDatabase::class.java,
            "devices.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAppSettingRepository(app: Application): AppSettingsRepositoryImpl{
        return AppSettingsRepositoryImpl(app.applicationContext)
    }

    @Provides
    @Singleton
    fun provideValidateEmail(): ValidateEmail{
        return ValidateEmail()
    }

    @Provides
    @Singleton
    fun provideValidatePassword(): ValidatePassword{
        return ValidatePassword()
    }

    @Provides
    @Singleton
    fun provideValidateRepeatedPassword(): ValidateRepeatedPassword{
        return ValidateRepeatedPassword()
    }

    @Provides
    @Singleton
    fun provideValidateTerms(): ValidateTerms{
        return ValidateTerms()
    }
}