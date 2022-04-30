package id.ac.ukdw.sub1_intermediate.userSession

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

class UserPreferencesDS internal constructor(private val dataStore: DataStore<Preferences>) {
    private val CURRENT_TOKEN = stringPreferencesKey("current_token")
    private val CURRENT_NAME = stringPreferencesKey("current_name")

    suspend fun saveCurrentName(mUserName: String){
        dataStore.edit{
            it[CURRENT_NAME] = mUserName
        }
    }

    suspend fun getCurrenctName(): String? {
        val preference = dataStore.data.first()
        return preference[CURRENT_NAME] ?: ""
    }

    suspend fun saveCurrentToken(mUserToken: String){
        dataStore.edit{
            it[CURRENT_TOKEN] = mUserToken
        }
    }

    suspend fun getCurrenctToken(): String? {
        val preference = dataStore.data.first()
        return preference[CURRENT_TOKEN] ?: ""
    }

    suspend fun clearSession(){
        dataStore.edit{
            it[CURRENT_TOKEN] = ""
            it[CURRENT_NAME] = ""
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: UserPreferencesDS? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferencesDS {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferencesDS(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}