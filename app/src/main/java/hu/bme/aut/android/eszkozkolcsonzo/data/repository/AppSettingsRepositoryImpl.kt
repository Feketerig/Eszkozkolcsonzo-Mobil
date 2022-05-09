package hu.bme.aut.android.eszkozkolcsonzo.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import hu.bme.aut.android.eszkozkolcsonzo.domain.model.User
import hu.bme.aut.android.eszkozkolcsonzo.settings.AppSettingSerializer
import hu.bme.aut.android.eszkozkolcsonzo.settings.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppSettingsRepositoryImpl @Inject constructor(
    context: Context
) {

    private val dataStore: DataStore<AppSettings> = DataStoreFactory.create(
        serializer = AppSettingSerializer,
        produceFile = {context.dataStoreFile("app-settings.json")}
    )

    fun getUser(): Flow<User?> {
       return dataStore.data.map {
            it.user
        }
    }

    suspend fun updateUser(user: User){
        dataStore.updateData {
            it.copy(user = user)
        }
    }

    fun getStayLogin(): Flow<Boolean>{
        return dataStore.data.map {
            it.stayLogin
        }
    }

    suspend fun updateStayLogin(stayLogin: Boolean){
        dataStore.updateData {
            it.copy(stayLogin = stayLogin)
        }
    }

    fun get(): Flow<AppSettings>{
        return dataStore.data
    }

    suspend fun set(appSettings: AppSettings){
        dataStore.updateData {
            it.copy(user = appSettings.user, stayLogin = appSettings.stayLogin)
        }
    }
}