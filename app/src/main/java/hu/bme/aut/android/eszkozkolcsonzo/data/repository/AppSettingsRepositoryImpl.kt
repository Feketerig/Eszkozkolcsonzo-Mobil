package hu.bme.aut.android.eszkozkolcsonzo.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import hu.bme.aut.android.eszkozkolcsonzo.settings.AppSettingSerializer
import hu.bme.aut.android.eszkozkolcsonzo.settings.AppSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppSettingsRepositoryImpl @Inject constructor(
    context: Context
) {

    private val dataStore: DataStore<AppSettings> = DataStoreFactory.create(
        serializer = AppSettingSerializer,
        produceFile = {context.dataStoreFile("app-settings.json")}
    )

    fun get(): Flow<AppSettings>{
        return dataStore.data
    }

    suspend fun set(appSettings: AppSettings){
        dataStore.updateData {
            it.copy(
                id = appSettings.id,
                name = appSettings.name,
                email= appSettings.email,
                stayLogin = appSettings.stayLogin,
                privilege = appSettings.privilege,
                password = appSettings.password,
                token = appSettings.token
            )
        }
    }
}